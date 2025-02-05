package com.weidi.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.UUID;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.packet.RosterPacket.ItemType;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.ReportedData.Row;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.search.UserSearchManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.weidi.QApp;
import com.weidi.bean.Session;
import com.weidi.bean.WjsmMessage;
import com.weidi.chat.GroupRoom;
import com.weidi.chat.IQOrder;
import com.weidi.listener.MsgListener;
import com.weidi.listener.XmppMessageIntercepter;
import com.weidi.listener.XmppTimetask;
import com.weidi.provider.ObtainMUCListIQ;

public class XmppUtil {
	private static String TAG = "XmppUtil";
	private static XmppUtil xmpp;
	public static List<GroupRoom> myRooms = new ArrayList<GroupRoom>();
	public static List<GroupRoom> leaveRooms = new ArrayList<GroupRoom>();
	Timer tExit;

	/**
	 * 单例模式
	 * 
	 * @return
	 */
	public static XmppUtil getInstance() {
		if (xmpp == null) {
			xmpp = new XmppUtil();
		}
		return xmpp;
	}

	public void setNull() {
		xmpp = null;
	}

	public static void joinMuc(String muc, String name) {
		GroupRoom room = new GroupRoom(muc, name);
		myRooms.add(room);
	}

	public static void getMUCList() {

		ObtainMUCListIQ iq = IQOrder.getInstance().obtainMUCList();
		GroupRoom room;
		for (ObtainMUCListIQ.Item item : iq.getItems()) {

			room = new GroupRoom(item.getMuc(), item.getName());
			myRooms.add(room);
			Logger.i(TAG, "已登录到群：" + item.getMuc());
		}

	}

	public static List<GroupRoom> getRoomList() {
		return myRooms;
	}

	public static GroupRoom getRoomByMuc(String muc) {

		for (GroupRoom item : myRooms) {
			if (muc.equals(item.getMuc())) {
				return item;
			}
		}
		return null;
	}

	/**
	 * 注册
	 * 
	 * @param account
	 *            注册帐号
	 * @param password
	 *            注册密码
	 * @return 1、注册成功 0、服务器没有返回结果2、这个账号已经存在3、注册失败
	 */
	public static int register(XMPPConnection mXMPPConnection, String account,
			String password) {

		Registration reg = new Registration();
		reg.setType(IQ.Type.SET);
		reg.setTo(mXMPPConnection.getServiceName());
		// 注意这里createAccount注册时，参数是UserName，不是jid，是"@"前面的部分。
		reg.setUsername(account);
		reg.setPassword(password);
		// 这边addAttribute不能为空，否则出错。所以做个标志是android手机创建的吧！！！！！
		reg.addAttribute("android", "geolo_createUser_android");
		PacketFilter filter = new AndFilter(new PacketIDFilter(
				reg.getPacketID()), new PacketTypeFilter(IQ.class));
		PacketCollector collector = mXMPPConnection
				.createPacketCollector(filter);
		mXMPPConnection.sendPacket(reg);
		IQ result = (IQ) collector.nextResult(SmackConfiguration
				.getPacketReplyTimeout());
		// Stop queuing results停止请求results（是否成功的结果）
		collector.cancel();
		if (result == null) {
			return 0;
		} else if (result.getType() == IQ.Type.RESULT) {
			return 1;
		} else {
			if (result.getError().toString().equalsIgnoreCase("conflict(409)")) {
				return 2;
			} else {
				return 3;
			}
		}
	}

	/**
	 * 修改用户信息
	 * 
	 * @param file
	 */
	public boolean changeVcard(VCard vcard) {
		if (QApp.getXmppConnection() == null)
			return false;
		try {
			// 加入这句代码，解决No VCard for
			ProviderManager.getInstance().addIQProvider("vCard", "vcard-temp",
					new VCardProvider());
			vcard.save(QApp.getXmppConnection());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 查询用户
	 * 
	 * @param userName
	 * @return
	 * @throws XMPPException
	 */
	public static List<Session> searchUsers(XMPPConnection mXMPPConnection,
			String userName) {
		List<Session> listUser = new ArrayList<Session>();
		try {
			UserSearchManager search = new UserSearchManager(mXMPPConnection);
			// 此处一定要加上 search.
			Form searchForm = search.getSearchForm("search."
					+ mXMPPConnection.getServiceName());
			Form answerForm = searchForm.createAnswerForm();
			answerForm.setAnswer("Username", true);
			answerForm.setAnswer("search", userName);
			ReportedData data = search.getSearchResults(answerForm, "search."
					+ mXMPPConnection.getServiceName());
			Iterator<Row> it = data.getRows();
			Row row = null;
			while (it.hasNext()) {
				row = it.next();
				Session session = new Session();
				session.setFrom(row.getValues("Username").next().toString());
				listUser.add(session);
			}
		} catch (Exception e) {

		}
		return listUser;
	}

	/**
	 * 搜索好友
	 * 
	 * @param key
	 * @return
	 */
	public static List<String> searchUser(XMPPConnection mXMPPConnection,
			String key) {
		List<String> userList = new ArrayList<String>();
		try {
			UserSearchManager search = new UserSearchManager(mXMPPConnection);
			Form searchForm = search.getSearchForm("search."
					+ Const.XMPP_DOMAIN);
			Form answerForm = searchForm.createAnswerForm();
			answerForm.setAnswer("Username", true);
			answerForm.setAnswer("search", key);
			ReportedData data = search.getSearchResults(answerForm, "search."
					+ Const.XMPP_DOMAIN);

			Iterator<Row> it = data.getRows();
			Row row = null;
			while (it.hasNext()) {
				row = it.next();
				userList.add(row.getValues("Username").next().toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userList;
	}

	/**
	 * 文件转字节
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private byte[] getFileBytes(File file) throws IOException {
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(file));
			int bytes = (int) file.length();
			byte[] buffer = new byte[bytes];
			int readBytes = bis.read(buffer);
			if (readBytes != buffer.length) {
				throw new IOException("Entire file not read");
			}
			return buffer;
		} finally {
			if (bis != null) {
				bis.close();
			}
		}
	}

	// 将InputStream转换成Bitmap
	public Bitmap InputStream2Bitmap(InputStream is) {
		return BitmapFactory.decodeStream(is);
	}

	/**
	 * 修改用户头像
	 * 
	 * @param file
	 */
	public Bitmap changeImage(File file) {
		Bitmap bitmap = null;
		if (QApp.getXmppConnection() == null)
			return bitmap;
		try {
			VCard vcard = Const.loginUser.getvCard();
			// 加入这句代码，解决No VCard for
			ProviderManager.getInstance().addIQProvider("vCard", "vcard-temp",
					new VCardProvider());

			byte[] bytes;
			bytes = getFileBytes(file);
			String encodedImage = StringUtils.encodeBase64(bytes);

			// vcard.setAvatar(bytes, encodedImage);
			// vcard.setEncodedImage(encodedImage);
			// vcard.setField("PHOTO", "<TYPE>image/jpg</TYPE><BINVAL>" +
			// encodedImage + "</BINVAL>", true);
			vcard.setField("avatar", encodedImage);
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			bitmap = InputStream2Bitmap(bais);
			// Image image = ImageIO.read(bais);
			// 　　　　　　　　ImageIcon ic = new ImageIcon(image);　
			vcard.save(QApp.getXmppConnection());

		} catch (Exception e) {
			e.printStackTrace();

		}
		return bitmap;
	}

	/**
	 * 更改用户状态
	 */
	public static void setPresence(Context context, XMPPConnection con, int code) {
		if (con == null)
			return;
		Presence presence = null;
		switch (code) {
		case 0:
			presence = new Presence(Presence.Type.available); // 在线
			break;
		case 1:
			presence = new Presence(Presence.Type.available); // 设置Q我吧
			presence.setMode(Presence.Mode.chat);
			break;
		case 2: // 隐身
			Roster roster = con.getRoster();
			Collection<RosterEntry> entries = roster.getEntries();
			for (RosterEntry entry : entries) {
				presence = new Presence(Presence.Type.unavailable);
				presence.setPacketID(Packet.ID_NOT_AVAILABLE);
				presence.setFrom(con.getUser());
				presence.setTo(entry.getUser());
			}
			// 向同一用户的其他客户端发送隐身状态
			presence = new Presence(Presence.Type.unavailable);
			presence.setPacketID(Packet.ID_NOT_AVAILABLE);
			presence.setFrom(con.getUser());
			presence.setTo(StringUtils.parseBareAddress(con.getUser()));
			break;
		case 3:
			presence = new Presence(Presence.Type.available); // 设置忙碌
			presence.setMode(Presence.Mode.dnd);
			break;
		case 4:
			presence = new Presence(Presence.Type.available); // 设置离开
			presence.setMode(Presence.Mode.away);
			break;
		case 5:
			presence = new Presence(Presence.Type.unavailable); // 离线
			break;
		default:
			break;
		}
		if (presence != null) {
			presence.setStatus(PreferencesUtils.getSharePreStr("sign"));
			con.sendPacket(presence);
		}
	}

	/**
	 * 删除当前用户
	 * 
	 * @param connection
	 * @return
	 */
	public static boolean deleteAccount(XMPPConnection connection) {
		try {
			connection.getAccountManager().deleteAccount();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 返回所有组信息 <RosterGroup>
	 * 
	 * @return List(RosterGroup)
	 */
	public static List<RosterGroup> getGroups(Roster roster) {
		List<RosterGroup> groupsList = new ArrayList<RosterGroup>();
		Collection<RosterGroup> rosterGroup = roster.getGroups();
		Iterator<RosterGroup> i = rosterGroup.iterator();
		while (i.hasNext())
			groupsList.add(i.next());
		return groupsList;
	}

	/**
	 * 返回相应(groupName)组里的所有用户<RosterEntry>
	 * 
	 * @return List(RosterEntry)
	 */
	public static List<RosterEntry> getEntriesByGroup(Roster roster,
			String groupName) {
		List<RosterEntry> EntriesList = new ArrayList<RosterEntry>();
		RosterGroup rosterGroup = roster.getGroup(groupName);
		Collection<RosterEntry> rosterEntry = rosterGroup.getEntries();
		Iterator<RosterEntry> i = rosterEntry.iterator();
		while (i.hasNext())
			EntriesList.add(i.next());
		return EntriesList;
	}

	public static List<String> getRosterAll(Roster roster) {
		Collection<RosterEntry> entries = roster.getEntries();
		List<String> data = new ArrayList<String>();
		for (RosterEntry item : entries) {
			data.add(item.getUser());
		}
		return data;
	}

	public static List<String> getRosterNone(Roster roster) {
		Collection<RosterEntry> entries = roster.getEntries();
		List<String> data = new ArrayList<String>();
		for (RosterEntry item : entries) {
			if (item.getType().equals(ItemType.none)) {
				data.add(item.getUser());
			}
		}
		return data;
	}

	public static List<String> getRosterTo(Roster roster) {
		Collection<RosterEntry> entries = roster.getEntries();
		List<String> data = new ArrayList<String>();
		for (RosterEntry item : entries) {
			if (item.getType().equals(ItemType.to)) {
				data.add(item.getUser());
			}
		}
		return data;
	}

	public static List<String> getRosterBoth(Roster roster) {
		Collection<RosterEntry> entries = roster.getEntries();
		List<String> data = new ArrayList<String>();
		for (RosterEntry item : entries) {
			Logger.i(TAG, "MI" + item.getName() + ":" + item.getType() + ":"
					+ item.getUser());
			if (item.getType().equals(ItemType.both)) {
				data.add(item.getUser());
			}
		}
		return data;
	}

	public Collection<RosterEntry> getMyRoster() {
		return getConnection().getRoster().getEntries();
	}

	/**
	 * 创建一个组
	 */
	public static boolean addGroup(Roster roster, String groupName) {
		try {
			roster.createGroup(groupName);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("jj", "创建分组异常：" + e.getMessage());
			return false;
		}
	}

	/**
	 * 修改密码
	 * 
	 * @param pwd
	 * @return
	 */
	public boolean changPwd(String pwd) {
		try {
			QApp.getXmppConnection().getAccountManager().changePassword(pwd);
			return true;
		} catch (XMPPException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除一个组
	 */
	public static boolean removeGroup(Roster roster, String groupName) {
		return false;
	}

	public static void agreeAdd(String from) {
		Presence ok = new Presence(Presence.Type.subscribed);
		ok.setTo(from);

	}

	/**
	 * 通过jid获得username
	 * 
	 * @param fullUsername
	 * @return
	 */
	public static String getUsername(String fullUsername) {
		return fullUsername.split("@")[0];
	}

	public static String getMucFrom(String muc) {
		return muc.split("/")[1];
	}

	/**
	 * 通过username获得jid
	 * 
	 * @param username
	 * @return
	 */
	public static String getFullUsername(String username) {
		return username + "@" + Const.XMPP_DOMAIN;
	}

	public static String getFullMUC(String muc) {
		return muc + "@conference.jsmny";
	}

	/**
	 * 添加一个好友 无分组
	 */
	public static boolean addUser(XMPPConnection connection, String fromName) {
		if (connection == null)
			return false;
		try {
			connection.getRoster().createEntry(getFullUsername(fromName),
					getFullUsername(fromName), null);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 添加一个好友到分组
	 * 
	 * @param roster
	 * @param userName
	 * @param name
	 * @return
	 */
	public static boolean addUsers(Roster roster, String userName, String name,
			String groupName) {
		try {
			roster.createEntry(userName, name, new String[] { groupName });
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("jj", "添加好友异常：" + e.getMessage());
			return false;
		}

	}

	/**
	 * 删除一个好友
	 * 
	 * @param roster
	 * @param userJid
	 * @return
	 */
	public static boolean removeUser(Roster roster, String userJid) {
		try {
			RosterEntry entry = roster.getEntry(userJid);
			roster.removeEntry(entry);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 把一个好友添加到一个组中
	 * 
	 * @param userJid
	 * @param groupName
	 */
	public static void addUserToGroup(final String userJid,
			final String groupName, final XMPPConnection connection) {
		RosterGroup group = connection.getRoster().getGroup(groupName);
		// 这个组已经存在就添加到这个组，不存在创建一个组
		RosterEntry entry = connection.getRoster().getEntry(userJid);
		try {
			if (group != null) {
				if (entry != null)
					group.addEntry(entry);
			} else {
				RosterGroup newGroup = connection.getRoster().createGroup(
						"我的好友");
				if (entry != null)
					newGroup.addEntry(entry);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 把一个好友从组中删除
	 * 
	 * @param userJid
	 * @param groupName
	 */
	public static void removeUserFromGroup(final String userJid,
			final String groupName, final XMPPConnection connection) {
		RosterGroup group = connection.getRoster().getGroup(groupName);
		if (group != null) {
			try {
				RosterEntry entry = connection.getRoster().getEntry(userJid);
				if (entry != null)
					group.removeEntry(entry);

			} catch (XMPPException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 修改签名
	 */
	public static void changeSign(XMPPConnection connection, int code,
			String content) {
		Presence presence = getOnlineStatus(code);
		presence.setStatus(content);
		connection.sendPacket(presence);
	}

	public static void sendAgreeAddFriend(XMPPConnection con, String toJid) {
		Presence ok = new Presence(Presence.Type.subscribed);
		ok.setTo(toJid);
		con.sendPacket(ok);
	}

	/**
	 * 发送好友请求
	 * 
	 * @param userName
	 */
	public static void sendAddFriendRequest(XMPPConnection con, String toJid) {
		Presence subscription = new Presence(Presence.Type.subscribe);
		subscription.setTo(toJid);
		con.sendPacket(subscription);
	}

	/**
	 * 发送消息
	 * 
	 * @param position
	 * @param content
	 * @param touser
	 * @throws XMPPException
	 */
	public static void sendMessage(XMPPConnection mXMPPConnection,
			String content, String touser) throws XMPPException {
		if (mXMPPConnection == null || !mXMPPConnection.isConnected()) {
			throw new XMPPException();
		}
		ChatManager chatmanager = mXMPPConnection.getChatManager();
		Chat chat = chatmanager.createChat(touser + "@" + Const.XMPP_DOMAIN,
				null);
		Message msg = new Message();
		msg.setBody(content);
		if (chat != null) {
			chat.sendMessage(msg);
			Logger.i(TAG, "sendMessge" + content + "to" + touser);
		}
	}

	/**
	 * 发送文件消息
	 * 
	 * @param mXMPPConnection
	 * @param content
	 * @param fileType
	 * @param touser
	 * @param messageListener
	 * @throws XMPPException
	 */
	public static void sendFileMsg(String fileName, String fileType,
			String touser) throws XMPPException {
		XMPPConnection mXMPPConnection = QApp.getXmppConnection();
		if (mXMPPConnection == null || !mXMPPConnection.isConnected()) {
			throw new XMPPException();
		}
		Message msg = new Message();
		WjsmMessage jsm = new WjsmMessage();
		StringBuffer sb = new StringBuffer();
		sb.append("<jsm xmlns=\"");
		sb.append(WjsmMessage.NAME_SPACE);
		sb.append("\"><");
		sb.append(fileType);
		sb.append(">");
		sb.append(fileName);
		sb.append("</");
		sb.append(fileType);
		sb.append("></jsm>");
		jsm.setPacketContent(sb);
		msg.addExtension(jsm);
		if (touser.contains("g")) {
			GroupRoom room = getRoomByMuc(getFullMUC(touser));
			msg.setBody("");
			msg.setType(Type.groupchat);
			msg.setTo(getFullMUC(touser));
			msg.setThread(UUID.randomUUID().toString());
			room.getMultiUserChat().sendMessage(msg);

		} else {

			ChatManager chatmanager = mXMPPConnection.getChatManager();
			Chat chat;
			chat = chatmanager.createChat(
					touser + "@" + mXMPPConnection.getServiceName(), null);

			if (chat != null) {
				chat.sendMessage(msg);
				Logger.i(TAG, "sendMessge" + msg.toXML());
			}
		}
	}

	/**
	 * 获取用户信息
	 * 
	 * @param user
	 * @return
	 */
	public static VCard getUserInfo(String user) { // null 时查自己
		try {
			VCard vcard = new VCard();
			// 加入这句代码，解决No VCard for
			if (vcard != null) {

				ProviderManager.getInstance().addIQProvider("vCard",
						"vcard-temp", new VCardProvider());
				if (user == null) {
					vcard.load(QApp.getXmppConnection());
				} else {
					vcard.load(QApp.getXmppConnection(), user + "@"
							+ Const.XMPP_DOMAIN);
				}
				if (vcard != null)
					return vcard;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 发送文本消息
	 * 
	 * @param mXMPPConnection
	 * @param content
	 * @param touser
	 * @param messageListener
	 * @throws XMPPException
	 */
	public static void sendTextMsg(String content, String touser)
			throws XMPPException {
		XMPPConnection mXMPPConnection = QApp.getXmppConnection();
		if (mXMPPConnection == null || !mXMPPConnection.isConnected()) {
			throw new XMPPException();
		}
		if (touser.contains("g")) {
			GroupRoom room = getRoomByMuc(getFullMUC(touser));
			if (room != null) {
				org.jivesoftware.smack.packet.Message msg = new Message();
				msg.setBody(content);
				msg.setType(Type.groupchat);
				msg.setTo(getFullMUC(touser));
				msg.setThread(UUID.randomUUID().toString());
				room.getMultiUserChat().sendMessage(msg);
			}

		} else {
			org.jivesoftware.smack.packet.Message msg = new Message();
			msg.setBody(content);
			ChatManager chatmanager = mXMPPConnection.getChatManager();
			Chat chat;
			chat = chatmanager.createChat(
					touser + "@" + mXMPPConnection.getServiceName(), null);

			if (chat != null) {
				chat.sendMessage(msg);
			}
		}

	}

	public static void sendMessage(XMPPConnection mXMPPConnection,
			String content, String touser, MessageListener messageListener)
			throws XMPPException {
		if (mXMPPConnection == null || !mXMPPConnection.isConnected()) {
			throw new XMPPException();
		}
		ChatManager chatmanager = mXMPPConnection.getChatManager();
		Chat chat = chatmanager.createChat(touser + "@" + Const.XMPP_DOMAIN,
				messageListener);
		if (chat != null) {
			chat.sendMessage(content);
			Logger.i(TAG, "sendMessge" + content);
		}
	}

	public static void setOnlineStatus(ImageView iv_stutas, int code,
			TextView tv_stutas, String[] items) {
		switch (code) {
		case 0:// 在线
				// iv_stutas.setImageResource(R.drawable.evk);
				// tv_stutas.setText(items[0]);
			break;
		case 1:// q我吧
				// iv_stutas.setImageResource(R.drawable.evm);
				// tv_stutas.setText(items[1]);
			break;
		case 2:// 隐身
				// iv_stutas.setImageResource(R.drawable.evf);
				// tv_stutas.setText(items[2]);
			break;
		case 3:// 忙碌
				// iv_stutas.setImageResource(R.drawable.evd);
				// tv_stutas.setText(items[3]);
			break;
		case 4:// 离开
				// iv_stutas.setImageResource(R.drawable.evp);
				// tv_stutas.setText(items[4]);
			break;
		default:
			break;
		}

	}

	public static Presence getOnlineStatus(int code) {
		Presence presence = null;
		switch (code) {
		case 0:
			presence = new Presence(Presence.Type.available); // 在线
			break;
		case 1:
			presence = new Presence(Presence.Type.available); // 设置Q我吧
			presence.setMode(Presence.Mode.chat);
			break;
		case 2: // 隐身
			presence = new Presence(Presence.Type.unavailable);
			break;
		case 3:
			presence = new Presence(Presence.Type.available); // 设置忙碌
			presence.setMode(Presence.Mode.dnd);
			break;
		case 4:
			presence = new Presence(Presence.Type.available); // 设置离开
			presence.setMode(Presence.Mode.away);
			break;
		case 5:
			presence = new Presence(Presence.Type.unavailable); // 离线
			break;
		default:
			break;
		}
		return presence;
	}

	public XMPPConnection getConnection() {
		return XmppConnectionManager.getInstance().getConnection();
	}

	public void conXmpp() {
		XMPPConnection xmppConnection = getConnection();
		Log.i(TAG,
				"xmppConnection.isConnected()" + xmppConnection.isConnected());
		if (xmppConnection.isConnected()) {

			XmppUtil.getInstance().login(Const.USER_ACCOUNT, Const.USER_PWD);
			Log.i(TAG, "xmppConnection.login success");
		} else {
			tExit = new Timer();
			tExit.schedule(new XmppTimetask(), 5000);
		}
	}

	public Boolean login(String account, String password) {
		try {

			if (getConnection() == null)
				return false;
			if (!getConnection().isAuthenticated()
					&& getConnection().isConnected()) {
				getConnection().login(account, password, Const.RESOUCE_ID);
				// // 更改在线状态
				Presence presence = new Presence(Presence.Type.available);
				// Constants.USER_STATUS = presence.getStatus();
				presence.setMode(Presence.Mode.available);
				getConnection().sendPacket(presence);
				Const.USER_ACCOUNT = account;
				ChatManager chatmanager = getConnection().getChatManager();
				chatmanager.addChatListener(new ChatManagerListener() {
					@Override
					public void chatCreated(Chat arg0, boolean arg1) {
						arg0.addMessageListener(new MsgListener());
					}
				});
				return true;
			}

		} catch (XMPPException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	/**
	 * 关闭连接
	 */
	public void closeConnection() {

		XmppConnectionManager.getInstance().closeConnection();
		xmpp = null;

	}

}

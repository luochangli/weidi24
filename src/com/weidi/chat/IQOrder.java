package com.weidi.chat;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.provider.ProviderManager;

import android.util.Log;

import com.weidi.QApp;
import com.weidi.provider.BingNewPhoneIQ;
import com.weidi.provider.BingNewPhoneProvider;
import com.weidi.provider.CreateMUCIQ;
import com.weidi.provider.CreateMUCProvider;
import com.weidi.provider.DelRemarkIQ;
import com.weidi.provider.DelRemarkProvider;
import com.weidi.provider.EntryMUCPresence;
import com.weidi.provider.ForgetPasswordIQ;
import com.weidi.provider.ForgetPasswrodProvider;
import com.weidi.provider.GetAccountByPhoneIQ;
import com.weidi.provider.GetAccountByPhoneProvider;
import com.weidi.provider.GetBindPhoneIQ;
import com.weidi.provider.GetBindPhoneProvider;
import com.weidi.provider.GetRemarkIQ;
import com.weidi.provider.GetRemarkProvider;
import com.weidi.provider.GetPassWordHintProvider;
import com.weidi.provider.GetPasswordHintIQ;
import com.weidi.provider.MUCAddmemberIQ;
import com.weidi.provider.MUCAddmemberProvider;
import com.weidi.provider.MUCChangNickIQ;
import com.weidi.provider.MUCChangNickProvider;
import com.weidi.provider.NewsIQ;
import com.weidi.provider.ObtainMUCChangInfoIQ;
import com.weidi.provider.ObtainMUCChangInfoProvider;
import com.weidi.provider.ObtainMUCDestroyIQ;
import com.weidi.provider.ObtainMUCDestroyProvider;
import com.weidi.provider.ObtainMUCExitIQ;
import com.weidi.provider.ObtainMUCExitProvider;
import com.weidi.provider.ObtainMUCInfoIQ;
import com.weidi.provider.ObtainMUCInfoProvider;
import com.weidi.provider.ObtainMUCKickIQ;
import com.weidi.provider.ObtainMUCKickProvider;
import com.weidi.provider.ObtainMUCListIQ;
import com.weidi.provider.ObtainMUCListProvider;
import com.weidi.provider.ObtainMUCmemberIQ;
import com.weidi.provider.ObtainMUCmemberProvider;
import com.weidi.provider.RegAuthcodeIQ;
import com.weidi.provider.RegAuthcodeProvider;
import com.weidi.provider.RegIQ;
import com.weidi.provider.RegProvider;
import com.weidi.provider.SaveRemarkIQ;
import com.weidi.provider.SaveRemarkProvider;
import com.weidi.provider.SuggestIQ;
import com.weidi.provider.SuggestProvider;
import com.weidi.provider.UpdateAkpIQ;
import com.weidi.provider.UpdateAkpIQProvider;
import com.weidi.provider.testProvider;
import com.weidi.provider.sign.IntegrationIQ;
import com.weidi.provider.sign.IntegrationProvider;
import com.weidi.provider.sign.SignInIQ;
import com.weidi.provider.sign.SignInProvider;
import com.weidi.util.Const;
import com.weidi.util.Logger;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-7-9 下午1:54:50
 * @Description 1.0
 */
public class IQOrder {

	private static String TAG = "ChatGroupOrder";
	private static IQOrder instance;
	private XMPPConnection conn = QApp.getXmppConnection();
	private ProviderManager pm = ProviderManager.getInstance();

	public static IQOrder getInstance() {
		if (instance == null) {
			instance = new IQOrder();
		}
		return instance;
	}

	/**
	 * 创建群
	 * 
	 * @param mucName
	 * @param description
	 * @param usernick
	 */
	public CreateMUCIQ createMUCRoom(String mucName, String description,
			String usernick) {
		try {
			CreateMUCIQ iq = new CreateMUCIQ(mucName, description, usernick);
			iq.setType(IQ.Type.SET);

			pm.addIQProvider(Const.QUERY, Const.XMLNS, new CreateMUCProvider());
			PacketFilter filter = new AndFilter(new PacketIDFilter(
					iq.getPacketID()), new PacketTypeFilter(CreateMUCIQ.class));
			PacketCollector collector = conn.createPacketCollector(filter);
			conn.sendPacket(iq);
			CreateMUCIQ result = (CreateMUCIQ) collector
					.nextResult(SmackConfiguration.getPacketReplyTimeout());
			return result;
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * 获取群列表
	 * 
	 * @param muc
	 */
	public ObtainMUCListIQ obtainMUCList() {
		try {
			pm.addIQProvider(Const.QUERY, Const.XMLNS,
					new ObtainMUCListProvider());
			ObtainMUCListIQ iq = new ObtainMUCListIQ();
			iq.setType(IQ.Type.GET);
			PacketFilter filter = new AndFilter(new PacketIDFilter(
					iq.getPacketID()), new PacketTypeFilter(
					ObtainMUCListIQ.class));
			PacketCollector collector = conn.createPacketCollector(filter);
			conn.sendPacket(iq);
			ObtainMUCListIQ result = (ObtainMUCListIQ) collector
					.nextResult(SmackConfiguration.getPacketReplyTimeout());

			return result;
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * 获取指定群信息
	 * 
	 * @param muc
	 */
	public ObtainMUCInfoIQ obtainMUCInfo(String muc) {
		try {
			pm.addIQProvider(Const.QUERY, Const.XMLNS,
					new ObtainMUCInfoProvider());
			ObtainMUCInfoIQ iq = new ObtainMUCInfoIQ();
			iq.setMuc(muc);
			iq.setType(IQ.Type.GET);

			PacketFilter filter = new AndFilter(new PacketIDFilter(
					iq.getPacketID()), new PacketTypeFilter(
					ObtainMUCInfoIQ.class));
			PacketCollector collector = conn.createPacketCollector(filter);
			conn.sendPacket(iq);
			ObtainMUCInfoIQ result = (ObtainMUCInfoIQ) collector
					.nextResult(SmackConfiguration.getPacketReplyTimeout());

			return result;
		} catch (Exception e) {
			Logger.e(TAG, e.toString());
			return null;
		}

	}

	/**
	 * 修改群信息
	 * 
	 * @param muc
	 * @param name
	 * @param description
	 */
	public void mucChangInfo(String muc, String name, String description) {
		try {
			pm.addIQProvider(Const.QUERY, Const.XMLNS,
					new ObtainMUCChangInfoProvider());
			ObtainMUCChangInfoIQ iq = new ObtainMUCChangInfoIQ();
			iq.setType(IQ.Type.SET);
			iq.setMuc(muc);
			iq.setName(name);
			iq.setDescription(description);
			conn.sendPacket(iq);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void entryMUCRoom(String muc, String weidi) {
		try {
			Presence presence = new Presence(Presence.Type.available);
			EntryMUCPresence entryPresence = new EntryMUCPresence();
			presence.setTo(muc + "@conference.jsmny/" + weidi);
			presence.addExtension(entryPresence);
			conn.sendPacket(presence);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * 销毁群
	 * 
	 * @param muc
	 * 
	 */
	public ObtainMUCDestroyIQ mucDestroy(String muc) {
		try {
			pm.addIQProvider(Const.QUERY, Const.XMLNS,
					new ObtainMUCDestroyProvider());
			ObtainMUCDestroyIQ iq = new ObtainMUCDestroyIQ();
			iq.setType(IQ.Type.SET);
			iq.setMuc(muc);
			PacketFilter filter = new AndFilter(new PacketIDFilter(
					iq.getPacketID()), new PacketTypeFilter(
					ObtainMUCDestroyIQ.class));
			PacketCollector collector = conn.createPacketCollector(filter);
			conn.sendPacket(iq);
			ObtainMUCDestroyIQ result = (ObtainMUCDestroyIQ) collector
					.nextResult(SmackConfiguration.getPacketReplyTimeout());
			return result;
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * 退出群
	 * 
	 * @param muc
	 * 
	 */
	public ObtainMUCExitIQ mucExit(String muc) {
		try {
			pm.addIQProvider(Const.QUERY, Const.XMLNS,
					new ObtainMUCExitProvider());
			ObtainMUCExitIQ iq = new ObtainMUCExitIQ();
			iq.setType(IQ.Type.SET);
			iq.setMuc(muc);
			PacketFilter filter = new AndFilter(new PacketIDFilter(
					iq.getPacketID()), new PacketTypeFilter(
					ObtainMUCExitIQ.class));
			PacketCollector collector = conn.createPacketCollector(filter);
			conn.sendPacket(iq);
			ObtainMUCExitIQ result = (ObtainMUCExitIQ) collector
					.nextResult(SmackConfiguration.getPacketReplyTimeout());
			return result;
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * 踢人
	 * 
	 * @param muc
	 * 
	 */
	public ObtainMUCKickIQ mucKick(String muc, String jid) {
		try {

			pm.addIQProvider(Const.QUERY, Const.XMLNS,
					new ObtainMUCKickProvider());
			ObtainMUCKickIQ iq = new ObtainMUCKickIQ();
			iq.setType(IQ.Type.SET);
			iq.setMuc(muc);
			iq.setJid(jid);
			PacketFilter filter = new AndFilter(new PacketIDFilter(
					iq.getPacketID()), new PacketTypeFilter(
					ObtainMUCKickIQ.class));
			PacketCollector collector = conn.createPacketCollector(filter);
			conn.sendPacket(iq);
			ObtainMUCKickIQ result = (ObtainMUCKickIQ) collector
					.nextResult(SmackConfiguration.getPacketReplyTimeout());
			return result;
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * 添加好友
	 * 
	 * @param muc
	 * @param jid
	 * @param nick
	 */
	public void mucAddmember(String muc, String jid, String nick) {
		try {
			pm.addIQProvider(Const.QUERY, Const.XMLNS,
					new MUCAddmemberProvider());
			MUCAddmemberIQ iq = new MUCAddmemberIQ();
			iq.setType(IQ.Type.SET);
			iq.setMuc(muc);
			iq.setJid(jid);
			iq.setNick(nick);
			conn.sendPacket(iq);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * 获取群成员列表
	 * 
	 * @param muc
	 * 
	 */
	public ObtainMUCmemberIQ obtainMUCGetmember(String muc) {
		try {
			pm.addIQProvider(Const.QUERY, Const.XMLNS,
					new ObtainMUCmemberProvider());
			ObtainMUCmemberIQ iq = new ObtainMUCmemberIQ();
			iq.setType(IQ.Type.GET);
			iq.setMuc(muc);

			PacketFilter filter = new AndFilter(new PacketIDFilter(
					iq.getPacketID()), new PacketTypeFilter(
					ObtainMUCmemberIQ.class));
			PacketCollector collector = conn.createPacketCollector(filter);
			conn.sendPacket(iq);
			ObtainMUCmemberIQ result = (ObtainMUCmemberIQ) collector
					.nextResult(SmackConfiguration.getPacketReplyTimeout());
			return result;
		} catch (Exception e) {
			Logger.e(TAG, e.toString());
			return null;
		}

	}

	/**
	 * 修改群呢称
	 * 
	 * @param muc
	 * @param nickname
	 */
	public void mucChangNick(String muc, String nickname) {
		try {
			pm.addIQProvider(Const.QUERY, Const.XMLNS,
					new MUCChangNickProvider());
			MUCChangNickIQ iq = new MUCChangNickIQ();
			iq.setType(IQ.Type.SET);
			iq.setMuc(muc);
			iq.setNickname(nickname);
			Log.i("TAG", iq.toXML());
			conn.sendPacket(iq);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * 获得指定好友的备注
	 * 
	 * @param muc
	 * @param nickname
	 */
	public GetRemarkIQ getRemark(String username) {
		try {
			pm.addIQProvider(Const.QUERY_REMARK, Const.XMLNS_REMARK,
					new GetRemarkProvider());
			GetRemarkIQ iq = new GetRemarkIQ();
			iq.setType(IQ.Type.GET);
			iq.setUsername(username);
			PacketFilter filter = new AndFilter(new PacketIDFilter(
					iq.getPacketID()), new PacketTypeFilter(GetRemarkIQ.class));
			PacketCollector collector = conn.createPacketCollector(filter);
			conn.sendPacket(iq);
			GetRemarkIQ result = (GetRemarkIQ) collector
					.nextResult(SmackConfiguration.getPacketReplyTimeout());
			return result;
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * 保存好友的备注
	 * 
	 * @param muc
	 * @param nickname
	 */
	public SaveRemarkIQ saveRemark(String username, String nickname) {
		try {
			pm.addIQProvider(Const.QUERY_REMARK, Const.XMLNS_REMARK,
					new SaveRemarkProvider());
			SaveRemarkIQ iq = new SaveRemarkIQ();
			iq.setType(IQ.Type.SET);
			iq.setUsername(username);
			iq.setNickname(nickname);

			PacketFilter filter = new AndFilter(new PacketIDFilter(
					iq.getPacketID()), new PacketTypeFilter(SaveRemarkIQ.class));
			PacketCollector collector = conn.createPacketCollector(filter);
			conn.sendPacket(iq);
			SaveRemarkIQ result = (SaveRemarkIQ) collector
					.nextResult(SmackConfiguration.getPacketReplyTimeout());
			return result;
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * 删除好友的备注
	 * 
	 * @param muc
	 * @param nickname
	 */
	public DelRemarkIQ delRemark(String username) {
		try {
			pm.addIQProvider(Const.QUERY_REMARK, Const.XMLNS_REMARK,
					new DelRemarkProvider());

			DelRemarkIQ iq = new DelRemarkIQ();
			iq.setType(IQ.Type.SET);
			iq.setUsername(username);

			PacketFilter filter = new AndFilter(new PacketIDFilter(
					iq.getPacketID()), new PacketTypeFilter(DelRemarkIQ.class));
			PacketCollector collector = conn.createPacketCollector(filter);
			conn.sendPacket(iq);
			DelRemarkIQ result = (DelRemarkIQ) collector
					.nextResult(SmackConfiguration.getPacketReplyTimeout());
			return result;
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * 通过手机号获取微迪号
	 * 
	 * @param phone
	 * 
	 */
	public GetAccountByPhoneIQ getAccountByPhone(String phone) {
		try {
			pm.addIQProvider(Const.QUERY_User, Const.XMLNS_User,
					new GetAccountByPhoneProvider());
			GetAccountByPhoneIQ iq = new GetAccountByPhoneIQ();
			iq.setType(IQ.Type.GET);
			iq.setPhone(phone);
			PacketFilter filter = new AndFilter(new PacketIDFilter(
					iq.getPacketID()), new PacketTypeFilter(
					GetAccountByPhoneIQ.class));
			PacketCollector collector = conn.createPacketCollector(filter);
			conn.sendPacket(iq);
			GetAccountByPhoneIQ result = (GetAccountByPhoneIQ) collector
					.nextResult(SmackConfiguration.getPacketReplyTimeout());
			Logger.i("Order", result.getAccount());
			return result;
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * 注册发的IQ
	 * 
	 * @param phone
	 * @param nickname
	 * @param passwordhint
	 * @param password
	 * @param answer
	 */
	public RegIQ reg(String phone, String password, String nickname,
			String authcode) {
		try {
			pm.addIQProvider(Const.QUERY_User, Const.XMLNS_User,
					new RegProvider());

			RegIQ iq = new RegIQ();
			iq.setType(IQ.Type.SET);
			iq.setPhone(phone);
			iq.setPassword(password);
			iq.setNickname(nickname);
			iq.setAuthcode(authcode);
			PacketFilter filter = new AndFilter(new PacketIDFilter(
					iq.getPacketID()), new PacketTypeFilter(RegIQ.class));
			PacketCollector collector = conn.createPacketCollector(filter);
			conn.sendPacket(iq);
			RegIQ result = (RegIQ) collector.nextResult(SmackConfiguration
					.getPacketReplyTimeout());
			return result;
		} catch (Exception e) {
			return null;

		}

	}

	/**
	 * 忘记密码
	 * 
	 * @param phone
	 * @param password
	 * @param answer
	 */
	public ForgetPasswordIQ forgetPassword(String phone, String password,
			String authcode) {
		try {
			pm.addIQProvider(Const.QUERY_User, Const.XMLNS_User,
					new ForgetPasswrodProvider());
			ForgetPasswordIQ iq = new ForgetPasswordIQ();
			iq.setType(IQ.Type.SET);
			iq.setPhone(phone);
			iq.setPassword(password);
			iq.setAuthcode(authcode);
			PacketFilter filter = new AndFilter(new PacketIDFilter(
					iq.getPacketID()), new PacketTypeFilter(
					ForgetPasswordIQ.class));
			PacketCollector collector = conn.createPacketCollector(filter);
			conn.sendPacket(iq);
			ForgetPasswordIQ result = (ForgetPasswordIQ) collector
					.nextResult(SmackConfiguration.getPacketReplyTimeout());
			return result;
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * 绑定新手机
	 * 
	 * @param phone
	 * @param answer
	 */
	public void bingNewPhone(String phone, String answer) {
		try {
			pm.addIQProvider(Const.QUERY_User, Const.XMLNS_User,
					new BingNewPhoneProvider());
			BingNewPhoneIQ iq = new BingNewPhoneIQ();
			iq.setType(IQ.Type.SET);
			iq.setPhone(phone);
			Log.i("TAG", iq.toXML());
			conn.sendPacket(iq);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * 获得绑定的手机
	 * 
	 */
	public void getBindPhone() {
		try {
			pm.addIQProvider(Const.QUERY_User, Const.XMLNS_User,
					new GetBindPhoneProvider());
			GetBindPhoneIQ iq = new GetBindPhoneIQ();
			iq.setType(IQ.Type.GET);
			conn.sendPacket(iq);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void getPwdHint(String weidi) {
		try {
			pm.addIQProvider(Const.QUERY_User, Const.XMLNS_User,
					new GetPassWordHintProvider());
			GetPasswordHintIQ iq = new GetPasswordHintIQ();
			iq.setType(IQ.Type.GET);
			iq.setAccount(weidi);

			conn.sendPacket(iq);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public RegAuthcodeIQ getRegAuthCode(String phone) {
		try {
			pm.addIQProvider(Const.QUERY_User, Const.XMLNS_User,
					new RegAuthcodeProvider());

			RegAuthcodeIQ iq = new RegAuthcodeIQ();
			iq.setType(IQ.Type.GET);
			iq.setPhone(phone);
			PacketFilter filter = new AndFilter(new PacketIDFilter(
					iq.getPacketID()),
					new PacketTypeFilter(RegAuthcodeIQ.class));
			PacketCollector collector = conn.createPacketCollector(filter);
			conn.sendPacket(iq);
			RegAuthcodeIQ result = (RegAuthcodeIQ) collector
					.nextResult(SmackConfiguration.getPacketReplyTimeout());
			return result;
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * 获取新闻
	 * 
	 * @param muc
	 * @param nickname
	 */
	public void getNews() {
		try {
			pm.addIQProvider(Const.QUERY_News, Const.XMLNS_News,
					new testProvider());

			NewsIQ qq = new NewsIQ();
			qq.setType(IQ.Type.GET);
			// iq.setDatetime(datetime);
			Log.i("TAG", qq.toXML());
			conn.sendPacket(qq);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 获取新闻
	 * 
	 * @param muc
	 * @param nickname
	 */
	public void getNews2(String datetime) {
		try {
			pm.addIQProvider(Const.QUERY_News, Const.XMLNS_News,
					new testProvider());

			NewsIQ qq = new NewsIQ();
			qq.setType(IQ.Type.GET);
			qq.setCreatedatetime(datetime);
			Log.i("TAG", qq.toXML());
			conn.sendPacket(qq);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public SuggestIQ sendSuggest(String msg) {
		try {
			pm.addIQProvider(Const.QUERY_User, "com:jsm:suggest",
					new SuggestProvider());
			SuggestIQ iq = new SuggestIQ();
			iq.setType(Type.SET);
			iq.setSuggest(msg);
			PacketFilter filter = new AndFilter(new PacketIDFilter(
					iq.getPacketID()), new PacketTypeFilter(SuggestIQ.class));
			PacketCollector collector = conn.createPacketCollector(filter);
			conn.sendPacket(iq);
			SuggestIQ result = (SuggestIQ) collector
					.nextResult(SmackConfiguration.getPacketReplyTimeout());
			return result;
		} catch (Exception e) {
			return null;
		}

	}

	public UpdateAkpIQ sendUpdate(int oldcode) {
		try {
			pm.addIQProvider(Const.QUERY_User, "com:jsm:system",
					new UpdateAkpIQProvider());
			UpdateAkpIQ iq = new UpdateAkpIQ();
			iq.setType(Type.GET);
			iq.setOldcode(oldcode);
			PacketFilter filter = new AndFilter(new PacketIDFilter(
					iq.getPacketID()), new PacketTypeFilter(UpdateAkpIQ.class));
			PacketCollector collector = conn.createPacketCollector(filter);
			conn.sendPacket(iq);
			UpdateAkpIQ result = (UpdateAkpIQ) collector
					.nextResult(SmackConfiguration.getPacketReplyTimeout());
			return result;
		} catch (Exception e) {
			return null;
		}
	}
	
	public SignInIQ sendSignIn(){
		try {
			pm.addIQProvider(Const.QUERY, "com:jsm:user",
					new SignInProvider());
			SignInIQ iq = new SignInIQ();
			iq.setType(Type.SET);
		
			PacketFilter filter = new AndFilter(new PacketIDFilter(
					iq.getPacketID()), new PacketTypeFilter(SignInIQ.class));
			PacketCollector collector = conn.createPacketCollector(filter);
			conn.sendPacket(iq);
			SignInIQ result = (SignInIQ) collector
					.nextResult(SmackConfiguration.getPacketReplyTimeout());
			return result;
		} catch (Exception e) {
			return null;
		}
	}
	
	public IntegrationIQ sendIntegrationIQ(){
		try {
			pm.addIQProvider(Const.QUERY, "com:jsm:user",
					new IntegrationProvider());
			IntegrationIQ iq = new IntegrationIQ();
			iq.setType(Type.GET);
		
			PacketFilter filter = new AndFilter(new PacketIDFilter(
					iq.getPacketID()), new PacketTypeFilter(IntegrationIQ.class));
			PacketCollector collector = conn.createPacketCollector(filter);
			conn.sendPacket(iq);
			IntegrationIQ result = (IntegrationIQ) collector
					.nextResult(SmackConfiguration.getPacketReplyTimeout());
			return result;
		} catch (Exception e) {
			return null;
		}
	}

}

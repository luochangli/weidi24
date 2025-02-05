package com.weidi.util;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.provider.PrivacyProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.bytestreams.socks5.provider.BytestreamsProvider;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.search.UserSearch;

import com.weidi.QApp;
import com.weidi.listener.CheckConnectionListener;
import com.weidi.listener.FriendsPacketListener;
import com.weidi.listener.XmppIQInterceptor;
import com.weidi.listener.XmppIQListener;
import com.weidi.listener.XmppMessageIntercepter;
import com.weidi.listener.XmppPresenceInterceptor;
import com.weidi.listener.XmppRosterListener;
import com.weidi.provider.BindPhone;
import com.weidi.provider.BindPhoneProvider;
import com.weidi.provider.Near;
import com.weidi.provider.NearProvider;
import com.weidi.provider.NearTime;
import com.weidi.provider.NearTimeProvider;

/**
 * 
 * XMPP服务器连接工具类.
 */

public class XmppConnectionManager {
	private static XmppConnectionManager xmppConnectionManager;
	private static XMPPConnection connection;
	static {
		try {
			Class.forName("org.jivesoftware.smack.ReconnectionManager");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private XmppConnectionManager() {

	}

	public void setConnNull() {
		connection = null;
	}

	/**
	 * 关闭连接
	 */
	public void closeConnection() {
		if (connection != null) {

			try {
				connection.disconnect();
			} catch (Exception e) {

				e.printStackTrace();
			} finally {
				connection = null;
				xmppConnectionManager = null;
			}
		}
	}

	/**
	 * 创建连接
	 */
	public XMPPConnection getConnection() {
		if (connection == null) {
			init();
		}
		return connection;
	}

	public static XmppConnectionManager getInstance() {
		if (xmppConnectionManager == null) {
			xmppConnectionManager = new XmppConnectionManager();
		}
		return xmppConnectionManager;
	}

	// init
	private Boolean init() {
		try {
			if (null == connection || !connection.isAuthenticated()) {
				Connection.DEBUG_ENABLED = false;

				ConnectionConfiguration connectionConfig = new ConnectionConfiguration(
						Const.XMPP_HOST, Const.XMPP_PORT);
				// connectionConfig.setSASLAuthenticationEnabled(false);//
				// 不使用SASL验证，设置为false
				// connectionConfig
				// .setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
				// 允许自动连接
				connectionConfig.setReconnectionAllowed(true);
				// 允许登陆成功后更新在线状态
				connectionConfig.setSendPresence(true);
				connectionConfig.setSecurityMode(SecurityMode.disabled);
				connectionConfig.setSASLAuthenticationEnabled(false);
				// 收到好友邀请后manual表示需要经过同意,accept_all表示不经同意自动为好友
				Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.manual);
				connection = new XMPPConnection(connectionConfig);
				connection.connect();
				ProviderManager pm = ProviderManager.getInstance();
				configure(pm);

				setPacketListener(connection);
				Const.XMPP_DOMAIN = connection.getServiceName();
				Logger.i("Conntion", Const.XMPP_DOMAIN);
				return true;
			}

		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	CheckConnectionListener checkConnectionListener;

	private void setPacketListener(XMPPConnection connection) {

		// packet监听
		connection.addPacketListener(new XmppIQListener(),
				new PacketTypeFilter(IQ.class));

		connection.addPacketInterceptor(new XmppMessageIntercepter(),
				new PacketTypeFilter(Message.class));
		// 添加xmpp连接监听
		checkConnectionListener = new CheckConnectionListener();
		connection.addConnectionListener(checkConnectionListener);
		// 注册好友状态更新监听
		FriendsPacketListener friendsPacketListener = new FriendsPacketListener();
		PacketFilter filter = new AndFilter(
				new PacketTypeFilter(Presence.class));
		connection.addPacketListener(friendsPacketListener, filter);
		// 注册好友拦截器
		connection.addPacketInterceptor(new XmppPresenceInterceptor(),
				new PacketTypeFilter(Presence.class));
		// IQ包拦截器
		connection.addPacketInterceptor(new XmppIQInterceptor(),
				new PacketTypeFilter(IQ.class));
		XmppUtil.setPresence(QApp.getInstance(), connection,
				PreferencesUtils.getSharePreInt("online_status"));// 设置在线状态
		// 注册通许录监听器
		Roster roster = connection.getRoster();
		roster.addRosterListener(new XmppRosterListener());
	}

	public void configure(ProviderManager pm) {
		pm.addIQProvider(BindPhone.ELEMENT_NAME, BindPhone.NAMESPACE,
				new BindPhoneProvider());

		pm.addIQProvider(NearTime.ELEMENT_NAME, NearTime.NAMESPACE,
				new NearTimeProvider());

		pm.addIQProvider(Near.ELEMENT_NAME, Near.NAMESPACE, new NearProvider());

		pm.addIQProvider("query", "jabber:iq:private",
				new PrivateDataManager.PrivateDataIQProvider());
		// Time
		try {
			pm.addIQProvider("query", "jabber:iq:time",
					Class.forName("org.jivesoftware.smackx.packet.Time"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Roster Exchange
		pm.addExtensionProvider("x", "jabber:x:roster",
				new RosterExchangeProvider());
		// Message Events
		pm.addExtensionProvider("x", "jabber:x:event",
				new MessageEventProvider());
		// Chat State
		pm.addExtensionProvider("active",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("composing",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("paused",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("inactive",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("gone",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		// XHTML
		pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im",
				new XHTMLExtensionProvider());
		// Group Chat Invitations
		pm.addExtensionProvider("x", "jabber:x:conference",
				new GroupChatInvitation.Provider());
		// Service Discovery # Items //解析房间列表
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#items",
				new DiscoverItemsProvider());
		// Service Discovery # Info //某一个房间的信息
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",
				new DiscoverInfoProvider());
		// Data Forms
		pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());
		// MUC User
		pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user",
				new MUCUserProvider());
		// MUC Admin
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin",
				new MUCAdminProvider());
		// MUC Owner
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner",
				new MUCOwnerProvider());
		// Delayed Delivery
		pm.addExtensionProvider("x", "jabber:x:delay",
				new DelayInformationProvider());
		// Version
		try {
			pm.addIQProvider("query", "jabber:iq:version",
					Class.forName("org.jivesoftware.smackx.packet.Version"));
		} catch (ClassNotFoundException e) {
			// Not sure what's happening here.
		}
		// VCard
		pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());
		// Offline Message Requests
		pm.addIQProvider("offline", "http://jabber.org/protocol/offline",
				new OfflineMessageRequest.Provider());
		// Offline Message Indicator
		pm.addExtensionProvider("offline",
				"http://jabber.org/protocol/offline",
				new OfflineMessageInfo.Provider());
		// Last Activity
		pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());
		// User Search
		pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());
		// SharedGroupsInfo
		pm.addIQProvider("sharedgroup",
				"http://www.jivesoftware.org/protocol/sharedgroup",
				new SharedGroupsInfo.Provider());
		// JEP-33: Extended Stanza Addressing
		pm.addExtensionProvider("addresses",
				"http://jabber.org/protocol/address",
				new MultipleAddressesProvider());
		pm.addIQProvider("si", "http://jabber.org/protocol/si",
				new StreamInitiationProvider());
		pm.addIQProvider("query", "http://jabber.org/protocol/bytestreams",
				new BytestreamsProvider());
		pm.addIQProvider("query", "jabber:iq:privacy", new PrivacyProvider());
		pm.addIQProvider("command", "http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider());
		pm.addExtensionProvider("malformed-action",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.MalformedActionError());
		pm.addExtensionProvider("bad-locale",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadLocaleError());
		pm.addExtensionProvider("bad-payload",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadPayloadError());
		pm.addExtensionProvider("bad-sessionid",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadSessionIDError());
		pm.addExtensionProvider("session-expired",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.SessionExpiredError());

	}
}

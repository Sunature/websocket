package com.sunx.ws.jetty;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;

import com.sunx.util.JsonUtil;
import com.sunx.util.external.StringUtil;
import com.sunx.util.log.LogLev;
import com.sunx.util.log.LogUtil;
import com.sunx.ws.api.WebSocketHandle;
import com.sunx.ws.api.WebSocketTemplate;
import com.sunx.ws.common.Constaint;
import com.sunx.ws.common.Constaint.MsgType;
import com.sunx.ws.model.MsgFrom;
import com.sunx.ws.model.MsgTo;

/**
 * when create connection , this object is an singleton
 * 
 * @author Sun
 * 
 */
public class Jwslistner implements WebSocketListener {

	public static Map<String, Session> MAP = new HashMap<String, Session>();
	// $version < 1.2.2 ; since 1.2.3: alone thread pool to beat all clients
	// private ScheduledExecutorService s = Executors
	// .newSingleThreadScheduledExecutor();
	private String flag;

	public static Class<?> clazz = null;

	public static void setHandleClass(String whname) {
		if (StringUtil.isEmpty(whname))
			return;
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			if (loader != null)
				clazz = (Class<?>) Class.forName(whname, false, loader);
			else
				clazz = (Class<?>) Class.forName(whname);
			if (!WebSocketHandle.class.isAssignableFrom(clazz))
				throw new ClassNotFoundException(
						"handlerClass should extends class : WebSocketHandle");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			LogUtil.LOG("WebSocketHandleClass: " + whname + " load error : "
					+ e.getMessage(), LogLev.ERROR, Jwslistner.class);
		}
	}

	private WebSocketHandle wh = null;

	public WebSocketHandle getWh() {
		if (this.wh != null)
			return this.wh;
		if (clazz == null)
			return this.wh = new WebSocketTemplate();
		try {
			return this.wh = (WebSocketHandle) clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return this.wh = new WebSocketTemplate();
	}

	@Override
	public void onWebSocketBinary(byte[] payload, int offset, int len) {
		if (this.getWh() != null)
			this.getWh().onBinary(payload, offset, len, flag, null);
	}

	@Override
	public void onWebSocketClose(int statusCode, String reason) {
		MAP.remove(this.flag);
		// s.shutdown();// $version < 1.2.3
		this.getWh().onClose(statusCode, reason, flag);
	}

	@Override
	public void onWebSocketConnect(final Session session) {
		MAP.put(this.flag = this.getConnectFlag(session), session);
		MsgTo to = new MsgTo(MsgType.connect, "", this.flag, "200",
				System.currentTimeMillis(), null);
		send(session, JsonUtil.toJsonStr(to));

		// $version < 1.2.2 ;
		// s.scheduleWithFixedDelay(new Runnable() {
		// {
		//
		// Jwslistner.send(session, "200");
		// }
		//
		// @Override
		// public void run() {
		// Jwslistner.send(session, "heartbeat");
		// }
		// }, 30, 30, TimeUnit.SECONDS);

		this.getWh().onConnect(session, flag);
	}

	@Override
	public void onWebSocketError(Throwable error) {
		error.printStackTrace();
		this.getWh().onError(error, flag);
	}

	@Override
	public void onWebSocketText(String msg) {
		if (msg == null)
			return;
		MsgFrom mf = null;
		try {
			mf = JsonUtil.toObject(msg, MsgFrom.class);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.LOG(
					"onWebSocketText msg json-to-object error: "
							+ e.getMessage(), LogLev.ERROR, Jwslistner.class);
			return;
		}
		mf.setFrom(this.flag);
		mf.setTime(System.currentTimeMillis());
		boolean r = this.getWh().onText(mf);
		if (r)
			this.wh.send(mf);
		// message receipt
		MsgTo mt = new MsgTo(MsgType.msg, this.flag, this.flag, null,
				System.currentTimeMillis(), mf.getMsgid());
		this.wh.send(mt);
	}

	public static boolean send(Session session, String msg) {
		if (session == null)
			return false;
		try {
			if (session.isOpen())
				session.getRemote().sendString(msg);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private String getConnectFlag(final Session session) {
		List<String> flags = session.getUpgradeRequest().getParameterMap()
				.get(Constaint.FLAG);
		return flags != null && !flags.isEmpty() ? flags.get(0) : String
				.valueOf(session.hashCode());
	}

}

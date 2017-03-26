package com.sunx.ws.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.sunx.util.JsonUtil;
import com.sunx.util.external.StringUtil;
import com.sunx.util.log.LogLev;
import com.sunx.util.log.LogUtil;
import com.sunx.ws.common.Constaint;
import com.sunx.ws.common.Constaint.MsgType;
import com.sunx.ws.jetty.Jwservlet;
import com.sunx.ws.jetty.Jwslistner;
import com.sunx.ws.model.MsgFrom;
import com.sunx.ws.model.MsgTo;

public class WebSocketServer implements WebSocketService {

	private static WebSocketServer WS = new WebSocketServer();
	private static ExecutorService HEART_BEAT_SERVICE;

	/**
	 * send msg to all online clients
	 * 
	 * @param type
	 * @param from
	 * @param msg
	 * @param time
	 * @param msgId
	 * @return
	 */
	public List<String> sendToAll(MsgType type, String from, String msg,
			long time, String msgId) {
		List<String> rs = new ArrayList<String>();
		Iterator<String> keys = Jwslistner.MAP.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			MsgTo mt = new MsgTo(type, from, key, msg, time, msgId);
			if (this.send(mt))
				rs.add(key);
		}
		return rs;
	}

	/**
	 * return the to list those has send success
	 */
	@Override
	public List<String> send(MsgFrom mf) {
		if (!this.validate(mf))
			return null;
		if (mf.isToall())
			return this.sendToAll(mf.getType(), mf.getFrom(), mf.getMsg(),
					mf.getTime(), mf.getMsgid());
		if (mf.getTo() == null || mf.getTo().length < 1)
			return null;
		List<String> rs = new ArrayList<String>();
		for (String to : mf.getTo()) {
			if (to.equals(mf.getFrom()))
				continue;
			if (this.send(new MsgTo(mf.getType(), mf.getFrom(), to,
					mf.getMsg(), mf.getTime(), mf.getMsgid())))
				rs.add(to);
		}
		return rs;
	}

	@Override
	public boolean send(MsgTo mt) {
		return Jwslistner.send(Jwslistner.MAP.get(mt.getTo()),
				JsonUtil.toJsonStr(mt));
	}

	/**
	 * return current session counts
	 */
	@Override
	public long getOnLines() {
		return Jwslistner.MAP.keySet().size();
	}

	/**
	 * return boolean
	 * 
	 * @param flag
	 * @return
	 */
	@Override
	public boolean isOnLine(String flag) {
		return Jwslistner.MAP.containsKey(flag);
	}

	/**
	 * check params
	 * 
	 * @param mf
	 * @return
	 */
	private boolean validate(MsgFrom mf) {
		if (StringUtil.isEmpty(mf.getFrom()) || mf.getMsg() == null
				|| StringUtil.isEmpty(mf.getMsgid()))
			return false;
		if (!mf.isToall() && mf.getTo() == null)
			return false;
		return true;
	}

	/**
	 * keep alive
	 */
	public void heartBeats() {
		Iterator<String> keys = Jwslistner.MAP.keySet().iterator();
		long time = System.currentTimeMillis();
		while (keys.hasNext()) {
			MsgTo mt = new MsgTo(MsgType.heartBeat, null, keys.next(), null,
					time, null);
			HEART_BEAT_SERVICE.execute(new HeartBeatTask(WS, mt));
		}
	}

	/**
	 * initial pool size
	 * 
	 * @param size
	 */
	public static void initHeartBeatThreadPool(int size) {
		HEART_BEAT_SERVICE = Executors.newFixedThreadPool(size);
	}

	private class HeartBeatTask implements Runnable {
		private WebSocketServer ws;
		private MsgTo mt;

		public HeartBeatTask(WebSocketServer ws, MsgTo mt) {
			this.ws = ws;
			this.mt = mt;
		}

		public void run() {
			ws.send(mt);
		}
	}

	/**
	 * start server
	 * 
	 * @param p
	 */
	public static void startWebSocket(Properties p) {
		startWebSocket(
				Integer.parseInt(p.getProperty(Constaint.WEBSOCKET_PORT)),
				p.getProperty(Constaint.WEBSOCKET_PATH),
				p.getProperty(Constaint.HANDLER_CLASS));
	}

	/**
	 * start server
	 * 
	 * @param port
	 * @param path
	 * @param handlerClass
	 */
	public static void startWebSocket(int port, String path, String handlerClass) {
		try {
			Server server = new Server(port);
			HandlerList handlerList = new HandlerList();
			ServletContextHandler context = new ServletContextHandler(
					ServletContextHandler.SESSIONS);
			context.setContextPath("/");
			context.addServlet(new ServletHolder(new Jwservlet(handlerClass)),
					path);
			handlerList.addHandler(context);
			handlerList.addHandler(new DefaultHandler());
			server.setHandler(handlerList);
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.LOG("start websocket server error:" + e.getMessage(),
					LogLev.ERROR, WebSocketServer.class);
			System.exit(1);
		}
	}

}

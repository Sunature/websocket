package com.sunx.ws.api;

import org.eclipse.jetty.websocket.api.Session;

import com.sunx.ws.model.MsgFrom;

public abstract class WebSocketHandle extends WebSocketServer {

	public abstract boolean onBinary(byte[] payload, int offset, int len,
			String from, String[] to);

	public abstract void onClose(int statusCode, String reason, String from);

	public abstract void onConnect(final Session session, String from);

	public abstract void onError(Throwable error, String from);

	public abstract boolean onText(MsgFrom wm);

}

package com.sunx.ws.api;

import org.eclipse.jetty.websocket.api.Session;

import com.sunx.ws.model.MsgFrom;

public final class WebSocketTemplate extends WebSocketHandle {

	@Override
	public boolean onBinary(byte[] payload, int offset, int len, String from,
			String[] to) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onClose(int statusCode, String reason, String from) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnect(Session session, String from) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(Throwable error, String from) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onText(MsgFrom wm) {
		return true;
	}

}

package com.sunx.ws.javaee7;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/ws")
public class Java7WsGlassFish {

	@OnMessage
	public void onMessage(String msg, Session session) throws IOException {
		System.out.println("received : " + msg);
		this.sendMsg("replay...", session);
	}

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("client connect..." + session.getId());
	}

	@OnClose
	public void onClose() {
		System.out.println("connect close...");
	}

	public void sendMsg(String msg, Session session) throws IOException {
		session.getBasicRemote().sendText(msg);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

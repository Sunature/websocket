package com.sunx.ws.javaee7;

import java.io.IOException;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;

public class java7Ws extends Endpoint {

	@Override
	public void onOpen(Session session, EndpointConfig arg1) {
		final RemoteEndpoint.Basic remote = session.getBasicRemote();
		session.addMessageHandler(new MessageHandler.Whole<String>() {
			public void onMessage(String text) {
				try {
					remote.sendText(text.toUpperCase());
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		});
	}

	public void onClose(Session arg0, EndpointConfig arg1) {
		System.out.println("close...");
	}

	public void onError(Session arg0, EndpointConfig arg1) {
		System.out.println("error...");
	}

}

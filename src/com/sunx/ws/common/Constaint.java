package com.sunx.ws.common;

public class Constaint {
	public static final String FLAG = "wsflag";

	public enum MsgType {
		connect, heartBeat, system, msg
	}
	
	public static final String HANDLER_CLASS = "handlerClass";
	public static final String WEBSOCKET_PORT = "webSocketPort";
	public static final String WEBSOCKET_PATH = "webSocketPath";
	
}

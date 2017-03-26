package com.sunx.ws.test;

import org.eclipse.jetty.websocket.api.Session;

import com.sunx.util.log.LogLev;
import com.sunx.util.log.LogUtil;
import com.sunx.ws.api.WebSocketHandle;
import com.sunx.ws.model.MsgFrom;

public class MsgManager extends WebSocketHandle {

	public MsgManager() {
		LogUtil.LOG("load msgmanager success...", LogLev.INFO, MsgManager.class);
	}

	// private ***Manager ***Manager; //业务的manager

	// {
	//
	// this.sendMsgToOne(flag, msg) //向单个目标发送消息
	// this.sendMsgToSome(flags, msg) //向多个目标发送消息
	// this.sendMsgToAll(from, msg); //向所有目标发送消息，除了自身
	// }

	@Override
	public void onClose(int arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		System.out.println(arg2 + ":closed !");
	}

	@Override
	public void onConnect(Session arg0, String arg1) {
		// TODO Auto-generated method stub
		System.out.println(arg1 + ":connected !");
	}

	@Override
	public void onError(Throwable arg0, String arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onBinary(byte[] payload, int offset, int len,
			String fromFlag, String[] toFlag) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean onText(MsgFrom wm) {
		// TODO Auto-generated method stub
		return true;
	}

}

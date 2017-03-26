package com.sunx.ws.model;

import com.sunx.ws.common.Constaint.MsgType;

public class MsgTo extends Msg {
	private String to;

	public MsgTo(MsgType type, String from, String to, String msg, long time,
			String msgid) {
		this.from = from;
		this.to = to;
		this.msg = msg;
		this.time = time;
		this.msgid = msgid;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

}

package com.sunx.ws.test;

import com.sunx.util.JsonUtil;
import com.sunx.ws.common.Constaint.MsgType;
import com.sunx.ws.model.MsgFrom;

public class Test {

	public static void main(String[] asd) {
		MsgFrom mf = new MsgFrom();
		mf.setFrom("");
		mf.setMsg("");
		mf.setMsgid("");
		mf.setTime(1111l);
		mf.setType(MsgType.heartBeat);
		mf.setTo(new String[] { "sdsadas", "fasdsad" });
		System.out.println(JsonUtil.toJsonStr(mf));
	}

}

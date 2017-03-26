package com.sunx.ws.model;

public class MsgFrom extends Msg {

	private String[] to;
	private boolean toall = false;

	public String[] getTo() {
		return to;
	}

	public void setTo(String[] to) {
		this.to = to;
	}

	public boolean isToall() {
		return toall;
	}

	public void setToall(boolean toall) {
		this.toall = toall;
	}

}

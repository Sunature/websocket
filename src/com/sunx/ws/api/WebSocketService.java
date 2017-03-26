package com.sunx.ws.api;

import java.util.List;

import com.sunx.ws.model.MsgFrom;
import com.sunx.ws.model.MsgTo;

public interface WebSocketService {

	public List<String> send(MsgFrom mf);

	public boolean send(MsgTo mt);

	public long getOnLines();

	public boolean isOnLine(String flag);

}

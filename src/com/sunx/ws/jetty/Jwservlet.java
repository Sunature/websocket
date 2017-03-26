package com.sunx.ws.jetty;

import javax.servlet.ServletException;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class Jwservlet extends WebSocketServlet {

	private static final long serialVersionUID = 3135875261422669863L;

	public Jwservlet() {
	}

	public Jwservlet(String handleClass) {
		Jwslistner.setHandleClass(handleClass);
	}

	@Override
	public void configure(WebSocketServletFactory factory) {
		factory.register(Jwslistner.class);
	}

	public void init() throws ServletException {
		super.init();
		if (this.getInitParameter("handler-class") != null)
			Jwslistner.setHandleClass(this.getInitParameter("handler-class"));
	}

}

package com.sunx.ws.test;

import java.util.Properties;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import com.sunx.util.JsonUtil;
import com.sunx.util.PropertyUtil;
import com.sunx.ws.jetty.Jwservlet;

public class Jetty9Ws {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Properties p = PropertyUtil.loadProperties("/ws.properties");
		System.out.println(JsonUtil.toJsonStr(p));
		Server server = new Server(Integer.parseInt(p
				.getProperty("webSocketPort")));
		try {
			HandlerList handlerList = new HandlerList();

			/* websocket */
			ServletContextHandler context = new ServletContextHandler(
					ServletContextHandler.SESSIONS);
			context.setContextPath("/");
			context.addServlet(
					new ServletHolder(new Jwservlet(p
							.getProperty("handlerClass"))), p
							.getProperty("webSocketPath"));

			/* webapp */
			WebAppContext c = new WebAppContext();
			c.setContextPath("/w");
			c.setDescriptor("../websockets/WebRoot/WEB-INF/web.xml");
			c.setResourceBase("../websockets/WebRoot");

			handlerList.addHandler(c);
			handlerList.addHandler(context);
			handlerList.addHandler(new DefaultHandler());
			server.setHandler(handlerList);
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

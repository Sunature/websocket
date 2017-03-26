package com.sunx.ws.listener;

import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.sunx.util.PropertyUtil;
import com.sunx.util.external.StringUtil;
import com.sunx.ws.api.WebSocketServer;
import com.sunx.ws.common.Constaint;

public class WebsocketListener implements ServletContextListener {

	private ScheduledExecutorService s;

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// 载入配置文件
		final Properties p = PropertyUtil.loadProperties("/ws.properties");
		// context-param 优先级高
		String handleClass = arg0.getServletContext().getInitParameter(
				Constaint.HANDLER_CLASS);
		if (!StringUtil.isEmpty(handleClass))
			p.setProperty(Constaint.HANDLER_CLASS, handleClass);
		// 启动服务
		new Thread(new Runnable() {
			public void run() {
				WebSocketServer.startWebSocket(p);
			}
		}).start();
		// keep alive
		String sizeStr = p.getProperty("webSocketHbSize");
		Integer size = StringUtil.isNumeric(sizeStr) ? Integer
				.parseInt(sizeStr) : 1;
		// 初始化心跳线程池 - 默认单线程
		WebSocketServer.initHeartBeatThreadPool(size);
		// 调度周期 - 默认30s
		String intervalStr = p.getProperty("webSocketHbInterval");
		Integer interval = StringUtil.isNumeric(intervalStr) ? Integer
				.parseInt(intervalStr) : 30;
		s = Executors.newSingleThreadScheduledExecutor();
		s.scheduleWithFixedDelay(new Runnable() {
			public void run() {
				new WebSocketServer().heartBeats();
			}
		}, interval, interval, TimeUnit.SECONDS);
	}

}

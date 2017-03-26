/*******************************************************************************
 * * *
 * 
 * @Sun * websocket 1.2.3 * param : * url : 127.0.0.1:8181/ws * uuid : flag *
 *      onmsg : function || function-name * onback : function * ontimeout :
 *      function * timeout : default 6000 min 1000 * send.param : msg , to , is
 *      to all *
 ******************************************************************************/
;
$.ws = function(p) {
	this.flag = p.uuid;
	this.timeout = p.timeout || 6000;
	this.list = {};
	this.ontimeout = p.ontimeout;
	this.connect = new WebSocket(p.url + "?wsflag=" + this.flag);
	$(this.connect).bind("open", function(event) {
	}).bind("message", function(event) {
		var data = event.data || event.originalEvent.data;
		data = JSON.parse(data);
		switch (data.type) {
		case 0:
			// 连接成功
			break;
		case 1:
			// 心跳
			break;
		case 2:
			// 系统消息
			break;
		case 3:
			// 收到消息
			if (data.from != $.ws.flag) {
				if (typeof (p.onmsg) === "string")
					eval(p.onmsg)(data);
				else
					p.onmsg(data);
			} else {
				delete $.ws.list[data.msgid];
				if (p.onback)
					p.onback(data);
			}
			break;
		}
	}).bind("error", function(event) {
	}).bind("close", function(event) {
		window.opener = null;
		window.close();
	});
	this.send = function(msg, to, istoall) {
		var time = new Date().getTime();
		var idpart = [ this.flag, time, Math.ceil(Math.random() * 1000) ];
		var mf = {
			from : this.flag,
			msg : msg,
			to : typeof (to) === "string" ? to.split(",") : to,
			istoall : istoall || false,
			msgid : idpart.join("-")
		};
		this.connect.send(JSON.stringify(mf));
		if (this.ontimeout)
			this.list[mf.msgid] = time;
		mf.time = time;
		return mf;
	};
	this.timeout = this.timeout < 1000 ? 1000 : this.timeout;
	if (this.ontimeout)
		window.setInterval(function() {
			$.wscheck();
		}, 1000);
	return $.ws = this;
};
$.wscheck = function() {
	var now = new Date().getTime();
	for ( var p in $.ws.list) {
		var d = now - Number($.ws.list[p]);
		if (d >= $.ws.timeout) {
			$.ws.ontimeout(p);
			delete $.ws.list[p];
		}
	}
};

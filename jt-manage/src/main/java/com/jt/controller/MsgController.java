package com.jt.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MsgController {
		/**
		 * 动态获取当前服务器的端口号
		 */
	@Value("${server.port}")
	private int port;
	//主要获取访问当前服务器的端口号信息
	@RequestMapping("/getPort")
	public String getMsg() {
		return "您当前访问的服务器端口号为:"+port;
	}
}

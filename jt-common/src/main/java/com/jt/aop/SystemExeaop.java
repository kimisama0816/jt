package com.jt.aop;

import java.io.IOException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jt.vo.SysResult;

import lombok.extern.slf4j.Slf4j;

//标识该类是全局异常处理机制的配置类
@RestControllerAdvice  //advice  通知  返回的数据都是json串
@Slf4j				   //
public class SystemExeaop {
	/**
	 * 添加通用异常返回的方法.
	 * 底层原理: aop的异常通知,
	 * 
	 */
	@ExceptionHandler(RuntimeException.class)//拦截运行时的异常
	public Object SystemResultExe(Exception exception) {
		exception.printStackTrace(); // 如果有问题,则直接在控制台打印
		log.error("{~~~~"+exception.getMessage()+"}", exception);  //输出日志
		return SysResult.fail();	 // 返回统一的失败数据
		
		
	}
	
}

package com.jt.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperUtil {
	//json与对象的转化 优化异常处理
	private static final ObjectMapper MAPPER= new ObjectMapper();
	
	//1.将对象转化为json
	public static String toJSON(Object target) {
		
		
		
		if(target == null) {
			throw new NullPointerException("target数据为空");
		}
		try {
			return MAPPER.writeValueAsString(target);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

		//2.将json串转化为对象  用户传递什么样的类型,就返回什么样的对象
	//<T>定义了一个泛型对象  代表任意类型
	public static <T> T toObject(String json,Class<T> targetClass){
		if(StringUtils.isEmpty(json) || targetClass == null) {
			throw new NullPointerException("参数不能为空");
		}
		try {
			return MAPPER.readValue(json, targetClass);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
		
		
		
		
		
		
		
		
		
		
		
	
}

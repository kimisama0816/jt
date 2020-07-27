package com.jt.aop;

public class Test {
	/**
	 * 测试 获取的串如何根据符号进行分割注入 避免解析报错
	 * @param args
	 */
	public static void main(String[] args) {
		String s="125422:ddd,255214:dd";
		String[] split = s.split(",");
		for (String string : split) {
//			System.out.println(string);
			String[] split2 = string.split(":");
			System.out.println(split2[0]);
			System.out.println(split2[1]);
		}
	}
}

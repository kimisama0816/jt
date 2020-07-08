package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class EasyUITree {

	/*
	 * "id":"1", "text":"英雄联盟", "iconCls":"icon-save", "children":[
	 */
	private Long id;		//id值     			与itemcat中的id一致
	private String text;	//文本信息		与itemcat中的name属性一致
	private String state;	//状态			打开:open 关闭:closed
	
}

package com.jt.service;

import com.jt.pojo.Item;
import com.jt.vo.EasyUITable;

public interface ItemService {

	EasyUITable findItemByPage(Integer page, Integer rows);

	 void saveItem(Item item);

	 void updateItem(Item item);
	 //返回的是调用的long数组和ids  并不是item
	void deleteItem(Long[] ids);
	
}

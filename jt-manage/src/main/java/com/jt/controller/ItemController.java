package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.pojo.Item;
import com.jt.service.ItemService;
import com.jt.vo.EasyUITable;
import com.jt.vo.SysResult;

@RestController	//返回数据时不需要经过视图解析器.
@RequestMapping("/item")
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	/**
	 * 业务: 展现商品列表数据,以EasyUI表格数据展现
	 * url地址: http://localhost:8091/item/query?page=1&rows=20
	 * 参数:	page=1 页数  &  rows=20 没有记录数
	 * 返回值: EasyUITable VO对象
	 */
	@RequestMapping("/query")
	public EasyUITable findItemByPage(Integer page,Integer rows) {
		
		return itemService.findItemByPage(page,rows);
	}
	
	/**
	 * 商品新增操作
	 * url: /item/save
	 * 参数: form表单数据
	 * 返回值结果SYSresult对象
	 */
	
	@RequestMapping("/save")
	public SysResult saveItem(Item item) {
		/*
		 * try { itemService.saveItem(item); return SysResult.success(); } catch
		 * (Exception e) { e.printStackTrace();//打印错误信息 return SysResult.fail(); }
		 */
		/* int a= 1/0; */
		itemService.saveItem(item);
		return SysResult.success();}
	//定义全局异常处理机制	
		/**
		 * 商品修改操作
		 * url: /item/update
		 * 参数: form表单数据
		 * 返回值结果SYSresult对象
		 */
		
		@RequestMapping("/update")
		public SysResult updateItem(Item item) {
			/*
			 * try { itemService.saveItem(item); return SysResult.success(); } catch
			 * (Exception e) { e.printStackTrace();//打印错误信息 return SysResult.fail(); }
			 */
			/* int a= 1/0; */
			itemService.updateItem(item);
			return SysResult.success();}
			/**
			 * 商品删除操作
			 * url: /item/delete
			 * 参数: form表单数据
			 * 返回值结果SYSresult对象
			 */
		@RequestMapping("/delete")
		//将前端的获取的商品编号  组成一个long类型的数组进行储存
		public SysResult deleteItem(Long[] ids) {
		//调用ids属性 关联service层的删除方法
			itemService.deleteItem(ids);
			return SysResult.success();
		}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

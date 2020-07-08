package com.jt.service;

import java.sql.Array;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.xml.crypto.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jt.mapper.ItemMapper;
import com.jt.pojo.Item;
import com.jt.vo.EasyUITable;

@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private ItemMapper itemMapper;

	
	/**
	 * 开发方式:
	 * 	1.自下而上的方式     自己写页面的开发方式  自己写页面的JS   
	 * 	2.自上而下的方式     通过接口的方式进行的开发.
	 * 分页sql:  select xxxxx,xxxx from tb_item limit 起始位置,查询记录数
	 * 查询第一页: 规则:含头不尾特点
	 * 			select xxxxx,xxxx from tb_item limit 0,20    0-19
	 * 			select xxxxx,xxxx from tb_item limit 20,20   20-39
	 * 			select xxxxx,xxxx from tb_item limit 40,20   40-59
	 * 等差数列第N项:   (page-1)*rows
	 */
	/*
	 * @Override public EasyUITable findItemByPage(Integer page, Integer rows) {
	 * //1.获取记录总数 int total = itemMapper.selectCount(null);
	 * 
	 * //2.查询分页后的结果 int start = (page-1) * rows; List<Item> itemList =
	 * itemMapper.selectItemByPage(start,rows);
	 * 
	 * return new EasyUITable(total, itemList); }
	 */
	
	
	/**
	 * 利用MP的方式查询数据记录
	 * current:查询第几页  
	 * size: 每页记录数
	 */
	@Override
	public EasyUITable findItemByPage(Integer page, Integer rows) {
		//1.定义分页对象
		Page<Item> mpPage = new Page<>(page, rows);
		//2.定义条件构造器
		QueryWrapper<Item> queryWrapper = new QueryWrapper<Item>();
		//3.要求:按照更新时间,进行排序  降序排列.
		queryWrapper.orderByDesc("updated");
		//执行分页操作,之后封装Page对象数据
		mpPage = itemMapper.selectPage(mpPage, queryWrapper);
		int total = (int) mpPage.getTotal();	//获取总记录数
		List<Item> items = mpPage.getRecords(); //获取当前分页记录
		return new EasyUITable(total, items);
	}
	//控制数据库事务

	@Transactional
	@Override
	public void saveItem(Item item) {
		item.setStatus(1)
			.setCreated(new Date())
			.setUpdated(item.getCreated());
		itemMapper.insert(item);
	
	//可能有其他的首尾工作(集中抛出异常)
	}

	@Override
	public void updateItem(Item item) {
		
		item.setUpdated(new Date());
		itemMapper.updateById(item);
		
	}

	@Override
	public void deleteItem(Long[] ids) {
		/*
		 * Arrays.asList()方法是将数组转化为list，有以下几点需要注意：
		 * 
		 * （1）该方法不适用于基本数据类型（byte,short,int,long,float,double,boolean）
		 * 
		 * （2）该方法将数组与列表链接起来，当更新其中之一时，另一个自动更新
		 * 
		 * （3）不支持add和remove方法
		 */
		//将long数字转化成list 然后关联mapper层的调用方法 修改数据库的数据
		List<Long> idList= Arrays.asList(ids);
		itemMapper.deleteBatchIds(idList);
		
	}

	
	
}
	
	
	
	


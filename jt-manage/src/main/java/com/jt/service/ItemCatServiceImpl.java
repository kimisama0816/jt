package com.jt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.anno.CacheFind;
import com.jt.mapper.ItemCatMapper;
import com.jt.pojo.ItemCat;
import com.jt.utils.ObjectMapperUtil;
import com.jt.vo.EasyUITree;

import redis.clients.jedis.Jedis;
@Service
public class ItemCatServiceImpl implements ItemCatService {
	
	@Autowired
	private ItemCatMapper itemCatMapper;
	@Autowired(required = false)
	private Jedis jedis;

	@Override
	@CacheFind(key="ITEM_CAT_ID")
	public ItemCat findItemCatById(Long itemCatId) {
		/*
		 * QueryWrapper<ItemCat> queryWrapper = new QueryWrapper<ItemCat>();
		 * queryWrapper.select("name","id"); itemCatMapper.selectMaps(queryWrapper);
		 */
		return itemCatMapper.selectById(itemCatId);
	}

	/**
	 * 数据的来源: 数据库中
	 * 数据库中的数据类型:  ItemCat对象信息   POJO
	 * 需要的类型:        EasyUITree对象信息.  VO对象
	 * 思路: 将ItemCat转化为EasyUITree对象.
	 * sql: parent_id=0 SELECT * FROM tb_item_cat WHERE parent_id=0
	 */
	@Override
	@CacheFind(key="ITEM_CAT_LIST")
	public List<EasyUITree> findItemCatByParentId(Long parentId) {
		//1.根据parentId 查询数据库信息  根据父级查询子级信息.
		QueryWrapper<ItemCat> queryWrapper = new QueryWrapper();
		queryWrapper.eq("parent_id", parentId);
		List<ItemCat> itemCatList = itemCatMapper.selectList(queryWrapper);
		
		//2.将数据库记录转化为VO数据.
		List<EasyUITree> treeList = new ArrayList<EasyUITree>();
		for (ItemCat itemCat : itemCatList) {
			Long id = itemCat.getId();
			String text = itemCat.getName();
			//如果是父级标题,则默认关闭 closed,否则开启. open
			String state = itemCat.getIsParent()?"closed":"open"; 
			EasyUITree uiTree = new EasyUITree(id, text, state);
			treeList.add(uiTree);
		}
		
		return treeList;
	}
	
	/**
	 * 通过缓存的方式查询数据库.
	 * 1).定义key
	 * 2).根据key查询redis.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<EasyUITree> findItemCatByCache(Long parentId){
		
		//1.定义key
		String key = "ITEM_CAT_LIST::"+parentId;
		
		List<EasyUITree> treelist = new ArrayList<EasyUITree>();
		
		Long startTime = System.currentTimeMillis();
		//2.判断redis中是否有值
		if(jedis.exists(key)){
			//不是第一次查询,则获取缓存中的数据并返回
			String json=jedis.get(key);
			Long endTime=System.currentTimeMillis();
			treelist = ObjectMapperUtil.toObject(json, treelist.getClass());
			System.out.println("redis查询缓存的时间为:"+(endTime-startTime)+"毫秒");
		}else {
			//redis中没有这个key 表示用户第一次查询
			treelist = findItemCatByParentId(parentId);
			Long endTime=System.currentTimeMillis();
			//需要将list集合转化为json
			String json = ObjectMapperUtil.toJSON(treelist);
			//将数据保存到redis中
			jedis.set(key, json);
			System.out.println("查询数据库的时间为:"+(endTime-startTime)+"毫秒");
			
		}
		return treelist;
	}
	
	
	
	
	
	
}

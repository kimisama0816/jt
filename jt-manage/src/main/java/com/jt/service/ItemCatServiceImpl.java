package com.jt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.ItemCatMapper;
import com.jt.pojo.ItemCat;
import com.jt.vo.EasyUITree;

@Service
public class ItemCatServiceImpl implements ItemCatService {
	
	@Autowired
	private ItemCatMapper itemCatMapper;

	@Override
	public ItemCat findItemCatById(Long itemCatId) {
		/*
		 * QueryWrapper<ItemCat> queryWrapper = new QueryWrapper<ItemCat>();
		 * queryWrapper.select("name","id"); itemCatMapper.selectMaps(queryWrapper);
		 */
		return itemCatMapper.selectById(itemCatId);
		
		/**
		 * 数据的来源:数据库中
		 * 数据库中的数据类型: itemcat对象信息 pojo
		 * 需要的类型:  		eastuitree对象信息  vo对象
		 * 思路 : 将itemcat转换为easyuitree对象
		 */
	
	}

	@Override
	public List<EasyUITree> findItemCatByParentId(Long parentId) {
		
			//1.根据parent_id 查询数据库信息  根据父级查询子级信息
			QueryWrapper<ItemCat> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("parent_id", parentId);
			List<ItemCat> itemCatList = itemCatMapper.selectList(queryWrapper);
			//2.将数据库记录转化为vo数据
			List<EasyUITree> treeList = new ArrayList<EasyUITree>();
			for (ItemCat itemCat:itemCatList) {
				Long id= itemCat.getId();
				String text=itemCat.getName();
				//如果是父级标题 则默认关闭 closed,否则开启 open
				String state = itemCat.getIsParent()?"closed":"open";
				EasyUITree uiTree = new EasyUITree(id,text,state);
				treeList.add(uiTree);
			}
			
			return treeList;
		}
	}


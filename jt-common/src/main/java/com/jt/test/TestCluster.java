package com.jt.test;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

/**
 * 
 * 通过程序操作redis
 * @author 000
 * 3主机 负责读写 3从机  负责数据备份 发生故障就通过自行投票机制主从替换 
 */

public class TestCluster {
	@Test
	public void test01() {
		
		Set<HostAndPort> nodes = new HashSet<HostAndPort>();
		
		nodes.add(new HostAndPort("192.168.126.129", 7000));
		nodes.add(new HostAndPort("192.168.126.129", 7001));
		nodes.add(new HostAndPort("192.168.126.129", 7002));
		nodes.add(new HostAndPort("192.168.126.129", 7003));
		nodes.add(new HostAndPort("192.168.126.129", 7004));
		nodes.add(new HostAndPort("192.168.126.129", 7005));
		//利用程序操作redis集群
		
		JedisCluster jedisCluster = new JedisCluster(nodes);
		
		jedisCluster.set("asd", "redis集群测试");
		
		System.out.println(jedisCluster.get("asd"));
	}
}

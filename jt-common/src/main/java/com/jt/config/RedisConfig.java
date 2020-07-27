package com.jt.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

@Configuration
@PropertySource("classpath:/properties/redis.properties")
public class RedisConfig {
	
	/**
	 * spring整合redis集群
	 */
	@Value("${redis.nodes}")
	private String redisNodes;
	
	@Bean
	public JedisCluster jedisCluster() {
		//System.out.println(redisNodes+"dddddddddddddddd");
		
		Set<HostAndPort> nodeSet= new HashSet<HostAndPort>();
		//192.168.126.129:7000,192.168.126.129:7001,
		String[] clusters = redisNodes.split(",");
		
		for(String cluster : clusters) {
			
			String host =cluster.split(":")[0];
			
			int port = Integer.parseInt(cluster.split(":")[1]);
			
			nodeSet.add(new HostAndPort(host, port));
		}
		
		return new JedisCluster(nodeSet);
	}
	/*
	 * @Value("${redis.host}") private String host;
	 * 
	 * @Value("${redis.port}") private Integer port;
	 * 
	 * //将返回值的结果交给spring容器进行管理,如果以后想要再次使用该对象可以直接注入
	 * 
	 * @Bean public Jedis jedis() {
	 * 
	 * return new Jedis(host,port);
	 * 
	 * }
	 * 
	 * @Value("${redis.nodes}") private String redisNodes; //node node node
	 * 整合分片实现Redis内存扩容
	 * 
	 * @Bean public ShardedJedis shardedJedis() {
	 * 
	 * String[] nodes = redisNodes.split(","); //节点数组
	 * 
	 * //动态获取redis信息 List<JedisShardInfo> list = new ArrayList<JedisShardInfo>();
	 * 
	 * for(String node : nodes) { //node =host:port ------>[host,port]
	 * 
	 * String host = node.split(":")[0];
	 * 
	 * int port = Integer.parseInt(node.split(":")[1]);
	 * 
	 * list.add(new JedisShardInfo(host,port)); } //返回分片对象 return new
	 * ShardedJedis(list); }
	 */
}

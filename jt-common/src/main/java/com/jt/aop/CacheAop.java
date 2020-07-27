package com.jt.aop;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jt.anno.CacheFind;
import com.jt.utils.ObjectMapperUtil;

import redis.clients.jedis.JedisCluster;

//将对象交给容器管理
@Component
//定义aop切面
@Aspect
public class CacheAop {
		
		
	@Autowired(required = false)
	//private Jedis jedis;  单台redis注入
	//private ShardedJedis jedis; //分片jedis注入
	private JedisCluster jedis;   //集群注入,可以实现高可用
	/**
	 * 实现思路:  拦截被@CacheFind标识的方法 之后利用aop进行缓存的控制
	 * 通知方法:  环绕通知
	 * 实现步骤:
	 * 		1.准备查询redis的key   ITEM_CAT_LIST::第一个参数
	 *      2.@annotation(cacheFind) 动态获取注解的语法.
	 *        拦截指定注解类型的注解并且将注解对象当做参数进行传递.
	 */
	@SuppressWarnings("unchecked")
	@Around("@annotation(cacheFind)")
	public Object around(ProceedingJoinPoint joinPoint,CacheFind cacheFind) {
		
		//1.获取用户注解中的key  ITEM_CAT_LIST::0
		String key = cacheFind.key();
		//2.动态获取第一个参数当key
		//joinPoint.getArgs() 获取当前目标中的参数信息
		String firstArg = joinPoint.getArgs()[0].toString();
		
		key += "::"+firstArg;
		
		Object result = null;
		//3.根据key查询redis
		
		if(jedis.exists(key)) {
			
			//根据redis获取数据信息
			String json = jedis.get(key);
			//如何获取返回值类型
			MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
			
			result = ObjectMapperUtil.toObject(json,methodSignature.getReturnType());
			
			System.out.println("aop查询redis缓存");
		
		}else {
			//如果key不存在,则证明是第一次查询,应该查询数据库
			try {
				result = joinPoint.proceed();		//目标方法返回值
				System.out.println("aop查询数据库获取返回值结果");
				//将数据保存到redis中
				String json = ObjectMapperUtil.toJSON(result);
				
				int seconds = cacheFind.seconds();
				
				if(seconds > 0) 
					jedis.setex(key, seconds, json);
					else
						jedis.set(key,json);	
			} catch (Throwable e) {
				
				e.printStackTrace();
				
				throw new RuntimeException();
			}  
			
		}	
		return result;
	}
	
	
	/*
	 * //公式: 切面 = 切入点表达式 + 通知方法.
	 *//**
		 * 业务需求: 要求拦截ItemCatServiceImpl类中的业务
		 * 
		 * @Pointcut 切入点表达式 可以理解为就是一个if判断,只有满足条件,才能执行通知方法.
		 *//*
			 * //@Pointcut("bean(itemCatServiceImpl)") //按类匹配,控制的粒度较粗 单个bean
			 * //@Pointcut("within(com.jt.service..*)") //按类匹配,控制的粒度较粗 多个bean
			 * 
			 * @Pointcut("execution(* com.jt.service..*.*(..))")//细粒度的匹配方法 public void
			 * pointCut() {
			 * 
			 * }
			 * 
			 * //joinPoint 方法执行切入恰好被切入点表达式匹配,该方法的执行就被成为连接点
			 * 
			 * @Before("pointCut()") public void before(JoinPoint joinPoint) {
			 * 
			 * System.out.println("我是前置通知");
			 * 
			 * String typeName= joinPoint.getSignature().getDeclaringTypeName();
			 * 
			 * String methodName = joinPoint.getSignature().getName();
			 * 
			 * 
			 * Object[] objs =joinPoint.getArgs();
			 * 
			 * Object target = joinPoint.getTarget();
			 * 
			 * System.out.println("方法执行的全路径为:"+typeName+methodName);
			 * System.out.println("获取的方法参数"+objs); System.out.println("获取目标对象"+target);
			 * 
			 * }
			 * 
			 * 
			 * //添加环绕通知
			 * 
			 * @Around("pointCut()") public Object around(ProceedingJoinPoint joinPoint) {
			 * 
			 * System.out.println("我是环绕通知开始"); try { //object result = joinPoint.proceed();
			 * System.out.println("我是环绕通知结束"); return null;
			 * 
			 * } catch (Throwable e) { e.printStackTrace(); throw new RuntimeException(); //
			 * TODO: handle exception }
			 * 
			 * 
			 * }
			 */

}

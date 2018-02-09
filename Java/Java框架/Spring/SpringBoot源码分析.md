<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [1.SpringBoot注入问题,看如下代码:](#1springboot%E6%B3%A8%E5%85%A5%E9%97%AE%E9%A2%98%E7%9C%8B%E5%A6%82%E4%B8%8B%E4%BB%A3%E7%A0%81)
- [2.关于参数拦截:](#2%E5%85%B3%E4%BA%8E%E5%8F%82%E6%95%B0%E6%8B%A6%E6%88%AA)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

#### 1.SpringBoot注入问题,看如下代码:
	@Service
	public class RedisPoolFactory {
	    @Autowired
	    RedisConfig redisConfig;
	    @Bean
	    public JedisPool JedisPoolFactory(){
	        JedisPoolConfig config = new JedisPoolConfig();
	        config.setMaxIdle(redisConfig.getPoolMaxIdle());
	        config.setMaxTotal(redisConfig.getPoolMaxTotal());
	        config.setMaxWaitMillis(redisConfig.getPoolMaxWait());
	        JedisPool pool = new JedisPool(config, redisConfig.getHost(), redisConfig.getPort(),redisConfig.getTimeout(), redisConfig.getPassword(), 0);
	        return pool;
	    }
	}

	@Service
	public class RedisService {
	    @Autowired
	    private JedisPool jedisPool;
	    public <T> T get(String key, Class<T> clazz){
	        return null;
	    }
	}
	==> 为什么 RedisService 中能够注入 JedisPool
#### 2.关于参数拦截:
    WebMvcConfigurerAdapter, HandlerMethodArgumentResolver


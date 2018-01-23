*****************************************************SpringBoot 问题****************************************************
1.SpringBoot注入问题,看如下代码:
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
2.关于参数拦截:
WebMvcConfigurerAdapter, HandlerMethodArgumentResolver

************************************************************************************************************************
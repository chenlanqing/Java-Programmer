# 一、JPA

- [基于Java注解的JPA / SQL 语句生成器](https://gitee.com/Levin-Li/simple-dao)
- [JPA REDIS无缝整合精细化缓存](https://gitee.com/shujianhui/SpringJPARedis.git)

# 二、Redis

## 1、关于序列化

Spring 提供的 4 种 RedisSerializer（Redis 序列化器）：
- 默认情况下，RedisTemplate 使用 JdkSerializationRedisSerializer，也就是 JDK 序列化，容易产生 Redis 中保存了乱码的错觉；
- 通常考虑到易读性，可以设置 Key 的序列化器为 StringRedisSerializer。但直接使用 RedisSerializer.string()，相当于使用了 UTF_8 编码的 StringRedisSerializer，需要注意字符集问题；
- 如果希望 Value 也是使用 JSON 序列化的话，可以把 Value 序列化器设置为 Jackson2JsonRedisSerializer。默认情况下，不会把类型信息保存在 Value 中，即使我们定义 RedisTemplate 的 Value 泛型为实际类型，查询出的 Value 也只能是 LinkedHashMap 类型。如果希望直接获取真实的数据类型，你可以启用 Jackson ObjectMapper 的 activateDefaultTyping 方法，把类型信息一起序列化保存在 Value 中；
    ```java
    ...
    Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
    ObjectMapper objectMapper = new ObjectMapper();
    //把类型信息作为属性写入Value
    objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
    jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
    ...
    ```
- 如果希望 Value 以 JSON 保存并带上类型信息，更简单的方式是，直接使用 RedisSerializer.json() 快捷方法来获取序列化器
    ```java
    redisTemplate.setKeySerializer(RedisSerializer.string());
    redisTemplate.setValueSerializer(RedisSerializer.json());
    redisTemplate.setHashKeySerializer(RedisSerializer.string());
    redisTemplate.setHashValueSerializer(RedisSerializer.json());
    ```
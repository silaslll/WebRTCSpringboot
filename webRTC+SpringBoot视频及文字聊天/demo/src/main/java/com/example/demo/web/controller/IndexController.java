package com.example.demo.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/")
    public String index(){
        return "index";
    }

    /**
     * 清空redis
     */
    @RequestMapping("/clear_redis")
    @ResponseBody
    public Map clear_redis() {
        HashMap<Object, Object> map = new HashMap<>();
        redisTemplate.execute((RedisCallback) redisConnection -> {
            redisConnection.flushAll();
            return "";
        });
        map.put("msg", "清理缓存OK");
        map.put("code", 0);
        return map;
    }
}

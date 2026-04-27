package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author SXBai
 * @create 2026-04-23-17:18
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    public static final String WX_LOGIN="https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;

    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        String code = userLoginDTO.getCode();
        log.info("开始处理微信登录, code={}, appid='{}', appid长度={}, secret已配置={}",
                code,
                weChatProperties.getAppid(),
                weChatProperties.getAppid() == null ? 0 : weChatProperties.getAppid().length(),
                StringUtils.hasText(weChatProperties.getSecret()));

        String openid = getOpenid(code);

        //判断为空则抛出异常
        if(openid==null){
            log.error("微信登录失败, 未获取到openid, code={}", code);
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        //判断是否为新用户
        User user = userMapper.getById(openid);

        if(user==null){
           user = User.builder().
                   openid(openid).
                   createTime(LocalDateTime.now()).
                   build();
           userMapper.insert(user);
        }


        return user;
    }

    private String getOpenid(String code){
        Map<String,String> map = new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",code);
        map.put("grant_type","authorization_code");
        //调用接口服务，获得当前用户的openid
        String json = HttpClientUtil.doGet(WX_LOGIN,map);
        log.info("调用微信jscode2session返回, code={}, response={}", code, json);

        if (!StringUtils.hasText(json)) {
            log.error("调用微信jscode2session失败, 返回为空, code={}", code);
            return null;
        }

        JSONObject jsonObject = JSONObject.parseObject(json);
        if (jsonObject.containsKey("errcode")) {
            log.error("微信jscode2session返回错误, code={}, errcode={}, errmsg={}",
                    code,
                    jsonObject.getString("errcode"),
                    jsonObject.getString("errmsg"));
        }
        String openid = jsonObject.getString("openid");
        return openid;
    }

}

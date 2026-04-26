package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author SXBai
 * @create 2026-04-23-18:58
 */
@Mapper
public interface UserMapper {
    /**
     * 根据openid查询用户
     * @param openid
     * @return
     */
    @Select("select * from user where user.openid=#{openid}")
    User getById(String openid);

    /**
     * 插入数据
     * @param user
     */
    void insert(User user);
}

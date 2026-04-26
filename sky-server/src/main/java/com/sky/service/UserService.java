package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

/**
 * @author SXBai
 * @create 2026-04-22-1:53
 */

public interface UserService {
    User wxLogin(UserLoginDTO userLoginDTO);
}

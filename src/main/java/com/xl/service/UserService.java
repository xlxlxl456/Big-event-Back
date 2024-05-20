package com.xl.service;

import com.xl.pojo.User;

public interface UserService {
    User findByUserName(String username);

    void register(String username, String password);

    void update(User user);

    void updateAvatar(String avatar);

    void updatePwd(String newPwd);
}

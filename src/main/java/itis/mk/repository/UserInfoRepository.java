package itis.mk.repository;

import itis.mk.models.UserInfo;

public interface UserInfoRepository {
    void save(UserInfo user);
    UserInfo findByEmail(String email);
}

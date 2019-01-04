package com.bookexchange.app.model.dao;

import com.bookexchange.app.model.model.UserDO;
import org.apache.catalina.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
@Mapper
public interface UserMapper {
    @Insert("INSERT INTO Users(contact, openid) VALUES(#{contact}, #{openid})")
    void insertUser(UserDO user);

    @Update("UPDATE Users SET contact=#{contact}, name=#{name} WHERE uid=#{uid}")
    void updateInfoFromUserDO(UserDO user);

    @Select("SELECT uid AS uid, name AS name, contact AS contact from Users WHERE uid=#{uid}")
    UserDO selectUserInfoFromUid(int uid);

    @Select("SELECT contact from Users WHERE uid=#{uid}")
    String selectContactFromUid(int uid);

    @Select("SELECT name from Users WHERE uid=#{uid}")
    String selectNameFromUid(int uid);

    @Delete("DELETE FROM Users WHERE uid=#{uid}")
    void deleteUser(Integer uid);

//    @Select("SELECT uid from Users WHERE uid=#{uid}")
//    boolean selectIfUidExists(Integer uid);

    @Select("SELECT EXISTS(SELECT 1 FROM Users WHERE openid=#{openid})")
    boolean selectIfOpenidExists(String openid);

    @Select("SELECT uid from Users WHERE openid=#{openid}")
    Integer selectUidFromOpenid(String openid);

}

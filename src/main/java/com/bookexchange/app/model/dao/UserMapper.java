package com.bookexchange.app.model.dao;

import com.bookexchange.app.model.model.UserDO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {
    @Insert("INSERT INTO Users(contact, openid) VALUES(#{contact}, #{openid})")
    void insertUser(UserDO user);

    @Update("UPDATE Users SET contact=#{contact} WHERE uid=#{uid}")
    void updateContactFromUserDO(UserDO user);

    @Select("SELECT contact from Users WHERE uid=#{uid}")
    String selectContactFromUid(int uid);

    @Delete("DELETE FROM Users WHERE uid=#{uid}")
    void deleteUser(Integer uid);

//    @Select("SELECT uid from Users WHERE uid=#{uid}")
//    boolean selectIfUidExists(Integer uid);

    @Select("SELECT EXISTS(SELECT 1 FROM Users WHERE openid=#{openid})")
    boolean selectIfOpenidExists(String openid);

    @Select("SELECT uid from Users WHERE openid=#{openid}")
    Integer selectUidFromOpenid(String openid);

}

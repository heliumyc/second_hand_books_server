package com.bookexchange.app.model.dao;

import com.bookexchange.app.model.model.UserDO;
import com.bookexchange.app.utils.SearchHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Repository
public class UserDAO {

    private final UserMapper userMapper;

    private final BookMapper bookMapper;

    private final SearchHelper searchHelper;

    @Autowired
    public UserDAO(UserMapper userMapper, BookMapper bookMapper, SearchHelper searchHelper) {
        this.userMapper = userMapper;
        this.bookMapper = bookMapper;
        this.searchHelper = searchHelper;
    }

    /**
     * 获取用户uid
     * @param openid id from wx
     * @return uid
     */
    public Integer getUid(String openid) {
        return userMapper.selectUidFromOpenid(openid);
    }

    /**
     * 尝试注册, 原子操作, 不需要事务
     * @param openid id from wx
     */
    public void tryRegister(String openid) {
        if (!userMapper.selectIfOpenidExists(openid)) {
            UserDO user = new UserDO(openid, "未设置联系方式", "用户"+openid.substring(0,5));
            userMapper.insertUser(user);
        }
    }

    /**
     * 查询用户信息
     * @param uid user id
     * @return contact of user
     */
    public String getUserContact(int uid) {
        return userMapper.selectContactFromUid(uid);
    }

    /**
     * 获取用户全部信息
     * @param uid
     * @return
     */
    public UserDO getUserInfo(int uid) {
        return userMapper.selectUserInfoFromUid(uid);
    }

    /**
     * 修改用户信息, 原子操作, 不需要事务
     * @param uid user id
     * @param newContact new contact
     */
    public void modifyUserInfo(Integer uid, String newContact, String newName) {
        userMapper.updateInfoFromUserDO(new UserDO(uid, newContact, newName));
    }

    /**
     * 删除用户, 从用户表和书表, 事务支持
     * @param uid user id
     */
    @Transactional
    public void deleteUser(int uid) throws IOException {

        // get book list to delete
        List<Integer> bookIds = bookMapper.selectBooksIdFromUid(uid);

        // delete user from users table
        userMapper.deleteUser(uid);

        // delete user's book from books table
        bookMapper.deleteAllBookOfUser(uid);

        // delete book search index
        for (Integer bookId : bookIds) {
            searchHelper.deleteDocument(bookId);
        }
    }

}

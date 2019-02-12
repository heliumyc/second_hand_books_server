package com.bookexchange.app.model.dao;

import com.bookexchange.app.model.model.BookDO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface BookMapper {

    @Select("SELECT bid, title, price, note, uid FROM Books WHERE uid = #{uid}")
    List<BookDO> selectBooksFromUid(int uid);

    @Select("SELECT bid FROM Books WHERE uid=#{uid}")
    List<Integer> selectBooksIdFromUid(int uid);

    @Select("SELECT uid FROM Books WHERE bid=#{bid}")
    Integer selectUidFromBid(int uid);

    @Insert("INSERT INTO Books(title, price, note, uid) VALUES(#{book.title}, #{book.price}, #{book.note}, #{book.uid});")
    @Options(useGeneratedKeys=true, keyProperty="book.bid", keyColumn="book.bid")
    void insertBook(@Param("book") BookDO book);

    @Delete("DELETE FROM Books WHERE bid = #{bid} AND uid = #{uid}")
    void deleteBook(@Param("bid") int bid, @Param("uid") int uid);

//    @Select("SELECT bid, title, price, note, contact FROM Books LEFT JOIN users USING(uid)")
//    List<BookWithContactDO> selectBooks();

    @Update("UPDATE Books SET title=#{title}, price=#{price}, note=#{note} WHERE bid=#{bid} AND uid=#{uid}")
    void modifyBook(BookDO book);

    @Select("SELECT bid, title, price, note, uid FROM Books WHERE bid=#{bid}")
    BookDO selectOneBook(@Param("bid") int bid);
    
    @Delete("DELETE FROM Books WHERE uid=#{uid}")
    void deleteAllBookOfUser(int uid);

}

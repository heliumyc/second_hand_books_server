package com.bookexchange.app.model.dao;

import com.bookexchange.app.model.model.BookDO;
import com.bookexchange.app.model.model.BookWithContactDO;
import com.bookexchange.app.utils.SearchHelper;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Repository
public class BookDAO {
    private final
    BookMapper bookMapper;

    private final
    UserMapper userMapper;

    private final
    SearchHelper searchHelper;

    @Autowired
    public BookDAO(BookMapper bookMapper, UserMapper userMapper, SearchHelper searchHelper) {
        this.bookMapper = bookMapper;
        this.userMapper = userMapper;
        this.searchHelper = searchHelper;
    }

    /**
     * 获得一个用户的书籍
     * @param uid user id
     * @return a list of plain book
     */
    public List<BookDO> getBooksOfUser(int uid) {
        return bookMapper.selectBooksFromUid(uid);
    }

    /**
     * 获得带联系方式的书的信息
     * @param bid book id
     * @return a book with contact info
     */
    public BookWithContactDO getBookInfo(int bid) {
        BookDO book = bookMapper.selectOneBook(bid);
        if (book == null) return null;
        String contact = userMapper.selectContactFromUid(book.getUid());
        return new BookWithContactDO(book, contact);
    }

    /**
     * 从lucene处获得检索到的书的id
     * @param query 检索式
     * @param page 页数
     * @param pageSize 每页大小
     * @return a list of book id
     * @throws IOException exception from lucene
     * @throws ParseException exception from lucene
     */
    public List<Integer> queryToBooksId(String query, int page, int pageSize) throws IOException, ParseException {
        return searchHelper.query(query, page, pageSize);
    }

    /**
     * 添加新书
     * @param uid user id
     * @param book a plain book
     */
    @Transactional
    public void addNewBook(int uid, BookDO book) throws IOException {
        book.setUid(uid);
        bookMapper.insertBook(book);
        searchHelper.addNewDocument(book.getBid(), book.getTitle());
    }

    /**
     * modify a book
     * @param uid user id
     * @param bid book id
     * @param book a plain book
     */
    @Transactional
    public void modifyBook(int uid, int bid, BookDO book) throws IOException {
        if (authCheck(uid, bid)) {
            book.setUid(uid);
            book.setBid(bid);
            bookMapper.modifyBook(book);
            searchHelper.updateDocument(bid, book.getTitle());
        }
    }

    /**
     * auth check for class inner usage
     * @param uid user id
     * @param bid book id
     * @return boolean
     */
    private boolean authCheck(int uid, int bid) {
        Integer uidFromDB = bookMapper.selectUidFromBid(bid);
        return uidFromDB != null && uidFromDB == uid;
    }

//    /**
//     * 删除一本书
//     * @param uid user id
//     * @param bid book id
//     */
//    @Transactional
//    public void deleteOneBook(int uid, int bid) throws Exception {
//        bookMapper.deleteBook(bid, uid);
//    }

    /**
     * 删除一些书
     * @param uid user id
     * @param selection a list of book id
     * @throws IOException thrown from lucene
     */
    @Transactional
    public void deleteBookSelection(int uid, List<Integer> selection) throws IOException {
        for (Integer bid : selection) {
            if (authCheck(uid, bid)) {
                bookMapper.deleteBook(bid, uid);
                searchHelper.deleteDocument(bid);
            }
        }
    }

//    @Transactional
//    public void testTransactionSlow() throws IOException {
//
//        try {
//            bookMapper.insertBook(new BookDO("1 transaction book", 15, 8));
//            searchHelper.addNewDocument(1 , "1 transaction book");
//            Thread.sleep(5000);
//            bookMapper.insertBook(new BookDO("2 transaction book", 15, 8));
//            searchHelper.addNewDocument(2 , "2 transaction book");
//            Thread.sleep(5000);
//            bookMapper.insertBook(new BookDO("3 transaction book", 15, 8));
//            searchHelper.addNewDocument(3 , "3 transaction book");
//            Thread.sleep(5000);
//            int i = 1/0;
//            bookMapper.insertBook(new BookDO("4 transaction book", 15, 8));
//            searchHelper.addNewDocument(4 , "4 transaction book");
//            bookMapper.insertBook(new BookDO("5 transaction book", 15, 8));
//            searchHelper.addNewDocument(5 , "5 transaction book");
//        } catch (Exception e) {
//            System.out.println("we got a transaction to roll back!");
//            throw new RuntimeException();
//        }
//    }
//
//    @Transactional
//    public void testTransactionFast() throws IOException {
//
//        try {
//            bookMapper.insertBook(new BookDO("fast 1", 15, 8));
//            searchHelper.addNewDocument(1 , "fast 1");
//            bookMapper.insertBook(new BookDO("fast 2", 15, 8));
//            searchHelper.addNewDocument(2 , "fast 2");
//            bookMapper.insertBook(new BookDO("fast 3", 15, 8));
//            searchHelper.addNewDocument(3 , "fast 3");
//        } catch (Exception e) {
//            System.out.println("we got a transaction to roll back!");
//            throw new RuntimeException();
//        }
//    }

}

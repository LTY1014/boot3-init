package com.lty.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lty.model.dto.book.BookQueryRequest;
import com.lty.model.entity.Book;

/**
* @author lty
*/
public interface BookService extends IService<Book> {

    /**
     * 校验
     * @param book
     * @param add
     */
    void validBook(Book book,boolean add);

    /**
     * 获取查询条件
     * @param bookQueryRequest
     * @return
     */
    QueryWrapper<Book> getQueryWrapper(BookQueryRequest bookQueryRequest);
}

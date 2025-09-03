package com.lty.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lty.common.ErrorCode;
import com.lty.constant.BaseConstant;
import com.lty.exception.BusinessException;
import com.lty.mapper.BookMapper;
import com.lty.model.dto.book.BookQueryRequest;
import com.lty.model.entity.Book;
import com.lty.service.BookService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @author lty
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book>
        implements BookService {

    @Override
    public void validBook(Book book, boolean add) {
        if (book == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // TODO 添加要验证的字段
        Long id = book.getId();
        String bookName = book.getBookName();

        if(add){
            if(StringUtils.isAnyBlank(bookName)){
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
    }

    @Override
    public QueryWrapper<Book> getQueryWrapper(BookQueryRequest bookQueryRequest) {
        if (bookQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Book bookQuery = new Book();
        BeanUtils.copyProperties(bookQueryRequest, bookQuery);
        String sortField = bookQueryRequest.getSortField();
        String sortOrder = bookQueryRequest.getSortOrder();
        QueryWrapper<Book> qw = new QueryWrapper<>();
        // TODO 添加字段支持搜索
        String bookName = bookQuery.getBookName();
        String type = bookQuery.getType();

        // TODO 添加查询条件
        qw.like(StringUtils.isNoneBlank(bookName), "bookName", bookName);
        qw.eq(StringUtils.isNoneBlank(type), "type", type);

        qw.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(BaseConstant.SORT_ORDER_ASC), sortField);
        return qw;
    }
}





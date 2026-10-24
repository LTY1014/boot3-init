package com.lty.business.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lty.business.mapper.BookMapper;
import com.lty.business.model.dto.BookQueryRequest;
import com.lty.business.model.entity.Book;
import com.lty.common.BaseResponse;
import com.lty.common.ErrorCode;
import com.lty.common.IdRequest;
import com.lty.common.ResultUtils;
import com.lty.exception.BusinessException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 书
 * @author lty
 */
@Slf4j
@RestController
@RequestMapping("/book")
public class BookController {

    @Resource
    private BookMapper bookMapper;

    @PostMapping("/add")
    public BaseResponse<Long> addBook(@RequestBody Book bookAddRequest) {
        if (bookAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Book book = new Book();
        BeanUtils.copyProperties(bookAddRequest, book);
        int newId = bookMapper.insert(book);
        if (!(newId > 0)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(book.getId());
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateBook(@RequestBody Book bookUpdateRequest) {
        if (bookUpdateRequest == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        Book book = new Book();
        BeanUtils.copyProperties(bookUpdateRequest, book);
        Book oldBook = bookMapper.selectById(bookUpdateRequest.getId());
        if (oldBook == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        int i = bookMapper.updateById(book);
        Boolean result = i > 0;
        return ResultUtils.success(result);
    }

    @PostMapping("/id")
    public BaseResponse<Book> getByBookId(@RequestBody IdRequest idRequest) {
        if (idRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Book book = bookMapper.selectById(idRequest.getId());
        return ResultUtils.success(book);
    }

    @PostMapping("/list")
    public BaseResponse<List<Book>> listBook(@RequestBody Book book) {
        Wrapper<Book> qw = this.buildQueryWrapper(book);
        List<Book> bookList = bookMapper.selectList(qw);
        return ResultUtils.success(bookList);
    }

    @PostMapping(value = "/list/page")
    public BaseResponse<Page<Book>> listBookByPage(@RequestBody BookQueryRequest request) {
        // 分页基本字段
        long current = request.getCurrent();
        long size = request.getPageSize();
        // 反爬虫
        if (size > 200) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "查询数量过多");
        }
        Page<Book> page = new Page<>(current, size);
        Book book = new Book();
        BeanUtils.copyProperties(request, book);
        Page<Book> bookPage = bookMapper.selectPage(page, buildQueryWrapper(book));
        return ResultUtils.success(bookPage);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteBook(@RequestBody IdRequest deleteRequest) {
        if (deleteRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int i = bookMapper.deleteBatchIds(deleteRequest.getIds());
        Boolean result = i > 0;
        return ResultUtils.success(result);
    }

    public Wrapper<Book> buildQueryWrapper(Book book) {
        LambdaQueryWrapper<Book> qw = new LambdaQueryWrapper<>();
        // TODO 添加查询条件
        if (book != null) {
            qw.eq(book.getId() != null, Book::getId, book.getId());
            qw.eq(StringUtils.isNotBlank(book.getBookName()), Book::getBookName, book.getBookName());
            qw.eq(StringUtils.isNotBlank(book.getType()), Book::getType, book.getType());
            qw.eq(book.getPrice() != null, Book::getPrice, book.getPrice());
        }
        return qw;
    }
}

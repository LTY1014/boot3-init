package com.lty.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lty.common.BaseResponse;
import com.lty.common.ErrorCode;
import com.lty.common.IdRequest;
import com.lty.common.ResultUtils;
import com.lty.exception.BusinessException;
import com.lty.model.dto.book.BookAddRequest;
import com.lty.model.dto.book.BookQueryRequest;
import com.lty.model.dto.book.BookUpdateRequest;
import com.lty.model.entity.Book;
import com.lty.service.BookService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lty
 */
@Slf4j
@RequestMapping("/book")
@RestController
public class BookController {
    @Resource
    public BookService bookService;

    @PostMapping("/add")
    public BaseResponse<Long> addBook(@RequestBody BookAddRequest bookAddRequest) {
        if (bookAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Book book = new Book();
        BeanUtils.copyProperties(bookAddRequest, book);
        boolean result = bookService.save(book);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newBookId = book.getId();
        return ResultUtils.success(newBookId);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateBook(@RequestBody BookUpdateRequest bookUpdateRequest) {
        if (bookUpdateRequest == null || bookUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        Book book = new Book();
        BeanUtils.copyProperties(bookUpdateRequest, book);
        long id = bookUpdateRequest.getId();
        Book oldBook = bookService.getById(id);
        if (oldBook == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        boolean result = bookService.updateById(book);
        return ResultUtils.success(result);
    }

    @PostMapping("/id")
    public BaseResponse<Book> getByBookId(@RequestBody IdRequest idRequest) {
        if (idRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Book book = bookService.getById(idRequest.getId());
        return ResultUtils.success(book);
    }

    @PostMapping("/list")
    public BaseResponse<List<Book>> listBook(@RequestBody BookQueryRequest bookQueryRequest) {
        QueryWrapper<Book> qw = bookService.getQueryWrapper(bookQueryRequest);
        List<Book> bookList = bookService.list(qw);
        return ResultUtils.success(bookList);
    }

    @PostMapping("/list/page")
    public BaseResponse<Page<Book>> listBookByPage(@RequestBody BookQueryRequest bookQueryRequest) {
        // 分页基本字段
        long current = bookQueryRequest.getCurrent();
        long size = bookQueryRequest.getPageSize();
        // 反爬虫
        if (size > 100) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "查询数量过多");
        }
        Page<Book> page = new Page<>(current, size);
        Page<Book> bookPage = bookService.page(page, bookService.getQueryWrapper(bookQueryRequest));
        return ResultUtils.success(bookPage);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteBook(@RequestBody IdRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = deleteRequest.getId();
        Book oldBook = bookService.getById(id);
        if (oldBook == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        boolean b = bookService.removeById(id);
        return ResultUtils.success(b);
    }
}

package com.lty.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lty.common.BaseResponse;
import com.lty.common.ErrorCode;
import com.lty.common.IdRequest;
import com.lty.common.PageRequest;
import com.lty.common.ResultUtils;
import com.lty.exception.BusinessException;
import com.lty.mapper.BookMapper;
import com.lty.model.entity.Book;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
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
        if (newId <= 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success((long) newId);
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
    public BaseResponse<Page<Book>> listBookByPage(@RequestBody PageRequest request) {
        // 分页基本字段
        long current = request.getCurrent();
        long size = request.getPageSize();
        // 反爬虫
        if (size > 200) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "查询数量过多");
        }
        Page<Book> page = new Page<>(current, size);
        Page<Book> bookPage = bookMapper.selectPage(page, null);
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
        QueryWrapper<Book> qw = new QueryWrapper<>();
        // TODO 添加查询条件

        return qw;
    }
}

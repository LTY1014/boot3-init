package com.lty.model.dto.book;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author lty
 */
@Data
public class BookUpdateRequest implements Serializable {

    private long id;

    private String bookName;

    /**
     * 类型
     */
    private String type;

    /**
     * 价格
     */
    private BigDecimal price;
}

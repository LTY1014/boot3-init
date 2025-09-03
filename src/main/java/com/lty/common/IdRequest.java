package com.lty.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author lty
 */
@Data
public class IdRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * ids
     */
    private List<Long> ids;

    private static final long serialVersionUID = 1L;
}
package com.driver.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class SliceDTO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<T> content;

    private int page;

    private int size;

    private boolean hasNext;
}

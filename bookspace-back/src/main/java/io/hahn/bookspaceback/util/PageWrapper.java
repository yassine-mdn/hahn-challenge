package io.hahn.bookspaceback.util;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageWrapper<T> {

    private int totalPages;
    private long totalElements;
    private boolean hasNext;
    private boolean hasPrevious;
    private List<T> content;

    public PageWrapper(Page<T> page) {
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.hasPrevious = page.hasPrevious();
        this.hasNext = page.hasNext();
        this.content = page.getContent();
    }

    public PageWrapper(List<T> list) {
        this.totalPages = 1;
        this.totalElements = list.size();
        this.hasPrevious = false;
        this.hasNext = false;
        this.content = list;
    }
}
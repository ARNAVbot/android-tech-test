package com.bridge.androidtechnicaltest.network;

import java.util.List;

public class PagedResponseWrapper<T> {
    private List<T> items;
    private int pageNumber;
    private int itemCount;
    private int totalPages;

    public PagedResponseWrapper() {
    }

    public PagedResponseWrapper(List<T> items, int pageNumber, int itemCount, int totalPages) {
        this.items = items;
        this.pageNumber = pageNumber;
        this.itemCount = itemCount;
        this.totalPages = totalPages;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
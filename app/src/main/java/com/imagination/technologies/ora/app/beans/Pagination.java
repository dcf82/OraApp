package com.imagination.technologies.ora.app.beans;

public class Pagination {
    private int count;
    private int page_count;
    private int current_page;
    private boolean has_next_page;
    private boolean has_prev_page;
    private String limit;

    public int getPage_count() {
        return page_count;
    }

    public void setPage_count(int page_count) {
        this.page_count = page_count;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public boolean isHas_next_page() {
        return has_next_page;
    }

    public void setHas_next_page(boolean has_next_page) {
        this.has_next_page = has_next_page;
    }

    public boolean isHas_prev_page() {
        return has_prev_page;
    }

    public void setHas_prev_page(boolean has_prev_page) {
        this.has_prev_page = has_prev_page;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }
}

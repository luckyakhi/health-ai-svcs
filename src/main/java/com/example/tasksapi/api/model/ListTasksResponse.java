package com.example.tasksapi.api.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class ListTasksResponse {
    @NotNull
    private List<Task> items;

    @Min(1)
    private int page;

    @Min(1)
    private int pageSize;

    @Min(0)
    private long total;

    private boolean hasNext;

    public List<Task> getItems() { return items; }
    public void setItems(List<Task> items) { this.items = items; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }

    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }

    public boolean isHasNext() { return hasNext; }
    public void setHasNext(boolean hasNext) { this.hasNext = hasNext; }
}


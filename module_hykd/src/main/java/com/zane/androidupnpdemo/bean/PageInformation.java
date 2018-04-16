package com.zane.androidupnpdemo.bean;

import java.util.List;

/**
 * Created by gzq on 2018/4/13.
 */

public class PageInformation {
    public int endRow;
    public int firstPage;
    public boolean hasNextPage;
    public boolean hasPreviousPage;
    public boolean isFirstPage;
    public boolean isLastPage;
    public  int lastPage;
    public int navigatePages;
    public int[] navigatepageNums;
    public int nextPage;
    public String orderBy;
    public int pageNum;
    public int pageSize;
    public int pages;
    public int prePage;
    public int size;
    public int startRow;
    public int total;
    public List<ServiceDoctor> list;

    public static class ServiceDoctor{
        public String doctorName;
        public int serviceId;
        public String serviceTime;
        public int ROW_ID;
    }
}

package com.jiuyi.vggle.dto;

import java.io.Serializable;

/**
 * @author Administrator
 * 
 */
public class Page implements Serializable {
    private static final long serialVersionUID = -7387871970357565418L;

    /** 每页显示的记录数. */
    private Integer pageSize;//

    /** 总记录数. */
    private Integer totalRecord;

    /** 总页数. */
    private Integer totalPage;

    /** 页码. */
    private Integer currentPage;

    /**
     * @return the pageSize
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * @param pageSize
     *            the pageSize to set
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * @return the totalRecord
     */
    public int getTotalRecord() {
        return totalRecord;
    }

    /**
     * @param totalRecord
     *            the totalRecord to set
     */
    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;

        int totalPage = totalRecord % pageSize == 0 ? totalRecord / pageSize
                : totalRecord / pageSize + 1;

        this.setTotalPage(totalPage);
    }

    /**
     * @return the totalPage
     */
    public int getTotalPage() {
        return totalPage;
    }

    /**
     * @param totalPage
     *            the totalPage to set
     */
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    /**
     * @return the crrentPage
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * @param crrentPage
     *            the crrentPage to set
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
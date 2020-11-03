/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntq.bean;

import com.ntq.pojo.Lichsu;
import com.ntq.service.LichsuService;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import javax.persistence.Transient;

/**
 *
 * @author songo
 */
@ManagedBean
@Named(value = "lichsuBean")
@ViewScoped
public class LichsuBean {
    private static LichsuService lichsuSerice = new LichsuService();
    @Transient
    private int page = 1;
    @Transient
    private int number = 10;
    //////// sap xep///////////////
    private int orderold; // 1: id, 2: người chơi, 3: câu hỏi, 4: loại, 5: số điểm, 6: thời gian
    private int ordernew;
    private boolean typesort;  // false: tăng dần, true: giảm dần
    /////////////////
    @Transient
    private String typesearch;
    @Transient
    private String keyword;
    /**
     * Creates a new instance of LichsuBean
     */
    public LichsuBean() {
    }
    public void setSort(int type)
    {
        if(type != orderold)
            typesort = false;
        else
            typesort = !typesort;
        ordernew = type;
        orderold = type;
            
    }
    public void setDefaultSort()
    {
        page = 1;
        typesort = false;
        ordernew = 0;
        orderold = 0;
    }
    public List<Lichsu> getLichsu()
    {
        List<Lichsu> a= lichsuSerice.getLichsu();
        return a;
    }
    public List<Lichsu> getLichsuView()
    {
        List<Lichsu> a= lichsuSerice.getLichsuView();
        return a;
    }
    public List<Lichsu> getListLichsu()
    {
        return lichsuSerice.getLichsu(number, getPage(),keyword, typesearch, ordernew, typesort);
    }
    
    public void ChangPage(int i)
    {
        if(i < 0)
            page = page - 1;
        else
            if(i>0)
                page = page + 1;
        else
                page = 1;
    }

    /**
     * @return the page
     */
    public int getPage() {
        return page;
    }

    /**
     * @param page the page to set
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * @return the orderold
     */
    public int getOrderold() {
        return orderold;
    }

    /**
     * @param orderold the orderold to set
     */
    public void setOrderold(int orderold) {
        this.orderold = orderold;
    }

    /**
     * @return the ordernew
     */
    public int getOrdernew() {
        return ordernew;
    }

    /**
     * @param ordernew the ordernew to set
     */
    public void setOrdernew(int ordernew) {
        this.ordernew = ordernew;
    }

    /**
     * @return the typesort
     */
    public boolean isTypesort() {
        return typesort;
    }

    /**
     * @param typesort the typesort to set
     */
    public void setTypesort(boolean typesort) {
        this.typesort = typesort;
    }

    /**
     * @return the typesearch
     */
    public String getTypesearch() {
        return typesearch;
    }

    /**
     * @param typesearch the typesearch to set
     */
    public void setTypesearch(String typesearch) {
        this.typesearch = typesearch;
    }

    /**
     * @return the keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * @param keyword the keyword to set
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    
}

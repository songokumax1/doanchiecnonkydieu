/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntq.bean;

import com.ntq.pojo.Cauhoi;
import com.ntq.pojo.Loaicauhoi;
import com.ntq.service.CauhoiService;
import com.ntq.service.LoaicauhoiService;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Transient;

/**
 *
 * @author songo
 */
@ManagedBean
@Named(value = "cauhoiBean")
@ViewScoped
public class CauhoiBean {
    private static CauhoiService cauhoiSerice = new CauhoiService();
    private static LoaicauhoiService loaicauhoiSerice = new LoaicauhoiService();
    @Transient
    private int page = 1;
    @Transient
    private int number = 10;
    @Transient
    private int isEdit;
    private int band;
    private String noidung;
    private String dapan;
    private Loaicauhoi loaicauhoi;
    @Transient
    private Loaicauhoi loaicauhoicu;
    //////// sap xep///////////////
    private int orderold; // 1: id, 2: Mội dung, 3: đáp án, 4: Loại câu hỏi, 5: tình trạng
    private int ordernew;
    private boolean typesort;  // false: tăng dần, true: giảm dần
    /////////////////
    @Transient
    private String typesearch;
    @Transient
    private String keyword;
    
    /**
     * Creates a new instance of CauhoiBean
     */
    public CauhoiBean() {
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
    public boolean checkCauhoiNext()
    {
        List<Cauhoi> a =  cauhoiSerice.getCauhoi(number, getPage()+1,keyword, typesearch, ordernew, typesort);
        if(a==null || a.size()==0)
            return false;
        else
            return true;
    }
    public List<Cauhoi> getCauhoi()
    {
        List<Cauhoi> a= cauhoiSerice.getCauhoi();
        return a;
    }
    public List<Cauhoi> getListCauhoi()
    {
        return cauhoiSerice.getCauhoi(number, getPage(),keyword, typesearch, ordernew, typesort);
    }
    public void ChangeEdit(Cauhoi l)
    {
        this.isEdit = l.getId();
        this.noidung = l.getNoidung();
        this.dapan = l.getDapan();
        this.band = l.getBand();
        this.loaicauhoi = l.getLoaicauhoi();
        this.loaicauhoicu = l.getLoaicauhoi();
        
        ///////////////////////
        
    }
    public void HuyChangeEdit()
    {
        this.isEdit = -2;
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
    public void SaveChange()
    {
        Cauhoi u = new Cauhoi();
        u.setDapan(this.dapan);
        u.setNoidung(this.noidung);
        u.setLoaicauhoi(this.loaicauhoi);
        u.setId(this.isEdit);
        u.setBand(this.band);
        Loaicauhoi lch = null;
        if(!this.loaicauhoi.equals(this.loaicauhoicu))
        {
            lch = loaicauhoiSerice.getLoaicauhoiById(this.loaicauhoi.getId());
            if(lch.getBand()==1)
                u.setBand(1);
        }
        ///////////////default///////////////
        this.isEdit= -2;
        this.dapan = "";
        this.noidung = "";
        this.loaicauhoi = null;
        this.loaicauhoicu = null;
        this.band = 0;
        //////////////////////////
        
        boolean kq = cauhoiSerice.addOrSaveCauhoi(u);
        if(kq)
        {
        FacesMessage mgs = new FacesMessage("THÀNH CÔNG");
            mgs.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, mgs);
        }
        else
        {
            FacesMessage mgs = new FacesMessage("THẤT BẠI");
            mgs.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, mgs);
        }
    }
    public void Add()
    {
        Cauhoi u = new Cauhoi();
        u.setDapan(this.dapan);
        u.setNoidung(this.noidung);
        u.setLoaicauhoi(this.loaicauhoi);
        this.setNoidung("");
        this.setDapan("");
        this.setLoaicauhoi(null);
        boolean kq = cauhoiSerice.addOrSaveCauhoi(u);
        if(kq)
        {
        FacesMessage mgs = new FacesMessage("THÀNH CÔNG");
            mgs.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, mgs);
        }
        else
        {
            FacesMessage mgs = new FacesMessage("THẤT BẠI");
            mgs.setSeverity(FacesMessage.SEVERITY_WARN);
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, mgs);
        }
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
     * @return the isEdit
     */
    public int getIsEdit() {
        return isEdit;
    }

    /**
     * @param isEdit the isEdit to set
     */
    public void setIsEdit(int isEdit) {
        this.isEdit = isEdit;
    }

    /**
     * @return the noidung
     */
    public String getNoidung() {
        return noidung;
    }

    /**
     * @param noidung the noidung to set
     */
    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    /**
     * @return the dapan
     */
    public String getDapan() {
        return dapan;
    }

    /**
     * @param dapan the dapan to set
     */
    public void setDapan(String dapan) {
        this.dapan = dapan;
    }

    /**
     * @return the loaicauhoi
     */
    public Loaicauhoi getLoaicauhoi() {
        return loaicauhoi;
    }

    /**
     * @param loaicauhoi the loaicauhoi to set
     */
    public void setLoaicauhoi(Loaicauhoi loaicauhoi) {
        this.loaicauhoi = loaicauhoi;
    }

    /**
     * @return the band
     */
    public int getBand() {
        return band;
    }

    /**
     * @param band the band to set
     */
    public void setBand(int band) {
        this.band = band;
    }

    /**
     * @return the loaicauhoicu
     */
    public Loaicauhoi getLoaicauhoicu() {
        return loaicauhoicu;
    }

    /**
     * @param loaicauhoicu the loaicauhoicu to set
     */
    public void setLoaicauhoicu(Loaicauhoi loaicauhoicu) {
        this.loaicauhoicu = loaicauhoicu;
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

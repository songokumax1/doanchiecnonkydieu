/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntq.bean;

import com.ntq.pojo.Loaicauhoi;
import com.ntq.service.LoaicauhoiService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Transient;
import javax.servlet.http.Part;

/**
 *
 * @author songo
 */
@ManagedBean
@Named(value = "loaicauhoiBean")
@ViewScoped
public class LoaicauhoiBean {
    private static LoaicauhoiService loaicauhoiSerice = new LoaicauhoiService();
    @Transient
    private int page = 1;
    @Transient
    private int number = 10;
    @Transient
    private int isEdit;
    @Transient
    private Part imgFile;
    private int band;
    
    private int id;
    private String name;
    private String img;
    ////////////////////////
    //////// sap xep///////////////
    private int orderold; // 1: id, 2: Tên, 3: ngày cập nhật, 4: Tình trạng
    private int ordernew;
    private boolean typesort;  // false: tăng dần, true: giảm dần
    /////////////////
    @Transient
    private String typesearch;
    @Transient
    private String keyword;
    /**
     * Creates a new instance of LoaicauhoiBean
     */
    public LoaicauhoiBean() {
        
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
    public boolean checkLoaiCauHoiNext()
    {
        List<Loaicauhoi> a =  loaicauhoiSerice.getLoaicauhoi(number, getPage()+1,keyword, typesearch, ordernew, typesort);
        if(a==null || a.size()==0)
            return false;
        else
            return true;
    }
    public List<Loaicauhoi> getLoaicauhoi()
    {
        List<Loaicauhoi> a= loaicauhoiSerice.getLoaicauhoi(0);
        return a;
    }
    public List<Loaicauhoi> getLoaicauhoiControl()
    {
        List<Loaicauhoi> a= loaicauhoiSerice.getLoaicauhoi(1);
        return a;
    }
    public List<Loaicauhoi> getListLoaicauhoi()
    {
        return loaicauhoiSerice.getLoaicauhoi(number, getPage(),keyword, typesearch, ordernew, typesort);
    }
    public void ChangeEdit(Loaicauhoi l)
    {
        this.isEdit = l.getId();
        this.name = l.getName();
        this.band=l.getBand();
        
        
        ///////////////////////
        
    }
    
    public void HuyChangeEdit()
    {
        this.isEdit = -2;
    }
    
    public void SaveChange(String namefile)
    {
        Loaicauhoi l = new Loaicauhoi();
        l.setId(isEdit);
        l.setName(name);
        l.setBand(band);
        ///////////////default///////////////
        this.isEdit= -2;
        //////////////////////////
        try {
            if(imgFile != null)
            {
                this.uploadFile();
//                File file = new File(FacesContext.getCurrentInstance().getExternalContext()
//                    .getRealPath("resources/images") + namefile);
//                file.delete();
                l.setImg("/loaicauhoi/" + this.imgFile.getSubmittedFileName());
            }
            else
                l.setImg(namefile);
            Date d = new Date();
            l.setDate(d);
            boolean kq = loaicauhoiSerice.addOrSaveLoaicauhoi(l);
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

            } catch (IOException ex) {
                FacesMessage mgs = new FacesMessage("Không thể upload ảnh");
                mgs.setSeverity(FacesMessage.SEVERITY_WARN);
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, mgs);
                Logger.getLogger(LoaicauhoiBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        
    }
    public void Add()
    {
        Loaicauhoi l = new Loaicauhoi();
        l.setDate(new Date());
        l.setName(this.name);
        l.setCauhoi(null);
        try {
            if(imgFile != null)
            {
                this.uploadFile();
//                File file = new File(FacesContext.getCurrentInstance().getExternalContext()
//                    .getRealPath("resources/images") + namefile);
//                file.delete();
                l.setImg("/loaicauhoi/" + this.imgFile.getSubmittedFileName());
            }
            // SET ve default
            this.setName("");
            this.setImgFile(null);
            boolean kq = loaicauhoiSerice.addOrSaveLoaicauhoi(l);
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

            } catch (IOException ex) {
                FacesMessage mgs = new FacesMessage("Không thể upload ảnh");
                mgs.setSeverity(FacesMessage.SEVERITY_WARN);
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, mgs);
                Logger.getLogger(LoaicauhoiBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        
    }
    private void uploadFile() throws IOException
    {
        String path = FacesContext.getCurrentInstance().getExternalContext()
                .getRealPath("resources/images/loaicauhoi") + "/" + this.imgFile.getSubmittedFileName();
        try(InputStream in = this.imgFile.getInputStream();
                FileOutputStream out = new FileOutputStream(path))
        {
            byte[] b = new byte[1024];
            int byRead;
            while((byRead = in.read(b)) != -1)
            {
                out.write(b, 0, byRead);
            }
        }
        
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
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the img
     */
    public String getImg() {
        return img;
    }

    /**
     * @param img the img to set
     */
    public void setImg(String img) {
        this.img = img;
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
     * @return the imgFile
     */
    public Part getImgFile() {
        return imgFile;
    }

    /**
     * @param imgFile the imgFile to set
     */
    public void setImgFile(Part imgFile) {
        this.imgFile = imgFile;
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

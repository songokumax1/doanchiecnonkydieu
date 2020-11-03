/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntq.bean;

import com.ntq.conf.VerifyRecaptcha;
import com.ntq.pojo.User;
import com.ntq.pojo.Lichsu;
import com.ntq.pojo.UserRank;
import com.ntq.service.LichsuService;
import com.ntq.service.UserService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Transient;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author songo
 */
@ManagedBean
@Named(value = "userBean")
@ViewScoped
public class UserBean {
    @Transient
    private int page = 1;
    @Transient
    private int number = 10;
    @Transient
    private int isEdit;
    @Transient
    private String typesearch;
    @Transient
    private String keyword;
    ////////////////////////////
    private int id;
    private String name;
    private String email;
    private String username;
    private String password;
    private String role;
    private int diem;
    private int band;
    //////// sap xep///////////////
    private int orderold; // 1: id, 2: họ tên, 3: username, 4: email, 5: ngaydangky, 6: diem, 7 quyền, 8: tình trạng
    private int ordernew;
    private boolean typesort;  // false: tăng dần, true: giảm dần
    /////////////////
    @Transient
    private String confirmPass;
    private static UserService userSerice = new UserService();
    private static LichsuService lichsuSerice = new LichsuService();
    
    public String PageReg()
    {
        return "reg?faces-redirect=true";
    }
    public String PageLogin()
    {
        return "login?faces-redirect=true";
    }
    public List<UserRank> getRankUser()
    {
        List<UserRank> urank = new ArrayList<>();
        List<User> list = userSerice.getListRankUser();
        int i = 1;
        for(User u:list)
        {
            UserRank ur = new UserRank();
            ur.setUser(u);
            ur.setRank(i);
            urank.add(ur);
            i++;
        }
        return urank;
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
    public void getUserProfile()
    {
        User user =(User) FacesContext.getCurrentInstance().
                getExternalContext().getSessionMap().get("user");
        this.id = user.getId();
        this.name = user.getFullname();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }
    public void setUserProfile()
    {
        User user =(User) FacesContext.getCurrentInstance().
                getExternalContext().getSessionMap().get("user");
        String passconf = DigestUtils.md5Hex(confirmPass);
        if(user.getPass().equals(passconf))
        {
            if(password==null || password == "")
            {
                User u = new User();
                u.setId(id);
                u.setEmail(email);
                u.setFullname(name);
                u.setBand(user.getBand());
                u.setDiem(user.getDiem());
                u.setPass(user.getPass());
                u.setRegdate(user.getRegdate());
                u.setRole(user.getRole());
                u.setUsername(username);
                FacesMessage mgs;
                    
                if(userSerice.saveOrUpdate(u))
                {
                    mgs = new FacesMessage("Cập nhật thành công");
                    FacesContext.getCurrentInstance().
                getExternalContext().getSessionMap().remove("user");
                    FacesContext.getCurrentInstance().
                getExternalContext().getSessionMap().put("user", u);
                }
                else
                    mgs = new FacesMessage("Cập nhật thất bại");
                mgs.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, mgs);
            }
            else
            {
                User u = new User();
                u.setId(id);
                u.setEmail(email);
                u.setFullname(name);
                u.setBand(user.getBand());
                u.setDiem(user.getDiem());
                u.setPass(DigestUtils.md5Hex(password));
                u.setRegdate(user.getRegdate());
                u.setRole(user.getRole());
                u.setUsername(username);
                FacesMessage mgs;
                    
                if(userSerice.saveOrUpdate(u))
                {
                    mgs = new FacesMessage("Cập nhật thành công");
                    FacesContext.getCurrentInstance().
                getExternalContext().getSessionMap().remove("user");
                    FacesContext.getCurrentInstance().
                getExternalContext().getSessionMap().put("user", u);
                }
                else
                    mgs = new FacesMessage("Cập nhật thất bại");
                mgs.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, mgs);
            }
        }
        else
        {
                FacesMessage mgs = new FacesMessage("Sai mật khẩu xác minh");
                mgs.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, mgs);
        }
    }
    public void setDefaultSort()
    {
        page = 1;
        typesort = false;
        ordernew = 0;
        orderold = 0;
    }
    public List<Lichsu> getListLichSuUser()
    {
        return lichsuSerice.getLichsuUser(number, page, (User)FacesContext.getCurrentInstance().
                getExternalContext().getSessionMap().get("user"),0);
    }
    public List<Lichsu> getListAllLichSuUser()
    {
        return lichsuSerice.getLichsuUser(number, page, (User)FacesContext.getCurrentInstance().
                getExternalContext().getSessionMap().get("user"),1);
    }
    public boolean checkLichsuUserNext()
    {
        List<Lichsu> a =  lichsuSerice.getLichsuUser(number, page+1, (User)FacesContext.getCurrentInstance().
                getExternalContext().getSessionMap().get("user"),0);
        if(a==null || a.size()==0)
            return false;
        else
            return true;
    }
    public boolean submit_form() {
        try {
            String gRecaptchaResponse = FacesContext.getCurrentInstance().
                    getExternalContext().getRequestParameterMap().get("g-recaptcha-response");
            boolean verify = VerifyRecaptcha.verify(gRecaptchaResponse);
            if (verify) {
                return true;
            } else {
//                FacesContext context = FacesContext.getCurrentInstance();
//                context.addMessage(null, new FacesMessage("Select Captcha"));
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
    public String Reg() {
        User u = new User();
        u.setFullname(name);
        u.setEmail(email);
        u.setUsername(username);
        u.setPass(DigestUtils.md5Hex(password));
        u.setDiem(0);
        u.setRole("USER");
        u.setRegdate(new Date());
            if (submit_form()) {
                if (userSerice.checkUser(username, email)) {
                    FacesMessage mgs = new FacesMessage("Username hoặc Email đã tồn tại");
                    mgs.setSeverity(FacesMessage.SEVERITY_ERROR);
                    FacesContext context = FacesContext.getCurrentInstance();
                    context.addMessage(null, mgs);
                    return "";
                } else {
                    if (userSerice.saveOrUpdate(u) == true) {
                        return "index?faces-redirect=true";
                    } else {
                        FacesMessage mgs = new FacesMessage("Không thể đăng ký");
                        mgs.setSeverity(FacesMessage.SEVERITY_ERROR);
                        FacesContext context = FacesContext.getCurrentInstance();
                        context.addMessage("msgdangky", mgs);
                        return "";
                    }
                }
            } else {
                FacesMessage mgs = new FacesMessage("Xác minh robot lỗi");
                mgs.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, mgs);
                return "";
            }
    }
    
    public String Login()
    {
        if (submit_form()) {
            User u = userSerice.login(username, password);
            if (u != null) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", u);
                return "index?faces-redirect=true";
            } else {
                FacesMessage mgs = new FacesMessage("Tên đăng nhập hoặc mật khẩu sai!");
                mgs.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, mgs);
                return "";
            }
        } else {
            FacesMessage mgs = new FacesMessage("Xác minh robot lỗi");
            mgs.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, mgs);
            return "";
        }
    }
    public String Logout()
    {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("user");
        return "index?faces-redirect=true";
    }
    public List<User> getUser()
    {
       List<User> a =  userSerice.getUser(number, getPage(),keyword, typesearch, ordernew, typesort);
       return a;
    }
    public List<User> getListUser()
    {
       return userSerice.getListUser();
    }
    public boolean checkUserNext()
    {
        List<User> a =  userSerice.getUser(number, getPage()+1,keyword, typesearch, ordernew, typesort);
        if(a==null || a.size()==0)
            return false;
        else
            return true;
    }
    public void ChangPage(int i)
    {
        //ordernew = 0;
        if(i < 0)
            page = page - 1;
        else
            if(i>0)
                page = page + 1;
        else
                page = 1;
    }
    public void ChangeEdit(User u)
    {
        this.isEdit = u.getId();
        this.name = u.getFullname();
        this.email = u.getEmail();
        this.diem = u.getDiem();
        
        ///////////////////////
        
    }
    public void HuyChangeEdit()
    {
        this.isEdit = -2;
    }
    public void SaveChange()
    {
        User u = new User();
        u.setDiem(diem);
        u.setRole(role);
        u.setId(isEdit);
        u.setBand(band);
        
        ///////////////default///////////////
        this.isEdit= -2;
        //////////////////////////
        
        boolean kq = userSerice.updateUser(u);
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
     * Creates a new instance of UserBean
     */
    public UserBean() {
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
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the confirmPass
     */
    public String getConfirmPass() {
        return confirmPass;
    }

    /**
     * @param confirmPass the confirmPass to set
     */
    public void setConfirmPass(String confirmPass) {
        this.confirmPass = confirmPass;
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
     * @return the page
     */
    public int getPage() {
        return page;
    }

    /**
     * @param aPage the page to set
     */
    public void setPage(int aPage) {
        page = aPage;
    }

    /**
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
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
     * @return the diem
     */
    public int getDiem() {
        return diem;
    }

    /**
     * @param diem the diem to set
     */
    public void setDiem(int diem) {
        this.diem = diem;
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
    
}

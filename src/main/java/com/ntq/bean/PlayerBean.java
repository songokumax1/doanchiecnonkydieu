/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntq.bean;

import com.ntq.pojo.Cauhoi;
import com.ntq.pojo.Lichsu;
import com.ntq.pojo.User;
import com.ntq.service.CauhoiService;
import com.ntq.service.LichsuService;
import com.ntq.service.LoaicauhoiService;
import com.ntq.service.UserService;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

/**
 *
 * @author songo
 */
@ManagedBean
@Named(value = "playerBean")
@ViewScoped
public class PlayerBean {
     private static CauhoiService cauhoiSerice = new CauhoiService();
     private static UserService userSerice = new UserService();
     private static LichsuService lichsuSerice = new LichsuService();
     private static LoaicauhoiService loaicauhoiSerice = new LoaicauhoiService();
     private int id;
     private String dapan;  // đáp án của câu hỏi khi load lần đầu
     private String datraloi;  // đã trả lời của người chơi, những ký tự trả lười đúng sẽ hiện, ko thì ký tự *
     private int diemtong;  // điểm tồng của các lần trả lời đúng
     private int diemtam;  // điểm của vòng quay
     private int solansai;  // số lần trả lời sai, nếu từ 3 trở lên kết thúc vòng chơi
     private boolean statusPlay;  // false: ẩn nút quay, true là hiện nút quay
     private String traloi; // đáp án nhập vào
     private boolean btnmoi; // true: hiện nút chơi mới, false: ẩn đi
     private boolean btndoan; // true: hiện nút ĐOÁN, false ẩn.
     private int chondoan; // 0: trả lời từng chữ, 1: trả lời đoán hết
     private String value;
     private int loaitruot; // 0: trúng xui xeo, 1: hết time
    /**
     * Creates a new instance of PlayerBean
     */
    public PlayerBean() {
    }
    public String newPlay()
    {
        return "player.xhtml?faces-redirect=true&amp;id=" + value;
    }
    public void setDoan() // quay trúng ô xui xẻo
    {
        FacesMessage mgs;
        ++solansai;
        if (solansai >= 3) {
                    RequestContext.getCurrentInstance().execute("endgame();");
                    mgs = new FacesMessage("Bạn đã trả lời sai hoặc không trả lời 3 lần. RẤT TIẾC");
                    statusPlay = false;  // sai 3 lần set về false để ẩn nút quay.
                    btnmoi = true; // sai 3 lần qua chơi mới nên hiện.
                    btndoan = false; // sai 3 lần nút đoán ẩn.
                    saveLichSu(0);
        }
        else
            if(loaitruot==0)
            {
                RequestContext.getCurrentInstance().execute("doansai();");
                mgs = new FacesMessage("Gặp trúng ô xui xẻo.");
            }
            else
                if(loaitruot==-1)
                {
                    RequestContext.getCurrentInstance().execute("doansai();");
                    mgs = new FacesMessage("Gặp trúng ô mất điểm.");
                    diemtong = 0;
                }
        else
                {
                    RequestContext.getCurrentInstance().execute("doansai();");
                    mgs = new FacesMessage("Hết thời gian");
                }
        mgs.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, mgs);
    }
    public String getCauHoiChuan()
    {  /// lần chạy(load trang) đầu tiên
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        setValue(request.getParameter("id"));
        if(getValue()!=null && value != "")
        {
        setId(0);
        setDapan("");
        setDatraloi("");
        setDiemtong(0);
        setDiemtam(0);
        setSolansai(0);
        // set default///
        statusPlay = false;
        btnmoi = false;
        btndoan = true;
        chondoan = 0; // mặc định ban đầu trả lời từng chữ
        List<Cauhoi> a = cauhoiSerice.getCauHoiChuan("quangchinh", loaicauhoiSerice.getLoaicauhoiById(Integer.parseInt(getValue())));
        if(a.size()>0)
        {
            Random rand = new Random();
            int randomIndex = rand.nextInt(a.size());
            Cauhoi c = a.get(randomIndex);
            statusPlay = true; // có câu hỏi sẽ hiện nút QUAY
            id=c.getId();
            setDapan(c.getDapan());
            for(int i =0; i< c.getDapan().length(); i++)  // set da tra loi thanh chuoi *
                datraloi = datraloi + '*';
            return "Câu hỏi: " + c.getNoidung();
        }
        }
        return "Không có câu hỏi giành cho bạn";
    }
    public void setTraloi()
    {
        
        
        if (traloi.length() > 0) {
            if (traloi.length() > 1 && chondoan == 0) {
                ++solansai;
                diemtam = 0;
                traloi = "";
                FacesMessage mgs;

                if (solansai >= 3) {
                    RequestContext.getCurrentInstance().execute("endgame();");
                    mgs = new FacesMessage("Bạn đã trả lời sai 3 lần hoặc trả lời trùng từ đã đúng 3 lần. RẤT TIẾC");
                    statusPlay = false;
                    btnmoi = true;
                    btndoan = false;
                    //////////gọi xuống service lưu vào////////////
                    saveLichSu(0);
                    ///////////////////
                } else {
                    RequestContext.getCurrentInstance().execute("doansai();");
                    mgs = new FacesMessage("Nhập không hợp lệ. Chỉ cho phép 1 chữ.");
                }
                mgs.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, mgs);
            } else {
                if (chondoan == 0) { // trả lời từng chữ
                    if (datraloi.indexOf(traloi) != -1) {

                        ++solansai;
                        diemtam = 0;
                        traloi = "";
                        FacesMessage mgs;

                        if (solansai >= 3) {
                            RequestContext.getCurrentInstance().execute("endgame();");
                            mgs = new FacesMessage("Bạn đã trả lời sai 3 lần hoặc trả lời trùng từ đã đúng 3 lần. RẤT TIẾC");
                            statusPlay = false;
                            btnmoi = true;
                            btndoan = false;
                            //////////gọi xuống service lưu vào////////////
                                saveLichSu(0);
                            ///////////////////
                        } else {
                            RequestContext.getCurrentInstance().execute("doansai();");
                            mgs = new FacesMessage("Bạn đã trả lời đúng từ này trước đó rồi!");
                        }
                        mgs.setSeverity(FacesMessage.SEVERITY_ERROR);
                        FacesContext context = FacesContext.getCurrentInstance();
                        context.addMessage(null, mgs);
                    } else if (dapan.indexOf(traloi) != -1) {
                        int solanxh = 0;
                        for (int i = 0; i < dapan.length(); i++) {
                            if (dapan.charAt(i) == traloi.charAt(0)) {
                                String ctlmoi = datraloi.substring(0, i) + traloi.charAt(0) + datraloi.substring(i + 1);
                                datraloi = ctlmoi;
                                solanxh++;
                            }
                        }

                        FacesMessage mgs;

                        diemtong = diemtong + (solanxh * diemtam);

                        if (dapan.equals(datraloi)) {
                            RequestContext.getCurrentInstance().execute("setPhaoHoa();");
                            mgs = new FacesMessage("Bạn đã trả lời chính xác hết rồi. Pro quá, tổng điểm kiếm được " + diemtong + " điểm.");
                            
                            btnmoi = true;
                            statusPlay = false;
                            btndoan = false;
                            datraloi = dapan;
                            saveLichSu(1);
                        } else {
                            RequestContext.getCurrentInstance().execute("doantrung();");
                            mgs = new FacesMessage("Bạn đã trả lời đúng rồi nè, kiếm được " + solanxh * diemtam + " điểm.");
                        }
                        diemtam = 0;
                        //solansai = 0;
                        traloi = "";
                        mgs.setSeverity(FacesMessage.SEVERITY_ERROR);
                        FacesContext context = FacesContext.getCurrentInstance();
                        context.addMessage(null, mgs);
                    } else {
                        diemtam = 0;
                        solansai++;
                        traloi = "";
                        FacesMessage mgs;

                        if (solansai >= 3) {
                            RequestContext.getCurrentInstance().execute("endgame();");
                            mgs = new FacesMessage("Bạn đã trả lời sai 3 lần hoặc trả lời trùng từ đã đúng 3 lần liên tiếp. RẤT TIẾC");
                            statusPlay = false;
                            btnmoi = true;
                            btndoan = false;
                            //////////gọi xuống service lưu vào////////////
                               saveLichSu(0);
                            ///////////////////
                        } else {
                            RequestContext.getCurrentInstance().execute("doansai();");
                            mgs = new FacesMessage("Bạn đã trả sai rồi!");
                        }
                        mgs.setSeverity(FacesMessage.SEVERITY_ERROR);
                        FacesContext context = FacesContext.getCurrentInstance();
                        context.addMessage(null, mgs);
                    }
                } else {
                    FacesMessage mgs;
                    if (traloi.equals(dapan)) {
                        int demktcon = 0;
                        for (int i = 0; i < datraloi.length(); i++) {
                            if (datraloi.charAt(i) == '*') {
                                demktcon++;
                            }
                        }
                        diemtong = diemtong + 500 * demktcon;
                        datraloi = dapan;
                        RequestContext.getCurrentInstance().execute("setPhaoHoa();");
                        mgs = new FacesMessage("Bạn đã đoán đúng rồi. Pro quá, tổng điểm kiếm được " + diemtong + " điểm.");
                        saveLichSu(2);
                    } else {
                        RequestContext.getCurrentInstance().execute("endgame();");
                        mgs = new FacesMessage("Bạn đã đoán sai. RẤT TIẾC.");
                        saveLichSu(0);
                    }
                    btnmoi = true;
                    statusPlay = false;
                    btndoan = false;
                    mgs.setSeverity(FacesMessage.SEVERITY_ERROR);
                    FacesContext context = FacesContext.getCurrentInstance();
                    context.addMessage(null, mgs);
                }
            }
        } else {
            ++solansai;
            diemtam = 0;
            traloi = "";
            FacesMessage mgs;

            if (solansai >= 3) {
                RequestContext.getCurrentInstance().execute("endgame();");
                mgs = new FacesMessage("Bạn đã trả lời sai 3 lần hoặc trả lời trùng từ đã đúng 3 lần. RẤT TIẾC");
                statusPlay = false;
                btnmoi = true;
                btndoan = false;
                //////////gọi xuống service lưu vào////////////
                saveLichSu(0);
                ///////////////////
            } else {
                RequestContext.getCurrentInstance().execute("doansai();");
                mgs = new FacesMessage("Không được để trống.");
            }
            mgs.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, mgs);
        }

    }
    public void saveLichSu(int i)
    {
        User use = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
        Lichsu l = new Lichsu();
        l.setCauhoi(cauhoiSerice.getCauhoiById(id));
        l.setTime(new Date());
        l.setTongdiem(diemtong);
        l.setUsers(userSerice.getUserById(use.getId()));
        if(i==0)
            l.setStatus(0);
        else
            if(i==1)
            {
                userSerice.addDiemUser(use, use.getDiem()+diemtong);
                l.setStatus(1);
            }
        else
            {
                userSerice.addDiemUser(use, use.getDiem()+diemtong);
                l.setStatus(2);
            }
        boolean a = lichsuSerice.saveLichsu(l);
       
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
     * @return the datraloi
     */
    public String getDatraloi() {
        return datraloi;
    }

    /**
     * @param datraloi the datraloi to set
     */
    public void setDatraloi(String datraloi) {
        this.datraloi = datraloi;
    }

    /**
     * @return the diemtong
     */
    public int getDiemtong() {
        return diemtong;
    }

    /**
     * @param diemtong the diemtong to set
     */
    public void setDiemtong(int diemtong) {
        this.diemtong = diemtong;
    }

    /**
     * @return the diemtam
     */
    public int getDiemtam() {
        return diemtam;
    }

    /**
     * @param diemtam the diemtam to set
     */
    public void setDiemtam(int diemtam) {
        this.diemtam = diemtam;
    }

    /**
     * @return the solansai
     */
    public int getSolansai() {
        return solansai;
    }

    /**
     * @param solansai the solansai to set
     */
    public void setSolansai(int solansai) {
        this.solansai = solansai;
    }

    /**
     * @return the statusPlay
     */
    public boolean isStatusPlay() {
        return statusPlay;
    }

    /**
     * @param statusPlay the statusPlay to set
     */
    public void setStatusPlay(boolean statusPlay) {
        this.statusPlay = statusPlay;
    }

    /**
     * @return the traloi
     */
    public String getTraloi() {
        return traloi;
    }

    /**
     * @param traloi the traloi to set
     */
    public void setTraloi(String traloi) {
        this.traloi = traloi;
    }

    /**
     * @return the btnmoi
     */
    public boolean isBtnmoi() {
        return btnmoi;
    }

    /**
     * @param btnmoi the btnmoi to set
     */
    public void setBtnmoi(boolean btnmoi) {
        this.btnmoi = btnmoi;
    }

    /**
     * @return the btndoan
     */
    public boolean isBtndoan() {
        return btndoan;
    }

    /**
     * @param btndoan the btndoan to set
     */
    public void setBtndoan(boolean btndoan) {
        this.btndoan = btndoan;
    }

    /**
     * @return the chondoan
     */
    public int getChondoan() {
        return chondoan;
    }

    /**
     * @param chondoan the chondoan to set
     */
    public void setChondoan(int chondoan) {
        this.chondoan = chondoan;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the loaitruot
     */
    public int getLoaitruot() {
        return loaitruot;
    }

    /**
     * @param loaitruot the loaitruot to set
     */
    public void setLoaitruot(int loaitruot) {
        this.loaitruot = loaitruot;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntq.bean;

import com.google.gson.Gson;
import com.ntq.pojo.Cauhoi;
import com.ntq.pojo.Thongke;
import com.ntq.pojo.Lichsu;
import com.ntq.pojo.User;
import com.ntq.service.LichsuService;
import com.ntq.service.ThongkeService;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author songo
 */
@ManagedBean
@Named(value = "thongkeBean")
@SessionScoped
public class ThongkeBean {

    /**
     * Creates a new instance of TVTableBean
     */
    Date dd = new Date();
    private int page = 1;
    private int number = 10;
    private int selectmonth = dd.getMonth()+1;
    private int selectyear = dd.getYear()+1900;
    private int[] listmonth;
    private int[] listyear = new int[50];
    private static ThongkeService thongkeSerice = new ThongkeService();
    private static LichsuService lichsuSerice = new LichsuService();
    public ThongkeBean() {
        //runFirt();
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
    public boolean checkLichSuNext()
    {
        
        try
        {
        Date date1=new SimpleDateFormat("yyyy-MM").parse( selectyear+"-"+selectmonth);
            List<Lichsu> a =  lichsuSerice.getLichsu(number, getPage()+1, date1);
            if(a==null || a.size()==0)
                return false;
            else
                return true;
        }
        catch(Exception ex)
        {
            return false;
        }
    }
    public void runFirt()
    {
        Date d = new Date();

        selectmonth = d.getMonth()+1;
        selectyear = d.getYear()+1900;
        //number = 10;
        page = 1;

        listmonth =new int[d.getMonth()+1];
        for (int i = 1;i<= (d.getMonth()+1); i++) {
            listmonth[i - 1] = i;
        }
        int dem =0;
        for(int i = (d.getYear()+1900)-49; i<=d.getYear()+1900; i++)
        {
            listyear[dem] = i;
            dem++;
        }
    }
    public void setDefault()
    {
        number = 10;
        page = 1;
    }
    public void xemThu()
    {
        if(selectyear == (new Date().getYear()+1900))
        {
            listmonth = new int[new Date().getMonth()+1];
            for (int i = 1;i<= (new Date().getMonth()+1); i++) {
            listmonth[i - 1] = i;
            }
        }
        else
        {
            listmonth = new int[12];
                for (int i = 1; i <= 12; i++) {
                    listmonth[i - 1] = i;
                }
        }
            
    }
    public List<Lichsu> getListCauhoi()
    {
        try
        {
        Date date1=new SimpleDateFormat("yyyy-MM").parse( selectyear+"-"+selectmonth);
            List<Lichsu> a=  lichsuSerice.getLichsu(number, getPage(), date1);
            return a;
        }
        catch(Exception ex)
        {
            return null;
        }
        
    }
    public List<Lichsu> getAllListWithDate()
    {
        try
        {
            Date date1=new SimpleDateFormat("yyyy-MM").parse( selectyear+"-"+selectmonth);
            List<Lichsu> a=  lichsuSerice.getLichsu(date1);
            return a;
        }
        catch(Exception ex)
        {
            return null;
        }
    }
    public String getChart(int loai)
    {
        String days =(selectyear!=0?selectyear: new Date().getYear()+1900)+"-"+(selectmonth!=0?selectmonth: new Date().getMonth()+1);
        try{
            Date date1=new SimpleDateFormat("yyyy-MM").parse(days);
            YearMonth yearMonthObject = YearMonth.of(date1.getYear()+1900,date1.getMonth()+1);
            int daysInMonth = yearMonthObject.lengthOfMonth();
            List<Thongke> list = new ArrayList<>();
            for(int i=1;i<=daysInMonth; i++ )
            {
                String day = days +"-"+i;
                Date date=new SimpleDateFormat("yyyy-MM-dd").parse(day);
                int ngayne = date.getDate();
                int ngaycc = date.getDay();
                int dung=0;
                int sai = 0;
                int doan =0;
                for(int j=0;j<3;j++)
                {
                    int a = thongkeSerice.getLichsu(date, j, loai);
                    if(j==0)
                        sai = a;
                    else
                        if(j==1)
                            dung=a;
                    else
                            doan=a;
                }
                list.add(new Thongke(String.valueOf(i),dung,doan,sai,"","",0,0));
                
            }
            String json = new Gson().toJson(list);
            return json;
        }
        catch(Exception ex)
        {
            return null;
            
        }
    }
    public String getChartUser()
    {
        String days =(selectyear!=0?selectyear: new Date().getYear()+1900)+"-"+(selectmonth!=0?selectmonth: new Date().getMonth()+1);
        try{
            Date date1=new SimpleDateFormat("yyyy-MM").parse(days);
            YearMonth yearMonthObject = YearMonth.of(date1.getYear()+1900,date1.getMonth()+1);
            int daysInMonth = yearMonthObject.lengthOfMonth();
            List<Thongke> list = new ArrayList<>();
            for(int i=1;i<=daysInMonth; i++ )
            {
                String day = days +"-"+i;
                Date date=new SimpleDateFormat("yyyy-MM-dd").parse(day);
                String userCount ="";
                String userPoint = "";
                Long count =0L;
                Long point =0L;
                for(int j=1;j<3;j++)
                {
                    Object[] a = thongkeSerice.getUserThongKe(date, j);
                    
                    if(j==1)
                    {
                        if(a==null)
                            userCount ="Không có";
                        else
                        {
                            User use =(User) a[0];
                            count = (Long)a[1];
                            userCount = use.getUsername();
                        }
                    }
                    else
                    {
                        if(a==null)
                            userPoint ="Không có";
                        else
                        {
                            User use =(User) a[0];
                            point = (Long) a[1];
                            userPoint = use.getUsername();
                        }
                    }
                }
                list.add(new Thongke(String.valueOf(i),0,0,0,userCount,userPoint,count.intValue(),point.intValue()));
                
            }
            String json = new Gson().toJson(list);
            return json;
        }
        catch(Exception ex)
        {
            return null;
            
        }
    }
    public String getTopUser(int status)  // status 1: câu hỏi. 2: điểm
    {
        String days =(selectyear!=0?selectyear: new Date().getYear()+1900)+"-"+(selectmonth!=0?selectmonth: new Date().getMonth()+1);
        try{
            Date date1 = new SimpleDateFormat("yyyy-MM").parse(days);

            Object[] a = thongkeSerice.getTopUser(date1, status);
              if (a == null) {
                return "Không có";
            } else {
                  User use = (User) a[0];
                  return use.getUsername() + " (" + (Long) a[1] + ")";
            }

        }
        catch(Exception ex)
        {
            return null;
            
        }
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
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param aNumber the number to set
     */
    public void setNumber(int aNumber) {
        number = aNumber;
    }


    /**
     * @return the selectmonth
     */
    public int getSelectmonth() {
        return selectmonth;
    }

    /**
     * @param selectmonth the selectmonth to set
     */
    public void setSelectmonth(int selectmonth) {
        this.selectmonth = selectmonth;
    }

    /**
     * @return the selectyear
     */
    public int getSelectyear() {
        return selectyear;
    }

    /**
     * @param selectyear the selectyear to set
     */
    public void setSelectyear(int selectyear) {
        this.selectyear = selectyear;
    }

    /**
     * @return the listmonth
     */
    public int[] getListmonth() {
        return listmonth;
    }

    /**
     * @param listmonth the listmonth to set
     */
    public void setListmonth(int[] listmonth) {
        this.listmonth = listmonth;
    }

    /**
     * @return the listyear
     */
    public int[] getListyear() {
        return listyear;
    }

    /**
     * @param listyear the listyear to set
     */
    public void setListyear(int[] listyear) {
        this.listyear = listyear;
    }
    
}

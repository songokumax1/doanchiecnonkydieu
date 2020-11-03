/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntq.pojo;

/**
 *
 * @author songo
 */
public class Thongke {
    private String day;
    private int dung;
    private int doan;
    private int sai;
    private int count;
    private int point;
    private String userCount;
    private String userPoint;
    public Thongke(String day,int dung,int doan,int sai, String usercount, String userPoint, int count, int point)
    {
        this.day = day;
        this.dung = dung;
        this.doan = doan;
        this.sai = sai;
        this.userCount = usercount;
        this.userPoint = userPoint;
        this.count = count;
        this.point = point;
    }

    

    /**
     * @return the dung
     */
    public int getDung() {
        return dung;
    }

    /**
     * @param dung the dung to set
     */
    public void setDung(int dung) {
        this.dung = dung;
    }

    /**
     * @return the doan
     */
    public int getDoan() {
        return doan;
    }

    /**
     * @param doan the doan to set
     */
    public void setDoan(int doan) {
        this.doan = doan;
    }

    /**
     * @return the sai
     */
    public int getSai() {
        return sai;
    }

    /**
     * @param sai the sai to set
     */
    public void setSai(int sai) {
        this.sai = sai;
    }

    /**
     * @return the day
     */
    public String getDay() {
        return day;
    }

    /**
     * @param day the day to set
     */
    public void setDay(String day) {
        this.day = day;
    }

    /**
     * @return the userCount
     */
    public String getUserCount() {
        return userCount;
    }

    /**
     * @param userCount the userCount to set
     */
    public void setUserCount(String userCount) {
        this.userCount = userCount;
    }

    /**
     * @return the userPoint
     */
    public String getUserPoint() {
        return userPoint;
    }

    /**
     * @param userPoint the userPoint to set
     */
    public void setUserPoint(String userPoint) {
        this.userPoint = userPoint;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @return the point
     */
    public int getPoint() {
        return point;
    }

    /**
     * @param point the point to set
     */
    public void setPoint(int point) {
        this.point = point;
    }
}

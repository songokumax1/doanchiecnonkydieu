/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntq.pojo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author songo
 */
@Entity
@Table(name = "lichsu")
public class Lichsu implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int tongdiem;
    private Date time;
    private int status;
    @ManyToOne
    @JoinColumn(name ="user_id")
    private User users;
    @ManyToOne
    @JoinColumn(name ="cauhoi_id")
    private Cauhoi cauhoi;

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
     * @return the tongdiem
     */
    public int getTongdiem() {
        return tongdiem;
    }

    /**
     * @param tongdiem the tongdiem to set
     */
    public void setTongdiem(int tongdiem) {
        this.tongdiem = tongdiem;
    }

    /**
     * @return the time
     */
    public Date getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * @return the users
     */
    public User getUsers() {
        return users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(User users) {
        this.users = users;
    }

    /**
     * @return the cauhoi
     */
    public Cauhoi getCauhoi() {
        return cauhoi;
    }

    /**
     * @param cauhoi the cauhoi to set
     */
    public void setCauhoi(Cauhoi cauhoi) {
        this.cauhoi = cauhoi;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }
    
}

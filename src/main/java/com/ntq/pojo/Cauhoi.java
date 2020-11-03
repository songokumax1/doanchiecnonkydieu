/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntq.pojo;

import java.io.Serializable;
import java.util.Set;
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
@Table(name = "cauhoi")
public class Cauhoi implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String noidung;
    private String dapan;
    @ManyToOne
    @JoinColumn(name ="loaicauhoi_id")
    private Loaicauhoi loaicauhoi;
    private int band;
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

    
}

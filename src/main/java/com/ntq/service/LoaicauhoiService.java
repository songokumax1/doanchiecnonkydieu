/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntq.service;

import com.ntq.conf.HibernateUtil;
import com.ntq.pojo.Cauhoi;
import com.ntq.pojo.Loaicauhoi;
import java.util.Date;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author songo
 */
public class LoaicauhoiService {
    private final static SessionFactory FACTORY = HibernateUtil.getSessionFactory();
    
    public List<Loaicauhoi> getLoaicauhoi(int i)
    {
        try(Session session = FACTORY.openSession())
        {
            CriteriaBuilder b = session.getCriteriaBuilder();
            CriteriaQuery<Loaicauhoi> q = b.createQuery(Loaicauhoi.class);
            Root<Loaicauhoi> root = q.from(Loaicauhoi.class);
            q.select(root);
            if(i==0)
                q.where(b.equal(root.get("band").as(Integer.class), 0));
            return session.createQuery(q).getResultList();
        }
        catch(Exception ex)
        {
            return null;
        }
    }
    public List<Loaicauhoi> getLoaicauhoi(int number, int page ,String keyword, String typesearch, int orderby, boolean typesort)
    {
        try(Session session = FACTORY.openSession())
        {
            CriteriaBuilder b = session.getCriteriaBuilder();
            CriteriaQuery<Loaicauhoi> q = b.createQuery(Loaicauhoi.class);
            Root<Loaicauhoi> root = q.from(Loaicauhoi.class);
            q.select(root);
            if(orderby!= 0)
            {
                switch(orderby)
                {
                    case 1:
                        if(!typesort)
                            q.orderBy(b.asc(root.get("id").as(Integer.class)));
                        else
                            q.orderBy(b.desc(root.get("id").as(Integer.class)));
                        break;
                    case 2:
                        if (!typesort) {
                            q.orderBy(b.asc(root.get("name").as(String.class)));
                        } else {
                            q.orderBy(b.desc(root.get("name").as(String.class)));
                        }
                        break;
                    case 3:
                        if (!typesort) {
                            q.orderBy(b.asc(root.get("date").as(Date.class)));
                        } else {
                            q.orderBy(b.desc(root.get("date").as(Date.class)));
                        }
                        break;
                    case 4:
                        if (!typesort) {
                            q.orderBy(b.asc(root.get("band").as(Integer.class)));
                        } else {
                            q.orderBy(b.desc(root.get("band").as(Integer.class)));
                        }
                        break;
                }
                
            }
            Predicate p;
            if(keyword != null && keyword != "")
            {
                if(typesearch.equals("ten"))
                {
                    p = b.like(root.get("name").as(String.class), "%" + keyword + "%");
                    q.where(p);
                }
            }
            return session.createQuery(q).setFirstResult(page==1?0:(number*(page-1))).setMaxResults(number).getResultList();
        }
        catch(Exception ex)
        {
            return null;
        }
    }
    public Loaicauhoi getLoaicauhoiById(int id)
    {
        try (Session session = FACTORY.openSession())
        {
           return session.get(Loaicauhoi.class, id);
        }
    }
    public boolean addOrSaveLoaicauhoi(Loaicauhoi l)
    {
        try (Session session = FACTORY.openSession())
        {
            try
            {
                session.getTransaction().begin();
                if(l.getId()!=0)
                {
                    CriteriaBuilder b = session.getCriteriaBuilder();
                    CriteriaUpdate<Cauhoi> update = b.createCriteriaUpdate(Cauhoi.class);
                    Root<Cauhoi> root = update.from(Cauhoi.class);
                    update.set("band",l.getBand());
                    update.where(b.equal(root.get("loaicauhoi").as(Loaicauhoi.class), l));
                    session.createQuery(update).executeUpdate();
                }
                
                session.saveOrUpdate(l);
                session.getTransaction().commit();
                return true;
            }
            catch(Exception ex)
            {
                session.getTransaction().rollback();
            }
        }
        return false;
    }
    public boolean add(Loaicauhoi l)
    {
        try (Session session = FACTORY.openSession())
        {
            try
            {
                session.getTransaction().begin();
                session.save(l);
                session.getTransaction().commit();
                return true;
            }
            catch(Exception ex)
            {
                session.getTransaction().rollback();
            }
        }
        return false;
    }
    
}

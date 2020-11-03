/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntq.service;

import com.ntq.conf.HibernateUtil;
import com.ntq.pojo.Lichsu;
import com.ntq.pojo.User;
import com.ntq.pojo.Cauhoi;
import java.util.Date;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author songo
 */
public class LichsuService {
    private final static SessionFactory FACTORY = HibernateUtil.getSessionFactory();
    
    public List<Lichsu> getLichsu()
    {
        try(Session session = FACTORY.openSession())
        {
            CriteriaBuilder b = session.getCriteriaBuilder();
            CriteriaQuery<Lichsu> q = b.createQuery(Lichsu.class);
            Root<Lichsu> root = q.from(Lichsu.class);
            q.select(root);
            
            return session.createQuery(q).getResultList();
        }
        catch(Exception ex)
        {
            return null;
        }
    }
    public List<Lichsu> getLichsuView()
    {
        try(Session session = FACTORY.openSession())
        {
            CriteriaBuilder b = session.getCriteriaBuilder();
            CriteriaQuery<Lichsu> q = b.createQuery(Lichsu.class);
            Root<Lichsu> root = q.from(Lichsu.class);
            q.select(root);
            q.orderBy(b.desc(root.get("id").as(Integer.class)));
            return session.createQuery(q).setFirstResult(0).setMaxResults(10).getResultList();
        }
        catch(Exception ex)
        {
            return null;
        }
    }
    public List<Lichsu> getLichsu(int number, int page, String keyword, String typesearch, int orderby, boolean typesort)
    {
        try(Session session = FACTORY.openSession())
        {
            CriteriaBuilder b = session.getCriteriaBuilder();
            CriteriaQuery<Lichsu> q = b.createQuery(Lichsu.class);
            Root<Lichsu> root = q.from(Lichsu.class);
            Join<Lichsu, Cauhoi> ch = root.join("cauhoi", JoinType.INNER);
            Join<Lichsu, User> u = root.join("users", JoinType.INNER);
            q.select(root);
            if (orderby != 0) {
                switch (orderby) {
                    case 1:
                        if (!typesort) {
                            q.orderBy(b.asc(root.get("id").as(Integer.class)));
                        } else {
                            q.orderBy(b.desc(root.get("id").as(Integer.class)));
                        }
                        break;
                    case 2:
                        if (!typesort) {
                            q.orderBy(b.asc(u.get("username").as(String.class)));
                        } else {
                            q.orderBy(b.desc(u.get("username").as(String.class)));
                        }
                        break;
                    case 3:
                        if (!typesort) {
                            q.orderBy(b.asc(ch.get("noidung").as(String.class)));
                        } else {
                            q.orderBy(b.desc(ch.get("noidung").as(String.class)));
                        }
                        break;
                    case 4:
                        if (!typesort) {
                            q.orderBy(b.asc(root.get("status").as(String.class)));
                        } else {
                            q.orderBy(b.desc(root.get("status").as(String.class)));
                        }
                        break;
                    case 5:
                        if (!typesort) {
                            q.orderBy(b.asc(root.get("tongdiem").as(Date.class)));
                        } else {
                            q.orderBy(b.desc(root.get("tongdiem").as(Date.class)));
                        }
                        break;
                    case 6:
                        if (!typesort) {
                            q.orderBy(b.asc(root.get("time").as(Integer.class)));
                        } else {
                            q.orderBy(b.desc(root.get("time").as(Integer.class)));
                        }
                        break;
                }
                
            }
            Predicate p;
                if (keyword != null && keyword != "") {
                    if (typesearch.equals("user")) {
                        p = b.like(u.get("username").as(String.class), "%" + keyword + "%");
                    } else {
                        p = b.like(ch.get("noidung").as(String.class), "%" + keyword + "%");
                    }
                    q.where(p);
                }
            return session.createQuery(q).setFirstResult(page==1?0:(number*(page-1))).setMaxResults(number).getResultList();
        }
        catch(Exception ex)
        {
            return null;
        }
    }
    public List<Lichsu> getLichsu(int number, int page, Date date)
    {
        try(Session session = FACTORY.openSession())
        {
            CriteriaBuilder b = session.getCriteriaBuilder();
            CriteriaQuery<Lichsu> q = b.createQuery(Lichsu.class);
            Root<Lichsu> root = q.from(Lichsu.class);
            q.select(root);
            q.where(b.and(b.equal(b.function("YEAR", Integer.class, root.get("time") ), (date.getYear()+1900)),
                b.equal(b.function("MONTH", Integer.class, root.get("time")),(date.getMonth()+1))));
            return session.createQuery(q).setFirstResult(page==1?0:(number*(page-1))).setMaxResults(number).getResultList();
        }
        catch(Exception ex)
        {
            return null;
        }
    }
    public List<Lichsu> getLichsuUser(int number, int page,User u, int type)
    {
        try(Session session = FACTORY.openSession())
        {
            CriteriaBuilder b = session.getCriteriaBuilder();
            CriteriaQuery<Lichsu> q = b.createQuery(Lichsu.class);
            Root<Lichsu> root = q.from(Lichsu.class);
            Join<Lichsu, User> user = root.join("users",JoinType.INNER);
            q.select(root);
            q.orderBy(b.desc(root.get("id").as(Integer.class)));
            q.where(b.equal(user.as(User.class), u));
            if(type==0) //get có page number
                return session.createQuery(q).setFirstResult(page==1?0:(number*(page-1))).setMaxResults(number).getResultList();
            else // get tất cả
                return session.createQuery(q).getResultList();
        }
        catch(Exception ex)
        {
            return null;
        }
    }
    public List<Lichsu> getLichsu(Date date)
    {
        try(Session session = FACTORY.openSession())
        {
            CriteriaBuilder b = session.getCriteriaBuilder();
            CriteriaQuery<Lichsu> q = b.createQuery(Lichsu.class);
            Root<Lichsu> root = q.from(Lichsu.class);
            q.select(root);
            q.where(b.and(b.equal(b.function("YEAR", Integer.class, root.get("time") ), (date.getYear()+1900)),
                b.equal(b.function("MONTH", Integer.class, root.get("time")),(date.getMonth()+1))));
            return session.createQuery(q).getResultList();
        }
        catch(Exception ex)
        {
            return null;
        }
    }
    public boolean saveLichsu(Lichsu l)
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

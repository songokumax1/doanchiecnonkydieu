/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntq.service;

import com.ntq.conf.HibernateUtil;
import com.ntq.pojo.Cauhoi;
import com.ntq.pojo.Lichsu;
import com.ntq.pojo.Loaicauhoi;
import com.ntq.pojo.User;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author songo
 */
public class CauhoiService {
    private final static SessionFactory FACTORY = HibernateUtil.getSessionFactory();
    
    public List<Cauhoi> getCauhoi()
    {
        try(Session session = FACTORY.openSession())
        {
            CriteriaBuilder b = session.getCriteriaBuilder();
            CriteriaQuery<Cauhoi> q = b.createQuery(Cauhoi.class);
            Root<Cauhoi> root = q.from(Cauhoi.class);
            q.select(root);
            
            return session.createQuery(q).getResultList();
        }
        catch(Exception ex)
        {
            return null;
        }
    }
    public List<Cauhoi> getCauhoi(int number, int page ,String keyword, String typesearch, int orderby, boolean typesort)
    {
        try(Session session = FACTORY.openSession())
        {
            CriteriaBuilder b = session.getCriteriaBuilder();
            CriteriaQuery<Cauhoi> q = b.createQuery(Cauhoi.class);
            Root<Cauhoi> root = q.from(Cauhoi.class);
            Join<Cauhoi, Loaicauhoi> l = root.join("loaicauhoi", JoinType.INNER);
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
                            q.orderBy(b.asc(root.get("noidung").as(String.class)));
                        } else {
                            q.orderBy(b.desc(root.get("noidung").as(String.class)));
                        }
                        break;
                    case 3:
                        if (!typesort) {
                            q.orderBy(b.asc(root.get("dapan").as(String.class)));
                        } else {
                            q.orderBy(b.desc(root.get("dapan").as(String.class)));
                        }
                        break;
                    case 4:
                        if (!typesort) {
                            q.orderBy(b.asc(root.get("loaicauhoi").as(Loaicauhoi.class)));
                        } else {
                            q.orderBy(b.desc(root.get("loaicauhoi").as(Loaicauhoi.class)));
                        }
                        break;
                    case 5:
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
                if(typesearch.equals("noidung"))
                p = b.like(root.get("noidung").as(String.class), "%" + keyword + "%");
            else
                if(typesearch.equals("dapan"))
                    p = b.like(root.get("dapan").as(String.class), "%" + keyword + "%");
            else
                    p = b.like(l.get("name").as(String.class), "%" + keyword + "%");
            q.where(p);
            }
            return session.createQuery(q).setFirstResult(page==1?0:(number*(page-1))).setMaxResults(number).getResultList();
        }
        catch(Exception ex)
        {
            return null;
        }
    }
    public boolean addOrSaveCauhoi(Cauhoi l)
    {
        try (Session session = FACTORY.openSession())
        {
            
            try
            {
                session.getTransaction().begin();
                
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
    public List<Cauhoi> getCauHoiChuan(String username, Loaicauhoi lch)
    {
        try(Session session = FACTORY.openSession())
        {
            CriteriaBuilder b = FACTORY.getCriteriaBuilder();
            CriteriaQuery<Cauhoi> q = b.createQuery(Cauhoi.class);
            Root<Cauhoi> c = q.from(Cauhoi.class);
            
            Subquery<Cauhoi> subquery = q.subquery(Cauhoi.class);
            Root<Lichsu> l = subquery.from(Lichsu.class);
            Join<Lichsu,User> u = l.join("users", JoinType.INNER);
            subquery.select(l.get("cauhoi").as(Cauhoi.class));
            subquery.where(b.and(b.equal(u.get("username").as(String.class), username),
                    b.or(b.equal(l.get("status").as(Integer.class), 1),
                    b.equal(l.get("status").as(Integer.class), 2))));
//            Join<Lichsu,Cauhoi> root = l.join("cauhoi", JoinType.INNER);
//            Join<Lichsu,User> u = l.join("users", JoinType.INNER);
            q.select(c);
            q.where(b.and(b.not(c.in(subquery)),
                    b.equal(c.get("loaicauhoi").as(Loaicauhoi.class), lch),
                    b.equal(c.get("band").as(Integer.class), 0)));
            return session.createQuery(q).getResultList();
        }
        catch(Exception ex)
        {
            return null;
        }
    }
    public Cauhoi getCauhoiById(int id)
    {
        try(Session session = FACTORY.openSession())
        {
            
            return session.get(Cauhoi.class, id);
        }
        catch(Exception ex)
        {
            return null;
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntq.service;

import com.ntq.conf.HibernateUtil;
import com.ntq.pojo.Lichsu;
import com.ntq.pojo.User;
import java.util.Date;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

/**
 *
 * @author songo
 */
public class ThongkeService {
    private final static SessionFactory FACTORY = HibernateUtil.getSessionFactory();
    
    public int getLichsu(Date d, int status, int loai)
    {
        try(Session session = FACTORY.openSession())
        {
            CriteriaBuilder b = session.getCriteriaBuilder();
            CriteriaQuery<Long> q = b.createQuery(Long.class);
            Root<Lichsu> root = q.from(Lichsu.class);
            if(loai==1)
                q.select(b.sum(root.get("tongdiem").as(Long.class)));
            else
                q.select(b.count(root));
            q.where(b.and(b.equal(b.function("YEAR", Integer.class, root.get("time") ), (d.getYear()+1900)),
                b.equal(b.function("MONTH", Integer.class, root.get("time")),(d.getMonth()+1)),
                b.equal(b.function("DAY", Integer.class, root.get("time")),d.getDate()),
                b.equal(root.get("status").as(Integer.class), status)));
            Long a= session.createQuery(q).getSingleResult();
            return a==null?0:a.intValue();
        }
        catch(Exception ex)
        {
            return 0;
        }
    }
    public Object[] getUserThongKe(Date d, int status)
    {
        try(Session session = FACTORY.openSession())
        {
//            CriteriaBuilder b = session.getCriteriaBuilder();
//            CriteriaQuery<User> q = b.createQuery(User.class);
//            Root<Lichsu> l = q.from(Lichsu.class);
//            Join<Lichsu,User> root = l.join("users", JoinType.INNER);
//            q.select(root);
//            if(status==2)
//                q.orderBy(b.desc(l.get("tongdiem")));
//            else
//                q.orderBy(b.desc(b.count(l)));
//            q.groupBy(l.get("users"));
//            q.where(b.and(b.equal(b.function("YEAR", Integer.class, l.get("time") ), (d.getYear()+1900)),
//                b.equal(b.function("MONTH", Integer.class, l.get("time")),(d.getMonth()+1)),
//                b.equal(b.function("DAY", Integer.class, l.get("time")),d.getDate()),
//                b.or(b.equal(l.get("status").as(Integer.class), 1), b.equal(l.get("status").as(Integer.class), 2) )));
//            User a= session.createQuery(q).setMaxResults(1).getResultList().stream().findFirst().orElse(null);
//            return a==null?null:a;
            
            CriteriaBuilder b = session.getCriteriaBuilder();
            CriteriaQuery<Object[]> q = b.createQuery(Object[].class);
            Root<Lichsu> l = q.from(Lichsu.class);
            Join<Lichsu,User> root = l.join("users", JoinType.INNER);
            if(status==2)
            {
                q.multiselect(root, b.sum(l.get("tongdiem").as(Integer.class)));
                q.orderBy(b.desc(l.get("tongdiem")));
            }
            else
            {
                q.multiselect(root, b.count(l));
                q.orderBy(b.desc(b.count(l)));
            }
            q.groupBy(l.get("users"));
            q.where(b.and(b.equal(b.function("YEAR", Integer.class, l.get("time") ), (d.getYear()+1900)),
                b.equal(b.function("MONTH", Integer.class, l.get("time")),(d.getMonth()+1)),
                b.equal(b.function("DAY", Integer.class, l.get("time")),d.getDate()),
                b.or(b.equal(l.get("status").as(Integer.class), 1), b.equal(l.get("status").as(Integer.class), 2) )));
            Query<Object[]> query = session.createQuery(q);
            Object[] a= query.getResultList().stream().findFirst().orElse(null);
            return a;
        }
        catch(Exception ex)
        {
            return null;
        }
    }
    public Object[] getTopUser(Date d, int status)
    {
        try(Session session = FACTORY.openSession())
        {
            
            CriteriaBuilder b = session.getCriteriaBuilder();
            CriteriaQuery<Object[]> q = b.createQuery(Object[].class);
            Root<Lichsu> l = q.from(Lichsu.class);
            Join<Lichsu,User> root = l.join("users", JoinType.INNER);
            if(status==2)
            {
                q.multiselect(root, b.sum(l.get("tongdiem").as(Integer.class)));
                q.orderBy(b.desc(l.get("tongdiem")));
            }
            else
            {
                q.multiselect(root, b.count(l));
                q.orderBy(b.desc(b.count(l)));
            }
            q.groupBy(l.get("users"));
            q.where(b.and(b.equal(b.function("YEAR", Integer.class, l.get("time") ), (d.getYear()+1900)),
                b.equal(b.function("MONTH", Integer.class, l.get("time")),(d.getMonth()+1)),
                b.or(b.equal(l.get("status").as(Integer.class), 1), b.equal(l.get("status").as(Integer.class), 2) )));
            Query<Object[]> query = session.createQuery(q);
            Object[] a= query.getResultList().stream().findFirst().orElse(null);
            return a;
        }
        catch(Exception ex)
        {
            return null;
        }
    }
}

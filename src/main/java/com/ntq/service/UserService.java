/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntq.service;

import com.ntq.conf.HibernateUtil;
import com.ntq.pojo.User;
import java.util.Date;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author songo
 */
public class UserService {
    private final static SessionFactory FACTORY = HibernateUtil.getSessionFactory();
    
    public boolean saveOrUpdate(User u)
    {
        try (Session session = FACTORY.openSession())
        {
            try
            {
                session.getTransaction().begin();
//                u.setPass(DigestUtils.md5Hex(u.getPass()));
//                u.setDiem(0);
//                u.setRole("USER");
//                Date d = new Date();
//                u.setRegdate(d);
                session.saveOrUpdate(u);
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
    public User login(String username, String pass)
    {
        pass = DigestUtils.md5Hex(pass);
        try(Session session = FACTORY.openSession())
        {
            CriteriaBuilder b = session.getCriteriaBuilder();
            CriteriaQuery<User> q = b.createQuery(User.class);
            Root<User> root = q.from(User.class);
            q.select(root);
            q.where(b.and(b.equal(root.get("username").as(String.class), username),
                   b.equal(root.get("pass").as(String.class), pass)));
            return session.createQuery(q).getSingleResult();
        }
        catch(Exception ex)
        {
            return null;
        }
    }
    public boolean checkUser(String username, String email)
    {
        try(Session session = FACTORY.openSession())
        {
            CriteriaBuilder b = session.getCriteriaBuilder();
            CriteriaQuery<User> q = b.createQuery(User.class);
            Root<User> root = q.from(User.class);
            q.select(root);
            q.where(b.or(b.equal(root.get("username").as(String.class), username),
                   b.equal(root.get("email").as(String.class), email)));
            if(session.createQuery(q).getResultList().size()== 0)
                return false; // Không tồn tại trong db
            else
                return true;
        }
        catch(Exception ex)
        {
            return true;
        }
    }
    public List<User> getUser(int number, int page, String keyword, String typesearch, int orderby, boolean typesort)
    {
        try(Session session = FACTORY.openSession())
        {
            CriteriaBuilder b = session.getCriteriaBuilder();
            CriteriaQuery<User> q = b.createQuery(User.class);
            Root<User> root = q.from(User.class);
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
                            q.orderBy(b.asc(root.get("fullname").as(String.class)));
                        } else {
                            q.orderBy(b.desc(root.get("fullname").as(String.class)));
                        }
                        break;
                    case 3:
                        if (!typesort) {
                            q.orderBy(b.asc(root.get("username").as(String.class)));
                        } else {
                            q.orderBy(b.desc(root.get("username").as(String.class)));
                        }
                        break;
                    case 4:
                        if (!typesort) {
                            q.orderBy(b.asc(root.get("email").as(String.class)));
                        } else {
                            q.orderBy(b.desc(root.get("email").as(String.class)));
                        }
                        break;
                    case 5:
                        if (!typesort) {
                            q.orderBy(b.asc(root.get("regdate").as(Date.class)));
                        } else {
                            q.orderBy(b.desc(root.get("regdate").as(Date.class)));
                        }
                        break;
                    case 6:
                        if (!typesort) {
                            q.orderBy(b.asc(root.get("diem").as(Integer.class)));
                        } else {
                            q.orderBy(b.desc(root.get("diem").as(Integer.class)));
                        }
                        break;
                    case 7:
                        if (!typesort) {
                            q.orderBy(b.asc(root.get("role").as(String.class)));
                        } else {
                            q.orderBy(b.desc(root.get("role").as(String.class)));
                        }
                        break;
                    case 8:
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
                if(typesearch.equals("fullname"))
                p = b.like(root.get("fullname").as(String.class), "%" + keyword + "%");
            else
                if(typesearch.equals("username"))
                    p = b.like(root.get("username").as(String.class), "%" + keyword + "%");
            else
                    p = b.like(root.get("email").as(String.class), "%" + keyword + "%");
            q.where(p);
            }
            
            return session.createQuery(q).setFirstResult(page==1?0:(number*(page-1))).setMaxResults(number).getResultList();
        }
    }
    public List<User> getListUser()
    {
        try(Session session = FACTORY.openSession())
        {
            CriteriaBuilder b = session.getCriteriaBuilder();
            CriteriaQuery<User> q = b.createQuery(User.class);
            Root<User> root = q.from(User.class);
            q.select(root);
            return session.createQuery(q).getResultList();
        }
    }
    public List<User> getListRankUser()
    {
        try(Session session = FACTORY.openSession())
        {
            CriteriaBuilder b = session.getCriteriaBuilder();
            CriteriaQuery<User> q = b.createQuery(User.class);
            Root<User> root = q.from(User.class);
            q.select(root);
            q.orderBy(b.desc(root.get("diem").as(Integer.class)));
            return session.createQuery(q).setMaxResults(10).getResultList();
        }
    }
    public boolean updateUser(User u)
    {
        try(Session session = FACTORY.openSession())
        {
            session.getTransaction().begin();
            CriteriaBuilder b = session.getCriteriaBuilder();
            CriteriaUpdate<User> up = b.createCriteriaUpdate(User.class);
            Root<User> root = up.from(User.class);
            up.set(root.get("diem"), u.getDiem());
            up.set(root.get("role"), u.getRole());
            up.set(root.get("band"), u.getBand());
            up.where(b.equal(root.get("id"), u.getId()));
            int kq = session.createQuery(up).executeUpdate();
            session.getTransaction().commit();
            if(kq!=0)
                return true;
            else
                return false;
               
        }
        catch(Exception ex)
        {
            return false;
        }
    }
    public User getUserById(int id)
    {
        try(Session session = FACTORY.openSession())
        {
            
            return session.get(User.class, id);
        }
        catch(Exception ex)
        {
            return null;
        }
    }
    public boolean addDiemUser(User u, int diem)
    {
        try(Session session = FACTORY.openSession())
        {
            session.getTransaction().begin();
            CriteriaBuilder b = session.getCriteriaBuilder();
            CriteriaUpdate<User> up = b.createCriteriaUpdate(User.class);
            Root<User> root = up.from(User.class);
            up.set(root.get("diem"),diem);
            up.where(b.equal(root.get("id"), u.getId()));
            int kq = session.createQuery(up).executeUpdate();
            session.getTransaction().commit();
            if(kq!=0)
                return true;
            else
                return false;
               
        }
        catch(Exception ex)
        {
            return false;
        }
    }
//    public boolean DeleteUser(int id)
//    {
//        try(Session session = FACTORY.openSession())
//        {
//            session.getTransaction().begin();
//            CriteriaBuilder b = session.getCriteriaBuilder();
//            CriteriaDelete<User> de = b.createCriteriaDelete(User.class);
//            Root<User> root = de.from(User.class);
//            de.where(b.equal(root.get("id"), id));
//            int kq = session.createQuery(de).executeUpdate();
//            session.getTransaction().commit();
//            if(kq!=0)
//                return true;
//            else
//                return false;
//        }
//        catch(Exception ex)
//        {
//            return false;
//        }
//    }
    
}

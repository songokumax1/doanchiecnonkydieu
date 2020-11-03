/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntq.conf;

import com.ntq.pojo.Cauhoi;
import com.ntq.pojo.Lichsu;
import com.ntq.pojo.Loaicauhoi;
import com.ntq.pojo.User;
import java.util.Properties;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

/**
 *
 * @author songo
 */
public class HibernateUtil {
    private static final SessionFactory FACTORY;
    static {
        Configuration conf = new Configuration();
        Properties pro = new Properties();
        pro.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
        pro.put(Environment.DRIVER, "com.mysql.jdbc.Driver");
        pro.put(Environment.URL, "jdbc:mysql://localhost/doandb ");
        pro.put(Environment.USER, "root");
        pro.put(Environment.PASS, "123456");
        
        conf.setProperties(pro);
        conf.addAnnotatedClass(User.class);
        conf.addAnnotatedClass(Loaicauhoi.class);
        conf.addAnnotatedClass(Cauhoi.class);
        conf.addAnnotatedClass(Lichsu.class);
        ServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(conf.getProperties()).build();
        
        FACTORY = conf.buildSessionFactory(registry);
    }
    public static SessionFactory getSessionFactory()
    {
        return FACTORY;
    }
    
}

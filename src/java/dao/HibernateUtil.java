/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 *
 * @author Korisnik
 */
public class HibernateUtil {
    
    private static SessionFactory sessionFactory;
    private static ServiceRegistry serviceRegistry;
    private static StandardServiceRegistryBuilder standartServiceRegistryBuilder;

    static {
        //    java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        Configuration configuration = new Configuration().configure();
        standartServiceRegistryBuilder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        serviceRegistry = standartServiceRegistryBuilder.build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    public static Session openSession() {
        return sessionFactory.openSession();
    }

    public static void closeAll() {
        sessionFactory.close();
        standartServiceRegistryBuilder.destroy(serviceRegistry);
    }
    
}

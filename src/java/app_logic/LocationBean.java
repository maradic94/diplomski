/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app_logic;

import dao.HibernateUtil;
import entity.Location;
import java.util.List;
import javax.ejb.Stateless;
import org.hibernate.Session;

/**
 *
 * @author Korisnik
 */
@Stateless
public class LocationBean implements LocationBeanRemote {

    @Override
    public List<Location> getAllLocations() throws Exception {
        
        List<Location> allLocations = null;
        Session session = HibernateUtil.openSession();
        session.beginTransaction();
        try {
            allLocations = (List<Location>) session.getNamedQuery("allLocations").list();
            session.getTransaction().commit();
        } finally {
            session.close();
        }
        
        return allLocations;
    }

    
}

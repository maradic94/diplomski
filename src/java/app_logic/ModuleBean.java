/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app_logic;

import dao.HibernateUtil;
import entity.Module;
import java.util.List;
import javax.ejb.Stateless;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Korisnik
 */
@Stateless
public class ModuleBean implements ModuleBeanRemote {

    @Override
    public List<Module> getAllModules() {
        List<Module> allModules = null;
        Session session = HibernateUtil.openSession();
        session.beginTransaction();
        allModules = (List<Module>) session.getNamedQuery("allModules").list();
        session.getTransaction().commit();
        session.close();
        return allModules;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public List<Module> getModuleByName(String studentModule) {
        Session session = HibernateUtil.openSession();
        session.beginTransaction();

        Query query = session.getNamedQuery("getModuleByName");
        query.setParameter("modulename", studentModule);
        List<Module> modules = query.list();
        
        return modules;
    }
}

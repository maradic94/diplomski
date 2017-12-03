/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app_logic;

import dao.HibernateUtil;
import entity.User;
import java.util.List;
import javax.ejb.Stateless;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Korisnik
 */
@Stateless
public class UserBean implements UserBeanLocal {

    public List<User> userLogin(String username, String pass) throws Exception
    {
        List<User> users = null;
        
        Session session = HibernateUtil.openSession();
        session.beginTransaction();
        try{
            Query query = session.getNamedQuery("findUserByNamePass");
            query.setParameter("userName", username);
            query.setParameter("userPass", pass);
            users = query.list();
            session.getTransaction().commit();
            
            System.out.println("Korisnik: "+users.get(0));
        }
        catch(Exception ex)
        {
            throw ex;
        }
         finally {
            session.close();
        }
        
        return users;
    }

    @Override
    public void updateUser(User user) throws Exception {
        
        Session session = HibernateUtil.openSession();
        session.beginTransaction();
        try {
            session.update(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            throw e;

        } finally {
            session.close();
        }
    }

    @Override
    public List<User> findByUserNameAndPass(String username, String pass) throws Exception {
        
        List<User> users = null;
        Session session = HibernateUtil.openSession();
            session.beginTransaction();
            try {
                Query query = session.getNamedQuery("findUserByNamePass");
                query.setParameter("userName", username);
                query.setParameter("userPass", pass);
                users = query.list();
                session.getTransaction().commit();
            } catch (Exception e) {
                throw e;
            } finally {
                session.close();
            }
            
            return users;
    }

    @Override
    public void saveUser(User user) throws Exception {
        user.setIsDeactivated(0);
        user.setIsApproved(0);
        Session session = HibernateUtil.openSession();
        session.beginTransaction();
        try {
            session.save(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            throw e;

        } finally {
            session.close();
        }
    }

    @Override
    public List<User> getUserRequests() throws Exception {
        
        List<User> userRequests = null;
        Session session = HibernateUtil.openSession();
        session.beginTransaction();
        try {
            Query query = session.getNamedQuery("findNotApprovedUsers");
            userRequests = query.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
        
        return userRequests;
    }

    @Override
    public List<User> getUsers(String username) {
        
        List<User> allUsers = null;
        Session session = HibernateUtil.openSession();
        session.beginTransaction();
        try {
            Query query = session.getNamedQuery("findAllUsers");
            query.setParameter("userName", username);
            allUsers = query.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            //toDo
        } finally {
            session.close();
        }    
        
        return allUsers;
    }

    @Override
    public void deleteUser(User user) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

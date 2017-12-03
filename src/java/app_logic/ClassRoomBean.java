/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app_logic;

import dao.HibernateUtil;
import entity.ClassRoom;
import entity.Reservation;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.hibernate.Session;

/**
 *
 * @author Korisnik
 */
@Stateless
public class ClassRoomBean implements ClassRoomBeanRemote {
   

    @Override
    public List<ClassRoom> getAllClassRooms() throws Exception {
        List<ClassRoom> allClassRooms = null;
        Session session = HibernateUtil.openSession();
        session.beginTransaction();
        try {
            allClassRooms = (List<ClassRoom>) session.getNamedQuery("allClsRoom").list();
            session.getTransaction().commit();
        } 
        
        catch(Exception e){
            System.out.println("Nisam inicijalizovao ucionice");
            throw e;
        }finally {
            session.close();
        }
        return allClassRooms;
    }

    @Override
    public void saveClassRoom(ClassRoom clsr) throws Exception {
        Session session = HibernateUtil.openSession();
        session.beginTransaction();
        try {
            session.save(clsr);
            session.getTransaction().commit();
        } catch (Exception e) {
            throw e;

        } finally {
            session.close();
        }
    }

    @Override
    public void updateClassRoom(ClassRoom clsr) throws Exception {
        Session session = HibernateUtil.openSession();
        session.beginTransaction();

        try {
            session.update(clsr);
            session.getTransaction().commit();
        } 
        catch(Exception e){
            throw e;
        }finally {
            session.close();
        }
    }

    @Override
    public List<ClassRoom> getSearchedClassRooms(Reservation newReservation) {
        
        List<ClassRoom> searchedClassRooms = null;
        Session session = HibernateUtil.openSession();
        session.beginTransaction();
        try {
            searchedClassRooms = (List<ClassRoom>) session.createQuery(
                    "SELECT C FROM ClassRoom C  WHERE C.classRoomID NOT IN "
                    + "     ( SELECT R.classRoom.classRoomID FROM Reservation R WHERE "
                    + "R.isCanceled = 0 AND :termStart < R.endDate AND :termEnd > R.startDate)")
                    .setParameter("termStart", newReservation.getStartDate())
                    .setParameter("termEnd", newReservation.getEndDate())
                    .list();
            
            session.getTransaction().commit();
        } finally {
            session.close();
        }
        
        return searchedClassRooms;
    }
    
    
    @Override
    public List<ClassRoom> getSearchedClassRooms(Date startDate, Date endDate) {
        
        List<ClassRoom> searchedClassRooms = null;
        Session session = HibernateUtil.openSession();
        session.beginTransaction();
        try {
            searchedClassRooms = (List<ClassRoom>) session.createQuery(
                    "SELECT C FROM ClassRoom C  WHERE C.classRoomID NOT IN "
                    + "     ( SELECT R.classRoom.classRoomID FROM Reservation R WHERE "
                    + "R.isCanceled = 0 AND :termStart < R.endDate AND :termEnd > R.startDate)")
                    .setParameter("termStart", startDate)
                    .setParameter("termEnd", endDate)
                    .list();
            
            session.getTransaction().commit();
        } finally {
            session.close();
        }
        
        return searchedClassRooms;
    }

    
}

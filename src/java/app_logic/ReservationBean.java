/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app_logic;

import dao.HibernateUtil;
import entity.ClassRoom;
import entity.Reservation;
import entity.User;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Korisnik
 */
@Stateless
public class ReservationBean implements ReservationBeanRemote {

    @Override
    public List<Reservation> getReservationForClassroom(ClassRoom cls) throws Exception {
        List<Reservation> listReservationForClassroom = null;
        Session session = HibernateUtil.openSession();
        session.beginTransaction();
        try {
            Query query = session.getNamedQuery("getReservationsByClassroomForAdmin");
            query.setParameter("startDate", new Date());
            query.setParameter("classRoomID", cls);
            listReservationForClassroom = (List<Reservation>) query.list();
            session.getTransaction().commit();
        } finally {
            session.close();
        }
        return listReservationForClassroom;
    }

    @Override
    public List<Reservation> getAllReservations(User teacher) {
        
        List<Reservation> allReservations = null;
        Session session = HibernateUtil.openSession();
        session.beginTransaction();
        try {
            Query query = session.getNamedQuery("getReservationsByTeacher");
            query.setParameter("teacher", teacher);
            query.setParameter("startDate", new Date());
            allReservations = (List<Reservation>) query.list();
            session.getTransaction().commit();
        } finally {
            session.close();
        }
        return allReservations;
    }

    @Override
    public List<Reservation> getPassedReservations(User teacher) {
        
        List<Reservation> passedReservations = null;
        Session session = HibernateUtil.openSession();
        session.beginTransaction();
        try {
            Query query = session.getNamedQuery("getPassedReservationsByTeacher");
            query.setParameter("teacher", teacher);
            query.setParameter("endDate", new Date());
            passedReservations = (List<Reservation>) query.list();
            session.getTransaction().commit();
        } finally {
            session.close();
        }
        
        return passedReservations;
    }

    @Override
    public void updateReservation(Reservation reservation) throws Exception {
        Session session = HibernateUtil.openSession();
        session.beginTransaction();
        try {
            session.update(reservation);
            session.getTransaction().commit();
        } catch (Exception e) {
            throw e;

        } finally {
            session.close();
        }
    }

    @Override
    public List<Reservation> getAllReservations(Date dateS, Date dateE) {
        
        List<Reservation> allReservations = null;
        Session session = HibernateUtil.openSession();
        session.beginTransaction();
        try {
            Query query = session.getNamedQuery("getReservationsByDate");
            query.setParameter("sDate", dateS);
            query.setParameter("eDate", dateE);
            allReservations = (List<Reservation>) query.list();
            session.getTransaction().commit();
        } finally {
            session.close();
        }
        return allReservations;
    }

    @Override
    public List<Reservation> getReservationsForClassRoomOnly(ClassRoom clsr) {
        
        List<Reservation> allReservations = null;
         Session session = HibernateUtil.openSession();
         session.beginTransaction();
        try {
            Query query = session.getNamedQuery("getReservationsByClassRoom");
            query.setParameter("classRoomID", clsr);
            allReservations = (List<Reservation>) query.list();
        }
        finally{
            session.close();
        }
        
        return allReservations;
    }

    @Override
    public void saveReservation(Reservation newReservation) throws Exception {
        Session session = HibernateUtil.openSession();
        session.beginTransaction();
        try {
            session.save(newReservation);
            session.getTransaction().commit();
        } catch (Exception e) {
            throw e;

        } finally {
            session.close();
        }
    }

    @Override
    public List<Reservation> getReservationsByDateAndClassRoom(ClassRoom clsr, Date startDate, Date endDate) {
        List<Reservation> dailyReservations = null;
        Session session = HibernateUtil.openSession();
        session.beginTransaction();
        try {
            Query query = session.getNamedQuery("getReservationsByDateAndClassRoom");
            query.setParameter("classRoomID", clsr);
            query.setParameter("sDate", startDate);
            query.setParameter("eDate", endDate);
            dailyReservations = (List<Reservation>) query.list();
        }
        finally{
            session.close();
        }
        return dailyReservations;
    }
    
    

   
}

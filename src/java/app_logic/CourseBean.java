/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app_logic;

import dao.HibernateUtil;
import entity.Course;
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
public class CourseBean implements CourseBeanRemote {

    @Override
    public List<Course> getAllCourses() {
        List<Course> allCourses = null;
        Session session = HibernateUtil.openSession();
        session.beginTransaction();
        allCourses = (List<Course>) session.getNamedQuery("allCourses").list();
        session.getTransaction().commit();
        session.close();
        
        return allCourses;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public List<Course> getTeacherCourses(String courseName) {
        
        Session session = HibernateUtil.openSession();
        session.beginTransaction();
        Query query = session.getNamedQuery("getCourseByName");
        query.setParameter("coursename", courseName);
        List<Course> courses = query.list();
        
        return courses;
    }
}

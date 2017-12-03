/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app_logic;

import entity.Course;
import entity.Module;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Korisnik
 */
@Local
public interface CourseBeanRemote {

public List<Course> getAllCourses();    
public List<Course> getTeacherCourses(String courseName);
}

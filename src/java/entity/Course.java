/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import entity.Student.Degree;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;


@Entity
@NamedQueries(
        value = {
            @NamedQuery(name = "allCourses", query = "from Course"),
            @NamedQuery(name = "getCourseByName", query = "from Course where courseName = :coursename")
        }
)
public class Course implements Serializable {

    @Id
    @GeneratedValue
    private int courseID;

    // predavaci koji ga predaju
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "teacher_course", joinColumns = @JoinColumn(name = "courseID"),
            inverseJoinColumns = @JoinColumn(name = "teacherID"))
    List<Teacher> teachers = new LinkedList<Teacher>();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Degree courseDegree;

    @ManyToOne
    @JoinColumn(name = "moduleID")
    private Module courseModule;

    @Column(name = "course_name")
    private String courseName;

    private int year;

    public Course() {
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    public Degree getCourseDegree() {
        return courseDegree;
    }

    public void setCourseDegree(Degree courseDegree) {
        this.courseDegree = courseDegree;
    }

    public Module getCourseModule() {
        return courseModule;
    }

    public void setCourseModule(Module courseModule) {
        this.courseModule = courseModule;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;


@Entity
//nasledjuje Usera
public class Teacher extends User implements Serializable{

    public enum Education {

        REDOVNI_PROF, VANREDNI_PROF, DOCENT, ASISTENT, SARADNIK, GOSTUJUCI_PROF
    }

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Education education;

    private int officeNum;

    //vise nastavnika:vise kurseva kardinalnost m:m
   @ManyToMany(fetch = FetchType.EAGER, mappedBy = "teachers")
    List<Course> teachingCourses = new LinkedList<Course>();

    public Teacher() {
    }

    public Teacher(Education education, int officeNum, String userName, String userPass, String firstName, String surrName, Gender gender, String city, String JMBG, String telephone, String email, String picture) {
        super(userName, userPass, firstName, surrName, gender, city, JMBG, telephone, email, picture);
        this.education = education;
        this.officeNum = officeNum;
    }

    public Education getEducation() {
        return education;
    }

    public void setEducation(Education education) {
        this.education = education;
    }

    public int getOfficeNum() {
        return officeNum;
    }

    public void setOfficeNum(int officeNum) {
        this.officeNum = officeNum;
    }

    public List<Course> getTeachingCourses() {
        return teachingCourses;
    }

    public void setTeachingCourses(List<Course> teachingCourses) {
        this.teachingCourses = teachingCourses;
    }

}

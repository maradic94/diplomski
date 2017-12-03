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
import javax.persistence.ManyToOne;


@Entity
public class Student extends User implements Serializable {

    public enum Degree {

        AKADEMSKE, MASTER, DOKTORSKE
    }

    //kardinalnost 1modul:vise studenata
    //join po moduleID
    @ManyToOne
    @JoinColumn(name = "moduleID")
    private Module module;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Degree degree;

    //student cekira svoje obaveze
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "student_reservation", joinColumns = @JoinColumn(name = "studentID"),
            inverseJoinColumns = @JoinColumn(name = "reservationID"))
    List<Reservation> reservations = new LinkedList<Reservation>();

    public Student() {
    }

    public Student(Module module, int year, Degree degree) {
        this.module = module;
        this.year = year;
        this.degree = degree;
    }

    public Student(Module module, int year, Degree degree,
            String userName, String userPass, String firstName, String surrName,
            Gender gender, String city, String JMBG, String telephone, String email, String picture) {
        super(userName, userPass, firstName, surrName, gender, city, JMBG, telephone, email, picture);
        this.module = module;
        this.year = year;
        this.degree = degree;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    @Override
    public String toString() {
        return "Student{" + "module=" + module + ", year=" + year + ", degree=" + degree + '}';
    }

}

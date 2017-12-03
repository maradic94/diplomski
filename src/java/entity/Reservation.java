/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;


@Entity
@NamedQueries(
        value = {
            @NamedQuery(name = "getReservationsByDateAndClassRoom", query = "from Reservation where isCanceled = 0  and classRoom = :classRoomID"
                    + " and startDate between :sDate and :eDate order by startDate asc"),
            @NamedQuery(name = "getReservationsByClassRoom", query = "from Reservation where isCanceled = 0  and classRoom = :classRoomID"),
            @NamedQuery(name = "getReservationsByTeacher", query = "from Reservation where isCanceled = 0  and endDate > :startDate "
                    + "and teacher = :teacher order by startDate asc"),
            @NamedQuery(name = "getPassedReservationsByTeacher", query = "from Reservation where isCanceled = 0  and endDate <= :endDate "
                    + "and teacher = :teacher order by startDate asc"),
            @NamedQuery(name = "getReservationsByDate", query = "from Reservation where startDate between :sDate and :eDate"),
            
            // za editovanje ucionice
            @NamedQuery(name = "getReservationsByClassroomForAdmin", query = "from Reservation where isCanceled = 0  and endDate > :startDate "
                    + "and classRoom = :classRoomID order by startDate asc")
        }
)
public class Reservation implements Serializable{

    private static final String patternDate = "dd.MM.yyyy";
    private static final String patternTime = "HH:mm";

    private static final SimpleDateFormat simpleDateFormatDate = new SimpleDateFormat(patternDate);
    private static final SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat(patternTime);

    public enum Purpose {

        LECTURES, EXERCISES, LAB_EXERCISES, CONSULTATION,
        TEST, EXAM, HOMEWORK, PROJECTS, SPECIAL_LECTURES,
        GRADUATE_WORK, MASTER_WORK, DOCTORAL_WORK, OTHER
    }

    @Id
    @GeneratedValue
    private int reservationID;

    @Column(name = "description")
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Purpose purpose;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    
    @Column(nullable = false)
    private long duration;

    @ManyToOne
    @JoinColumn(nullable = false, name = "classRoomID")
    private ClassRoom classRoom;

    @ManyToOne
    @JoinColumn(nullable = false, name = "teacherID")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "courseID")
    private Course course;

    @Column(nullable = false, name = "is_canceled")
    private boolean isCanceled;

    //sve sto je transient ne cuvamo u bazi.
    //ovi podaci sluze nam iskljucivo za app
    @Transient
    private String dateToShow;

    @Transient
    private String timeToShow;

    @Transient
    private String dateEndToShow;

    @Transient
    private String timeEndToShow;

    @Column(nullable = false)
    private boolean isUsed;

    //studenti koji ga slusaju   
    //kardinalnost vise vise
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "reservations")
    List<Student> students = new LinkedList<Student>();

    public Reservation() {
    }

    public Purpose getPurpose() {
        return purpose;
    }

    public void setPurpose(Purpose purpose) {
        this.purpose = purpose;
    }

    public int getReservationID() {
        return reservationID;
    }

    public void setReservationID(int reservationID) {
        this.reservationID = reservationID;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;

    }

    public long getDuration() {
        duration = (endDate.getTime() - startDate.getTime()) / (60 * 1000);
        return duration;
    }

    public String getDurationFormated() {
        Date date = new Date(0, 0, 0, 0, (int) duration, 0);
        return simpleDateFormatTime.format(date);
    }

    public ClassRoom getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(ClassRoom classRoom) {
        this.classRoom = classRoom;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public boolean isIsCanceled() {
        return isCanceled;
    }

    public void setIsCanceled(boolean isCanceled) {
        this.isCanceled = isCanceled;
    }

    public String getDateToShow() {
        dateToShow = simpleDateFormatDate.format(startDate);
        return dateToShow;
    }

    public void setDateToShow(String dateToShow) {
        this.dateToShow = dateToShow;
    }

    public String getTimeToShow() {
        timeToShow = simpleDateFormatTime.format(startDate);
        return timeToShow;
    }

    public void setTimeToShow(String timeToShow) {
        this.timeToShow = timeToShow;
    }

    public String getDateEndToShow() {
        dateEndToShow = simpleDateFormatDate.format(endDate);
        return dateEndToShow;
    }

    public void setDateEndToShow(String dateEndToShow) {
        this.dateEndToShow = dateEndToShow;
    }

    public String getTimeEndToShow() {
        timeEndToShow = simpleDateFormatTime.format(endDate);
        return timeEndToShow;
    }

    public void setTimeEndToShow(String timeEndToShow) {
        this.timeEndToShow = timeEndToShow;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isIsUsed() {
        return isUsed;
    }

    public void setIsUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

}

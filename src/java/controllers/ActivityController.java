/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;


import app_logic.ClassRoomBean;
import entity.ClassRoom;
import entity.Course;
import entity.Reservation;
import entity.SessionEntity;
import entity.Teacher;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;
import util.Message;
import util.Util;
import app_logic.ClassRoomBeanRemote;
import app_logic.ReservationBean;
import app_logic.ReservationBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author Korisnik
 */
@ManagedBean(name = "activityController")
@SessionScoped
public class ActivityController {

    private List<ClassRoom> allClassRooms;
    
    ReservationBeanRemote reservationBean;
    
    ClassRoomBeanRemote classRoomBean;
    private ClassRoom classRoom;
    private Date selectedDate = new Date();

    // teacher
    private boolean showDetail;
    private List<Reservation> allReservations;

    private List<ClassRoom> searchedClassRooms;

    //za prikaz selektovane
    private Reservation reservation;

    //za rezervisanje
    private Course selectedCourse;

    private Reservation newReservation = new Reservation();
    private int purpose;
    private int alertType;
    private String messageContent = "";

    private void initClassRooms() {

        try {
            allClassRooms = classRoomBean.getAllClassRooms();
        } catch (Exception ex) {
        }

    }

    private void initReservations() {

        allReservations = reservationBean.getReservationsForClassRoomOnly(classRoom);

        if (allReservations.isEmpty()) {
            alertType = Message.ALERT_WARNING;
            messageContent = messageContent = Message.getMessageFromResources("noReservationsForClassRoom") + "\n";
        } else {
            alertType = Message.ALERT_OK;
            messageContent = messageContent = Message.getMessageFromResources("okReservationDisplayForClassRoom") + "\n";

        }

    }

    public ActivityController() {
        reservationBean = new ReservationBean();
        classRoomBean = new ClassRoomBean();
        initClassRooms();
        classRoom = allClassRooms.get(0);

        initReservations();

        scheduleInit();
    }

    public List<ClassRoom> getAllClassRooms() {
        return allClassRooms;
    }

    public void setAllClassRooms(List<ClassRoom> allClassRooms) {
        this.allClassRooms = allClassRooms;
    }

    public ClassRoom getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(ClassRoom classRoom) {
        this.classRoom = classRoom;
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }

    public void showDetails() {
        initReservations();
        scheduleInit();
    }

    public boolean isShowDetail() {
        return showDetail;
    }

    public void setShowDetail(boolean showDetail) {
        this.showDetail = showDetail;
    }

    // prikazivanje rezervacija
    private ScheduleModel model;
    private DefaultScheduleEvent event;

    public void scheduleInit() {
        model = new DefaultScheduleModel();

        for (Reservation r : allReservations) {
            long time = r.getStartDate().getTime() + 7200000;
            Date startD = new Date(time);
            time += r.getDuration() * 60000;
            Date endD = new Date(time);
            System.out.println("Vreme pocetka: " + startD + " Vreme zavrsetk:" + endD);
            event = new DefaultScheduleEvent(r.getDescription(), startD, endD);
            event.setData(r);
            event.setEditable(false);
            model.addEvent(event);
        }
    }

    public ScheduleModel getModel() {
        return model;
    }

    public void onEventSelect(SelectEvent selectEvent) {
        event = (DefaultScheduleEvent) selectEvent.getObject();
        reservation = (Reservation) event.getData();

        Logger.getLogger(ActivityController.class.getName()).log(Level.INFO, "USAO U TITLE" + event.getTitle());
    }

    public DefaultScheduleEvent getEvent() {
        return event;
    }

    public List<Reservation> getAllReservations() {
        return allReservations;
    }

    public void setAllReservations(List<Reservation> allReservations) {
        this.allReservations = allReservations;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public List<ClassRoom> getSearchedClassRooms() {
        return searchedClassRooms;
    }

    public void setSearchedClassRooms(List<ClassRoom> searchedClassRooms) {
        this.searchedClassRooms = searchedClassRooms;
    }

    public int getPurpose() {
        return purpose;
    }

    public void setPurpose(int purpose) {
        this.purpose = purpose;
    }

    public Course getSelectedCourse() {
        return selectedCourse;
    }

    public void setSelectedCourse(Course selectedCourse) {
        this.selectedCourse = selectedCourse;
    }

    public List<Course> getTeachersCourses() {
        SessionEntity sessionEntity = Util.getSessionEntity();
        Teacher myTeacher = (Teacher) sessionEntity.getMyUser();

        List<Course> courses = new LinkedList<Course>();
        for (Course c : myTeacher.getTeachingCourses()) {
            courses.add(c);
        }

        Course emptyCourse = new Course();
        emptyCourse.setCourseName(Message.getMessageFromResources("none"));
        courses.add(emptyCourse);
        return courses;
    }

    private boolean okToReservate() {

        searchedClassRooms = classRoomBean.getSearchedClassRooms(newReservation);

        for (ClassRoom c : searchedClassRooms) {
            if (c.getClassRoomName().equals(classRoom.getClassRoomName())) {

                return true;
            }
        }

        return false;

    }

    private boolean okStartBeforeEndDate() {
        if (newReservation.getStartDate().after(newReservation.getEndDate())) {
            return false;
        }
        return true;
    }

    private boolean okDateFormat() {

        Date dateS = new Date(newReservation.getStartDate().getTime());
        dateS.setHours(8);
        dateS.setMinutes(0);

        Date dateE = new Date(newReservation.getEndDate().getTime());
        dateE.setHours(21);
        dateE.setMinutes(30);
        if (newReservation.getStartDate().before(dateS) || newReservation.getEndDate().after(dateE)) {
            return false;
        }
        return true;
    }

    private boolean okReservationDuration() {
        //manje od pola sata
        if (newReservation.getEndDate().getTime() - newReservation.getStartDate().getTime() < 30 * 60 * 1000) {
            return false;
        }
        return true;
    }

    private void checkReservation() {
        alertType = Message.ALERT_OK;
        messageContent = "";

        if (!okStartBeforeEndDate()) {
            alertType = Message.ALERT_WARNING;
            messageContent = Message.getMessageFromResources("okStartBeforeEndFormat") + "\n";
            return;
        }
        if (!okDateFormat()) {
            alertType = Message.ALERT_WARNING;
            messageContent = Message.getMessageFromResources("errorDateFormat") + "\n";
            return;
        }

        if (!okReservationDuration()) {
            alertType = Message.ALERT_WARNING;
            messageContent = Message.getMessageFromResources("errorDateDuration") + "\n";
            return;
        }

        if (!okToReservate()) {
            alertType = Message.ALERT_WARNING;
            messageContent = Message.getMessageFromResources("notSuccessReservation") + "\n";
            return;
        }

    }

    public void insertNewReservation() {
        alertType = Message.ALERT_OK;
        messageContent = "";

        if (newReservation.getDescription() == null
                || newReservation.getDescription().trim().equals("")) {

            alertType = Message.ALERT_WARNING;
            messageContent = Message.getMessageFromResources("descriptionEmpty");
            return;
        }

        checkReservation();

        if (alertType != Message.ALERT_OK) {
            return;
        }
        messageContent = Message.getMessageFromResources("successReservation");

        if (newReservation.getCourse().getCourseName().equals(Message.getMessageFromResources("none"))) {
            newReservation.setCourse(null);
        }

        SessionEntity sessionEntity = Util.getSessionEntity();
        newReservation.setClassRoom(classRoom);

        newReservation.setTeacher((Teacher) sessionEntity.getMyUser());
        newReservation.setPurpose(Reservation.Purpose.values()[purpose - 1]);

        Date dateS = new Date(newReservation.getStartDate().getTime() + 2);
        dateS.setHours(dateS.getHours());
        Date dateE = new Date(newReservation.getEndDate().getTime() + 2);
        dateE.setHours(dateE.getHours());

        event = new DefaultScheduleEvent();
        event.setTitle(newReservation.getDescription());
        event.setStartDate(dateS);
        event.setEndDate(dateE);
        event.setData(newReservation);
        event.setEditable(false);
        model.addEvent(event);

        try {
            reservationBean.saveReservation(newReservation);
        } catch (Exception e) {
            //toDo
            alertType = Message.ALERT_DANGER;
            messageContent = Message.getMessageFromResources("msgBadRegister") + "\n";
        }
        newReservation = new Reservation();
    }

    public Reservation getNewReservation() {
        return newReservation;
    }

    public void setNewReservation(Reservation newReservation) {
        this.newReservation = newReservation;
    }

    public int getAlertType() {
        return alertType;
    }

    public void setAlertType(int alertType) {
        this.alertType = alertType;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

}

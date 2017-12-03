/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import dao.HibernateUtil;
import entity.Reservation;
import entity.SessionEntity;
import entity.Student;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.hibernate.Query;
import org.hibernate.Session;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;
import util.Message;
import util.Util;
/**
 *
 * @author Korisnik
 */


@ManagedBean(name = "scheduleController")
@SessionScoped
public class ScheduleController {

    private List<Reservation> weekReservations;
    private ScheduleModel model;
    private DefaultScheduleEvent event;
    private Reservation selectedReservation;
    private String onCourseName = "\n"+" na predmetu "+"\n";
    private LinkedList<String> myChosenReservationsId;
    private List<Reservation> studentsChosenReservations;

    private int alertType;
    private String messageContent;

    public ScheduleController() {
        initActivitiesForStudent();
    }

    public void initActivitiesForStudent() {
        SessionEntity sessionEntity = Util.getSessionEntity();
        Student student = (Student) sessionEntity.getMyUser();
        System.out.println("STUDENT: "+student.getIsApproved());

        Session session = HibernateUtil.openSession();
        session.beginTransaction();
        try {
            studentsChosenReservations = student.getReservations();
            Query query = session.createQuery("select R from Reservation R inner join R.course as course "
                    + "where course.year = :year and course.courseModule = :module and R.isCanceled = 0");

            query.setParameter("year", student.getYear());
            query.setParameter("module", student.getModule());

            weekReservations = (List<Reservation>) query.list();

            session.getTransaction().commit();
        } finally {
            session.close();
        }

        refreshModel();
        myChosenReservationsId = new LinkedList<String>();
        for (Reservation r : studentsChosenReservations) {
            myChosenReservationsId.add(r.getReservationID() + "");
        }

    }

    public List<Reservation> getWeekReservations() {
        return weekReservations;
    }

    public void setWeekReservations(List<Reservation> weekReservations) {
        this.weekReservations = weekReservations;
    }

    public ScheduleModel getModel() {
        return model;
    }

    public void setModel(ScheduleModel model) {
        this.model = model;
    }

    public DefaultScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(DefaultScheduleEvent event) {
        this.event = event;
    }

    public void onEventSelect(SelectEvent selectEvent) {
        event = (DefaultScheduleEvent) selectEvent.getObject();
        selectedReservation = (Reservation) event.getData();
    }

    private void updateStudent() {
        SessionEntity sessionEntity = Util.getSessionEntity();
        Student student = (Student) sessionEntity.getMyUser();

        student.setReservations(studentsChosenReservations);
        for (Reservation r : studentsChosenReservations) {
            r.getStudents().add(student);
        }

        Session session = HibernateUtil.openSession();
        session.beginTransaction();

        try {
            session.update(student);
            session.getTransaction().commit();
        } finally {
            session.close();
        }

    }

    private void refreshModel() {
        model = new DefaultScheduleModel();

        for (Reservation r : weekReservations) {
            long time = r.getStartDate().getTime() + 7200000;
            Date startD = new Date(time);
            time += r.getDuration() * 60 * 1000;
            Date endD = new Date(time);
            event = new DefaultScheduleEvent(r.getDescription(), startD, endD);
            event.setData(r);
            event.setEditable(false);

            if (studentsChosenReservations.size() > 0) {
                for (Reservation res : studentsChosenReservations) {
                    if (res.getReservationID() == r.getReservationID()) {
                        event.setStyleClass("event-custom");
                        break;
                    }
                }
            }
            model.addEvent(event);
        }
    }

    public void mark() {
       // alertType = Message.ALERT_OK;
        messageContent = "";
        studentsChosenReservations = new LinkedList<Reservation>();
        for (String resId : myChosenReservationsId) {
            alertType = Message.ALERT_OK;
            Reservation reservation = null;
            for (Reservation r : weekReservations) {
                if (resId.equals(r.getReservationID() + "")) {
                    reservation = r;
                    break;
                }
            }
            if (reservation == null) {
                continue;
            }

            if (studentsChosenReservations.size() > 0) {
                for (Reservation r : studentsChosenReservations) {
                    if (reservation.getDateToShow().equals(r.getDateToShow())) {

                        if ((reservation.getStartDate().after(r.getEndDate()))
                                || (reservation.getEndDate().before(r.getStartDate()))
                                || (reservation.getStartDate().compareTo(r.getEndDate()) == 0)
                                || (reservation.getEndDate().compareTo(r.getStartDate()) == 0)) {

                            //ok je
                        } else {
                            alertType = Message.ALERT_WARNING;
                            messageContent += Message.getMessageFromResources("chosenActivity")
                                    + " - " + reservation.getDescription() + " - "
                                    + Message.getMessageFromResources("cantMark") + " ( "
                                    + Message.getMessageFromResources("activ") + " - "
                                    + r.getDescription() + " - " + Message.getMessageFromResources("existsActivityTime") + ")." + "\n";
                        }
                    }
                }

                if (alertType == Message.ALERT_OK) {
                    studentsChosenReservations.add(reservation);
                }

            } else {
                studentsChosenReservations.add(reservation);
            }

            Logger.getLogger(ScheduleController.class.getName()).log(Level.INFO, "BUUUJA" + reservation.getDescription());
        }

        if (myChosenReservationsId.size() == 0) {
            alertType = Message.ALERT_INFO;
            messageContent = Message.getMessageFromResources("msgSuccessfullyDismarked") + "\n";
        }
        updateStudent();
        refreshModel();
    }

    public Reservation getSelectedReservation() {
        return selectedReservation;
    }

    public void setSelectedReservation(Reservation selectedReservation) {
        this.selectedReservation = selectedReservation;
    }

    public LinkedList<String> getMyChosenReservationsId() {
        return myChosenReservationsId;
    }

    public void setMyChosenReservationsId(LinkedList<String> myChosenReservationsId) {
        this.myChosenReservationsId = myChosenReservationsId;
    }

    public List<Reservation> getStudentsChosenReservations() {
        return studentsChosenReservations;
    }

    public void setStudentsChosenReservations(List<Reservation> studentsChosenReservations) {
        this.studentsChosenReservations = studentsChosenReservations;
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

    public String getOnCourseName() {
        return onCourseName;
    }

    public void setOnCourseName(String onCourseName) {
        this.onCourseName = onCourseName;
    }

    
}

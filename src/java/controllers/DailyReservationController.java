/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package controllers;

import app_logic.ClassRoomBean;
import entity.ClassRoom;
import entity.Reservation;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import util.Message;
import app_logic.ClassRoomBeanRemote;
import app_logic.ReservationBean;
import app_logic.ReservationBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author Korisnik
 */
@ManagedBean(name = "dailyReservationController")
@RequestScoped
public class DailyReservationController {

    private List<ClassRoom> allClassRooms;
    private ClassRoom classRoom;
    
    
    ClassRoomBeanRemote classRoomBean;
    
    
    ReservationBeanRemote reservationBean;
    private Date selectedDate = new Date();
    private int alertType;
    private String messageContent = "";
    private List<Reservation> dailyReservations;
    private Date endDate;
    private Date startDate;

    private void initClassRooms() {
        
        try {
            allClassRooms = classRoomBean.getAllClassRooms();
        } catch (Exception ex) {
            System.out.println("Ucionice nisu inicijalizovane");
        }
        
    }

    public DailyReservationController() {
        classRoomBean = new ClassRoomBean();
        reservationBean = new ReservationBean();
        
        initClassRooms();
        classRoom = allClassRooms.get(0);
    }

    private void formatStartEndDate() {
        endDate = new Date(selectedDate.getTime());
        endDate.setHours(22);

        startDate = new Date(selectedDate.getTime());
        startDate.setHours(8);
    }

    public void showActivity() {
        formatStartEndDate();
        dailyReservations = reservationBean.getReservationsByDateAndClassRoom(classRoom, startDate, endDate);
        if (!dailyReservations.isEmpty()) {
            alertType = Message.ALERT_OK;
            messageContent = Message.getMessageFromResources("msgSuccessfulActivityDisplay") + "\n";
        }

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

    public List<Reservation> getDailyReservations() {
        return dailyReservations;
    }

    public void setDailyReservations(List<Reservation> dailyReservations) {
        this.dailyReservations = dailyReservations;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
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

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
import java.util.Iterator;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import util.Message;
import app_logic.ClassRoomBeanRemote;
import app_logic.ReservationBean;
import app_logic.ReservationBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author Korisnik
 */
@ManagedBean(name = "adminReservationController")
@SessionScoped
public class AdminReservationController {

    //admin search
    private Date adminSearchDate = new Date();
    private List<ClassRoom> allClassRooms;
    private List<String> selectedClassRooms;
    
    
    ClassRoomBeanRemote classRoomBean;
    
    
    ReservationBeanRemote reservationBean;
    private int alertType;
    private String messageContent = "";
    private List<Reservation> allReservations;

    private void initClassRooms() {
        
        try {
            allClassRooms = classRoomBean.getAllClassRooms();
        } catch (Exception ex) {
            System.out.println("ClassRooms not loaded");
        }
    }

    private void initReservations() {
        Date dateS = new Date(adminSearchDate.getTime());
        Date dateE = new Date(adminSearchDate.getTime());

        dateS.setHours(7);
        dateE.setHours(22);
        
        allReservations = reservationBean.getAllReservations(dateS, dateE);       
    }

    public AdminReservationController() {
        classRoomBean = new ClassRoomBean();
        reservationBean = new ReservationBean();
        initClassRooms();
        initReservations();
    }

    public List<ClassRoom> getAllClassRooms() {
        return allClassRooms;
    }

    public void setAllClassRooms(List<ClassRoom> allClassRooms) {
        this.allClassRooms = allClassRooms;
    }

    public List<String> getSelectedClassRooms() {
        return selectedClassRooms;
    }

    public void setSelectedClassRooms(List<String> selectedClassRooms) {
        this.selectedClassRooms = selectedClassRooms;
    }

    public Date getAdminSearchDate() {
        return adminSearchDate;
    }

    public void setAdminSearchDate(Date adminSearchDate) {
        this.adminSearchDate = adminSearchDate;
    }

    public List<Reservation> getAllReservations() {
        return allReservations;
    }

    public void setAllReservations(List<Reservation> allReservations) {
        this.allReservations = allReservations;
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
    

    public void filterReservation() {
        initReservations();

        if (selectedClassRooms.size() > 0) {
            Iterator<Reservation> iter = allReservations.iterator();
            while (iter.hasNext()) {
                Reservation r = iter.next();
                if (!selectedClassRooms.contains(r.getClassRoom().getClassRoomName())) {
                    iter.remove();
                }
            }
            messageContent = Message.getMessageFromResources("successfullReservationDisplay") + "\n";
            alertType = Message.ALERT_OK;
        }
        else{
        messageContent = Message.getMessageFromResources("unsuccessfullReservationDisplay") + "\n";;
        alertType = Message.ALERT_WARNING;
        }
        
    }

}

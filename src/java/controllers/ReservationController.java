/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import app_logic.ReservationBean;
import entity.Reservation;
import entity.SessionEntity;
import entity.Teacher;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import util.Message;
import util.Util;
import app_logic.ReservationBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author Korisnik
 */
@ManagedBean(name = "reservationController")
@RequestScoped
public class ReservationController {

    private List<Reservation> allReservations;
    private List<Reservation> passedReservations;
    
    
    ReservationBeanRemote reservationBean;
    private int alertType;
    private String messageContent;

    private void initReservations() {
        SessionEntity sessionEntity = Util.getSessionEntity();
        Teacher teacher = (Teacher) sessionEntity.getMyUser();
        allReservations = reservationBean.getAllReservations(teacher);
    }

    private void initPassedReservations() {
        SessionEntity sessionEntity = Util.getSessionEntity();
        Teacher teacher = (Teacher) sessionEntity.getMyUser();
        passedReservations = reservationBean.getPassedReservations(teacher);
    }

    public ReservationController() {
        reservationBean = new ReservationBean();
        initReservations();
        initPassedReservations();
    }

    public List<Reservation> getAllReservations() {
        return allReservations;
    }

    public void cancelReservation(Reservation reservation) {
        alertType = Message.ALERT_OK;
        messageContent = Message.getMessageFromResources("msgSuccessfullyCanceled") + "\n";
        reservation.setIsCanceled(true);
        try {
            reservationBean.updateReservation(reservation);
        } catch (Exception ex) {
            alertType = Message.ALERT_DANGER;
            messageContent = Message.getMessageFromResources("msgBadRegister") + "\n";
        }
        allReservations.remove(reservation);
    }

    public void updateHappenedReservation(Reservation reservation, boolean used) {
        alertType = Message.ALERT_OK;
        messageContent = Message.getMessageFromResources("msgSuccessfullyCanceledUsage") + "\n";
        reservation.setIsUsed(used);
        try {
            reservationBean.updateReservation(reservation);
        } catch (Exception ex) {
            alertType = Message.ALERT_DANGER;
            messageContent = Message.getMessageFromResources("msgBadRegister") + "\n";
        }
    }

    public void setReservationUsed(Reservation reservation) {
        if (!reservation.isIsUsed()) {
            updateHappenedReservation(reservation, true);
        }
    }

    public void setReservationUnUsed(Reservation reservation) {
        if (reservation.isIsUsed()) {
            updateHappenedReservation(reservation, false);
        }
    }

    public List<Reservation> getPassedReservations() {
        return passedReservations;
    }

    public void setPassedReservations(List<Reservation> passedReservations) {
        this.passedReservations = passedReservations;
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

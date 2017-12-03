/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import app_logic.UserBean;
import app_logic.UserBeanLocal;
import entity.SessionEntity;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import util.Message;
import util.Util;

/**
 *
 * @author Korisnik
 */
@ManagedBean(name = "studentController")
@RequestScoped
public class StudentController {

    @ManagedProperty(value = "#{sessionEntity}")
    private SessionEntity sessionEntity;
    
    
    UserBeanLocal userBean;
    private String messageContent;
    private int messageType;

    private void initStudent() {
        sessionEntity = Util.getSessionEntity();
    }

    public StudentController() {
        userBean = new UserBean();
        initStudent();
    }

    public void updateStudent() {
        messageType = Message.ALERT_INFO;
        messageContent = Message.getMessageFromResources("successfullyChange") + "\n";
        try {
            userBean.updateUser(sessionEntity.getMyUser());
        } catch (Exception ex) {
            messageType = Message.ALERT_DANGER;
            messageContent = Message.getMessageFromResources("msgBadRegister") + "\n";
        }

        ScheduleController controller = Util.getSessionScheduleController();
        controller.initActivitiesForStudent();

    }

    public SessionEntity getSessionEntity() {
        return sessionEntity;
    }

    public void setSessionEntity(SessionEntity sessionEntity) {
        this.sessionEntity = sessionEntity;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

}

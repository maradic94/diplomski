/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import app_logic.ReservationBean;
import app_logic.UserBean;
import app_logic.UserBeanLocal;
import util.Message;
import entity.Admin;
import entity.Reservation;
import entity.SessionEntity;
import entity.Student;
import entity.Teacher;
import entity.User;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import util.Util;
import app_logic.ReservationBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author Korisnik
 */
@ManagedBean(name = "usersController")
@RequestScoped
public class UsersController {

    // admin_home
    private List<User> allUsers;

    @ManagedProperty(value = "#{sessionEntity}")
    private SessionEntity sessionEntity;

    private boolean showUser;
    private User chosenUser;
    private Teacher chosenTeacher;
    private Student chosenStudent;
    
    
    UserBeanLocal userBean;
    
   
    ReservationBeanRemote reservationBean;
    private String messageContent;
    private int messageType;

    private List<Reservation> allTeacherReservations;
    private List<Reservation> allPassedTeacherReservations;

    private void initUsers() {
        sessionEntity = Util.getSessionEntity();
        String userName = sessionEntity.getMyUser().getUserName();

        allUsers = userBean.getUsers(userName);

    }

    public UsersController() {
        reservationBean = new ReservationBean();
        userBean = new UserBean();
        initUsers();
    }

    public List<User> getAllUsers() {
        for (User u : allUsers) {
            if (u instanceof Admin) {
                u.setType(Message.getMessageFromResources("admin"));
                u.setTypeNum(Util.TYPE_ADMIN);
            } else if (u instanceof Teacher) {
                u.setType(Message.getMessageFromResources("teacher"));
                u.setTypeNum(Util.TYPE_TEACHER);
            } else {
                u.setType(Message.getMessageFromResources("student"));
                u.setTypeNum(Util.TYPE_STUDENT);
            }
        }
        return allUsers;
    }

    public void setAllUsers(List<User> allUsers) {
        this.allUsers = allUsers;
    }

    public boolean isShowUser() {
        return showUser;
    }

    public void setShowUser(boolean showUser) {
        this.showUser = showUser;
    }

    public void showUser(User user) {
        showUser = true;
        chosenUser = user;
        messageType = Message.ALERT_INFO;
        messageContent = Message.getMessageFromResources("successfulUserDisplay") + "\n";
        if (chosenUser instanceof Teacher) {
            initTeacherReservations();
            initTeacherPassedReservations();
            messageType = Message.ALERT_INFO;
            messageContent = Message.getMessageFromResources("successfulUserDisplay") + "\n";
        }
    }

    public void deactivateUser(User user) {
        //deaktivacija
        if (user instanceof Teacher) {
            messageType = Message.ALERT_INFO;
            user.setIsDeactivated(1);

            try {
                userBean.updateUser(user);
                messageContent = Message.getMessageFromResources("successfulTeacherDeactivation") + "\n";
            } catch (Exception ex) {
                messageType = Message.ALERT_DANGER;
                messageContent = Message.getMessageFromResources("msgBadRegister") + "\n";
            }
            allUsers.remove(user);
        }

        if (user instanceof Student) {
            //brisanje
            messageType = Message.ALERT_INFO;
            

            try {
                userBean.deleteUser(user);
                messageContent = Message.getMessageFromResources("successfulStudentDeactivation") + "\n";
                allUsers.remove(user);
            } catch (Exception ex) {
                messageType = Message.ALERT_DANGER;
                messageContent = Message.getMessageFromResources("msgBadRegister") + "\n";
            }

        }
    }

    public SessionEntity getSessionEntity() {
        return sessionEntity;
    }

    public void setSessionEntity(SessionEntity sessionEntity) {
        this.sessionEntity = sessionEntity;
    }

    public User getChosenUser() {
        return chosenUser;
    }

    public void setChosenUser(User chosenUser) {
        this.chosenUser = chosenUser;
    }

    public Teacher getChosenTeacher() {
        chosenTeacher = (Teacher) chosenUser;
        return chosenTeacher;
    }

    public void setChosenTeacher(Teacher chosenTeacher) {
        this.chosenTeacher = chosenTeacher;
    }

    public Student getChosenStudent() {
        chosenStudent = (Student) chosenUser;
        return chosenStudent;
    }

    public void setChosenStudent(Student chosenStudent) {
        this.chosenStudent = chosenStudent;
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

    public List<Reservation> getAllTeacherReservations() {
        return allTeacherReservations;
    }

    private void initTeacherReservations() {
        chosenTeacher = (Teacher) chosenUser;

        allTeacherReservations = reservationBean.getAllReservations(chosenUser);
    }

    private void initTeacherPassedReservations() {
        chosenTeacher = (Teacher) chosenUser;

        allPassedTeacherReservations = reservationBean.getPassedReservations(chosenUser);
    }

    public void setAllTeacherReservations(List<Reservation> allTeacherReservations) {
        this.allTeacherReservations = allTeacherReservations;
    }

    public List<Reservation> getAllPassedTeacherReservations() {
        return allPassedTeacherReservations;
    }

    public void setAllPassedTeacherReservations(List<Reservation> allPassedTeacherReservations) {
        this.allPassedTeacherReservations = allPassedTeacherReservations;
    }

}

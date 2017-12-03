/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import app_logic.UserBean;
import app_logic.UserBeanLocal;
import util.Message;
import entity.Admin;
import entity.SessionEntity;
import entity.Student;
import entity.Teacher;
import entity.User;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;


/**
 *
 * @author Korisnik
 */

@ManagedBean(name = "loginController")
//A @RequestScoped bean lives as long as a single HTTP request-response cycle
//(Ajax request counts as a single HTTP request too).
@RequestScoped
public class LoginController {

    public static final int TYPE_ADMIN = 1;
    public static final int TYPE_STUDENT = 2;
    public static final int TYPE_TEACHER = 3;
    private String userName;
    private String userPass;
    //private UserBeanLocal userBean;
    private User user;
    private Admin admin;
    private Teacher teacher;
    private Student student;

    @ManagedProperty(value = "#{sessionEntity}")
    private SessionEntity sessionEntity;
    
    
    UserBeanLocal userBean;

    public LoginController() {
        userBean = new UserBean();
    }

    private int alertType;
    private String messageContent;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
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

    public String redirectRegister() {
        return "register";
    }

    public String userLogin() {
        messageContent = "";
        List<User> users = null;
        try {
            users = userBean.userLogin(userName, userPass);
        } catch (Exception ex) {
            alertType = Message.ALERT_DANGER;
            messageContent += Message.getMessageFromResources("msgBadRegister") + "\n";
        }

        if (users == null) {
            alertType = Message.ALERT_DANGER;
            messageContent += Message.getMessageFromResources("noUser") + "\n";
            return "index.xhtml";
        } else {
            if (users.size() == 0) {
                alertType = Message.ALERT_DANGER;
                messageContent += Message.getMessageFromResources("noUser") + "\n";
                return "index.xhtml";
            }
        }

        User user = users.get(0);
        if (user.getIsApproved() == 0) {
            alertType = Message.ALERT_INFO;
            messageContent = Message.getMessageFromResources("requestNotYetApproved") + "\n";
            return "index.xhtml";
        }

        if (user.getIsApproved() == -1) {
            alertType = Message.ALERT_WARNING;
            messageContent = Message.getMessageFromResources("requestDenied") + "\n";

            return "index.xhtml";
        }

        if (user.getIsDeactivated() == 1) {
            alertType = Message.ALERT_DANGER;
            messageContent = Message.getMessageFromResources("requestDeactivated") + "\n";
            return "index.xhtml";
        }

        sessionEntity.setMyUser(user);

        if (user instanceof Admin) {
            admin = (Admin) user;
            sessionEntity.getMyUser().setTypeNum(TYPE_ADMIN);
            sessionEntity.getMyUser().setType(Message.getMessageFromResources("admin"));
            return "system/admin_home.xhtml?faces-redirect=true";
        } else if (user instanceof Teacher) {
            sessionEntity.getMyUser().setTypeNum(TYPE_TEACHER);
            sessionEntity.getMyUser().setType(Message.getMessageFromResources("teacher"));
            return "system/teacher_home.xhtml?faces-redirect=true";
        } else {
            sessionEntity.getMyUser().setType(Message.getMessageFromResources("student"));
            sessionEntity.getMyUser().setTypeNum(TYPE_STUDENT);
            return "system/student_home.xhtml?faces-redirect=true";
        }

    }

    public void userLogout() {
        sessionEntity.setMyUser(null);
        sessionEntity = null;
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.invalidate();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(".././login.xhtml?faces-redirect=true");
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public SessionEntity getSessionEntity() {
        return sessionEntity;
    }

    public void setSessionEntity(SessionEntity sessionEntity) {
        this.sessionEntity = sessionEntity;
    }

}

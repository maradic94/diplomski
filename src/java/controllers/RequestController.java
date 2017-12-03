/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import app_logic.UserBean;
import app_logic.UserBeanLocal;
import entity.User;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import util.Message;

/**
 *
 * @author Korisnik
 */
@ManagedBean(name = "requestController")
@RequestScoped
public class RequestController {

    private List<User> userRequests;
    private String messageContent;
    private int alertType;
    
    
    UserBeanLocal userBean;

    private void initRequests() {

        try {
            userRequests = userBean.getUserRequests();
        } catch (Exception ex) {
        }

    }

    public void approveUser(User user) {
        alertType = Message.ALERT_OK;
        messageContent = Message.getMessageFromResources("successfyllyApproved") + "\n";
        user.setIsApproved(1);
        System.out.println("Odobren korisnik");

        try {
            userBean.updateUser(user);
        } catch (Exception ex) {
            alertType = Message.ALERT_DANGER;
            messageContent = Message.getMessageFromResources("msgBadRegister") + "\n";

        }
        userRequests.remove(user);
    }

    public void denyUser(User user) {
        alertType = Message.ALERT_OK;
        messageContent = Message.getMessageFromResources("successfyllyDeny") + "\n";
        user.setIsApproved(-1);
        try {
            userBean.updateUser(user);
        } catch (Exception ex) {
            alertType = Message.ALERT_DANGER;
            messageContent = Message.getMessageFromResources("msgBadRegister") + "\n";

        }
        userRequests.remove(user);
    }

    public RequestController() {
        userBean = new UserBean();
        initRequests();
    }

    public List<User> getUserRequests() {
        return userRequests;
    }

    public void setUserRequests(List<User> userRequests) {
        this.userRequests = userRequests;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public int getAlertType() {
        return alertType;
    }

    public void setAlertType(int alertType) {
        this.alertType = alertType;
    }

}

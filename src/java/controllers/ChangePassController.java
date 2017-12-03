/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import app_logic.UserBean;
import app_logic.UserBeanLocal;
import util.Message;
import entity.SessionEntity;
import entity.User;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import util.Util;
/**
 *
 * @author Korisnik
 */

@ManagedBean(name = "changePassController")
@RequestScoped
public class ChangePassController {

    private String userName;
    private String oldPass;
    private String newPass;
    private String newPassConfirm;
    
    
    UserBeanLocal userBean;
    private static Pattern passwordRepeatCharPattern = Pattern.compile("(.)\\1{2}");
    private static Pattern passwordStartCharPattern = Pattern.compile("^([a-z]|[A-Z])");
    private static Pattern passwordContainsPattern = Pattern.compile("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])");

    private int alertType;
    private String messageContent;

    public ChangePassController() {
        userBean = new UserBean();
    }

    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOldPass() {
        return oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public String getNewPassConfirm() {
        return newPassConfirm;
    }

    public void setNewPassConfirm(String newPasConfirm) {
        this.newPassConfirm = newPasConfirm;
    }

    private boolean checkPasswordLengthOk() {
        if (newPass.length() < 6 || newPass.length() > 12) {
            return false;
        }
        return true;
    }

    private boolean checkPasswordRepeatCharOk() {
        Matcher matcher = passwordRepeatCharPattern.matcher(newPass);
        if (matcher.find()) {
            return false;
        }
        return true;
    }

    private boolean checkPasswordStartsCharOk() {
        Matcher matcher = passwordStartCharPattern.matcher(newPass);
        if (!matcher.find()) {
            return false;
        }
        return true;
    }

    private boolean checkPasswordContainsOk() {
        Matcher matcher = passwordContainsPattern.matcher(newPass);
        if (!matcher.find()) {
            return false;
        }
        return true;
    }

    private void checkOK() {
        alertType = Message.ALERT_OK;
        messageContent = "";

        if (newPass.equals(oldPass)) {
            alertType = Message.ALERT_DANGER;
            messageContent += Message.getMessageFromResources("samePasswordsError") + "\n";
        }

        if (!newPass.equals(newPassConfirm)) {
            alertType = Message.ALERT_DANGER;
            messageContent += Message.getMessageFromResources("confirmPassOldPass") + "\n";
        } else {

            if (!checkPasswordLengthOk()) {
                alertType = Message.ALERT_WARNING;
                messageContent += Message.getMessageFromResources("passwordErrorLengthFormat") + "\n";
            }
            if (!checkPasswordContainsOk()) {
                alertType = Message.ALERT_WARNING;
                messageContent += Message.getMessageFromResources("passwordErrorContainsFormat") + "\n";

            }
            if (!checkPasswordRepeatCharOk()) {
                alertType = Message.ALERT_WARNING;
                messageContent += Message.getMessageFromResources("passwordErrorRepeatFormat") + "\n";

            }
            if (!checkPasswordStartsCharOk()) {
                alertType = Message.ALERT_WARNING;
                messageContent += Message.getMessageFromResources("passwordErrorStartsFormat") + "\n";

            }
        }
    }

    @ManagedProperty(value = "#{sessionEntity}")
    private SessionEntity sessionEntity;

    // dok je ulogovan
    public void saveNewPasswordNoUserName() {
        alertType = Message.ALERT_OK;
        messageContent = "";

        checkOK();

        if (alertType == Message.ALERT_OK) {

            sessionEntity = Util.getSessionEntity();
            String pass = sessionEntity.getMyUser().getUserPass();

            if (pass.equals(oldPass)) {
                User user = sessionEntity.getMyUser();
                user.setUserPass(newPass);
                saveUser(user);

            } else {
                alertType = Message.ALERT_DANGER;
                messageContent += Message.getMessageFromResources("wrongPass") + "\n";
            }
        }

        resetAllData();
    }

    private void saveUser(User user) {
        alertType = Message.ALERT_OK;
        try {
            userBean.updateUser(user);
        } catch (Exception ex) {
            alertType = Message.ALERT_DANGER;
            messageContent += Message.getMessageFromResources("msgBadRegister") + "\n";
        }
    }

    public void saveNewPassword() {
        checkOK();

        if (alertType == Message.ALERT_OK) {
            List<User> users = null;
            try {
                users = userBean.findByUserNameAndPass(userName, oldPass);
            } catch (Exception ex) {
                  alertType = Message.ALERT_DANGER;
                   messageContent += Message.getMessageFromResources("msgBadRegister") + "\n";            }

            if (users == null) {
                alertType = Message.ALERT_DANGER;
                messageContent += Message.getMessageFromResources("noUser") + "\n";
                return;
            } else {
                if (users.size() == 0) {
                    alertType = Message.ALERT_DANGER;
                    messageContent += Message.getMessageFromResources("noUser") + "\n";
                    return;
                }
            }

            User user = users.get(0);
            user.setUserPass(newPass);
            saveUser(user);

        }
        resetAllData();
    }

    private void resetAllData() {
        userName = null;
        oldPass = null;
        newPass = null;
        newPassConfirm = null;
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

    public SessionEntity getSessionEntity() {
        return sessionEntity;
    }

    public void setSessionEntity(SessionEntity sessionEntity) {
        this.sessionEntity = sessionEntity;
    }

}

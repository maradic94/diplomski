/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import app_logic.UserBean;
import app_logic.UserBeanLocal;
import entity.SessionEntity;
import entity.User;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import util.Message;

/**
 *
 * @author Korisnik
 */
@ManagedBean(name = "fileUploadController")
public class FileUploadController {

    private String messageContent = "";
    private int alertType;
    
    
    UserBeanLocal userBean;

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

    public void upload(FileUploadEvent event) {
        userBean = new UserBean();
        SessionEntity sessionEntity = util.Util.getSessionEntity();
        User currentUser = sessionEntity.getMyUser();
        Random r = new Random();
        int broj = r.nextInt(100000);
        try {
            //Return the FacesContext instance for the request that is being processed by the current thread.
            //FacesContext contains all of the per-request state 
            //information related to the processing of a single JavaServer Faces request
            ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
            String uploadedFileName = event.getFile().getFileName();
            String fileExt = uploadedFileName.substring(uploadedFileName.lastIndexOf('.'));
            String fileName = currentUser.getUserName() + Integer.toString(broj) + fileExt;
            File file = new File(extContext.getRealPath("//images//") + "//" + fileName);
            copyFile(file, event.getFile().getInputstream());

            updateStudentImagePath(fileName);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void updateStudentImagePath(String imagePath) {
        SessionEntity sessionEntity = util.Util.getSessionEntity();
        User currentUser = sessionEntity.getMyUser();
        currentUser.setPicture(imagePath);
        try {
            userBean.updateUser(currentUser);
            alertType = Message.ALERT_OK;
            messageContent = Message.getMessageFromResources("imageSuccessfullyChanged") + "\n";
        } catch (Exception ex) {
            alertType = Message.ALERT_DANGER;
            messageContent = Message.getMessageFromResources("msgBadRegister") + "\n";
        }
    }

    private void copyFile(File newFile, InputStream in) {
        try {
            OutputStream out = new FileOutputStream(newFile);
            int read = 0;
            //KADA DODJE DO KRAJA INPUT STRIMA IN.READ = -1,
            //ISPISUJE U AUTPUT PROCITANE BAJTOVE
            byte[] bytes = new byte[1024];

            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            in.close();
            out.flush();
            out.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}

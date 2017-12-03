/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import controllers.ScheduleController;
import entity.SessionEntity;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

public class Util {

    public static final int TYPE_ADMIN = 1;
    public static final int TYPE_STUDENT = 2;
    public static final int TYPE_TEACHER = 3;

    public static SessionEntity getSessionEntity() {
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        SessionEntity sessionEntity = (SessionEntity) httpSession.getAttribute("sessionEntity");
        return sessionEntity;
    }
    
    public static ScheduleController getSessionScheduleController() {
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        ScheduleController scheduleController = (ScheduleController) httpSession.getAttribute("scheduleController");
        return scheduleController;
    }

}

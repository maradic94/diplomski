/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ResourceBundle;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;


public class Message {

    public static final int ALERT_OK = 1;
    public static final int ALERT_DANGER = 2;
    public static final int ALERT_WARNING = 3;
    public static final int ALERT_INFO = 4;

    public static String getMessageFromResources(String key) {
        FacesContext context = FacesContext.getCurrentInstance();
        Application app = context.getApplication();
        ResourceBundle text = app.getResourceBundle(context, "msgs");
        return text.getString(key);
    }
}

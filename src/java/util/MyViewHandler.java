/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.sun.faces.application.view.MultiViewHandler;
import java.util.Locale;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Korisnik
 */
public class MyViewHandler extends MultiViewHandler {
    public Locale calculateLocale(FacesContext context) {
        HttpSession session = (HttpSession) context.getExternalContext()
            .getSession(false);
        if (session != null) {
            Locale locale = (Locale) session.getAttribute("locale");
            if (locale != null) {
                return locale;
            }
        }
       return super.calculateLocale(context);
    }
    
}

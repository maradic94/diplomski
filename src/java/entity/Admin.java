/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;


@Entity
public class Admin extends User implements Serializable {

    public Admin() {
    }

    public Admin(String userName, String userPass, String firstName, String surrName, Gender gender, String city, String JMBG, String telephone, String email, String picture) {
        super(userName, userPass, firstName, surrName, gender, city, JMBG, telephone, email, picture);
    }

}

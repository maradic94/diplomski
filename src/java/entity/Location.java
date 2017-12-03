/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;


@Entity
@NamedQueries(
        value = {
            @NamedQuery(name = "allLocations", query = "from Location")
        }
)
public class Location implements Serializable{

    @Id
    @GeneratedValue
    private int locationID;

    @Column(name = "location_name", nullable = false, unique = true)
    private String locationName;

    public Location() {
    }

    
    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

}

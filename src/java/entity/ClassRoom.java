/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;


@Entity
@NamedQueries(
        value = {
            @NamedQuery(name = "allClsRoom", query = "from ClassRoom where isDeleted = 0")
        }
)
public class ClassRoom implements Serializable{

    @Id
    @GeneratedValue
    private int classRoomID;

    @Column(nullable = false, name = "classroom_name", unique = true)
    private String classRoomName;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false, name = "computer_num")
    private int computerNum;

    @Column(nullable = false, name = "has_whiteboard")
    private boolean hasWhiteboard;

    // ako ima nastavnicki - ima projektor
    @Column(nullable = false, name = "has_main_computer")
    private boolean hasMainComputer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "locationID")
    private Location location;

    @Transient
    private int previousCapacity;

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted;

    public ClassRoom() {
    }

    public int getClassRoomID() {
        return classRoomID;
    }

    public void setClassRoomID(int classRoomID) {
        this.classRoomID = classRoomID;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        previousCapacity = this.capacity;
        this.capacity = capacity;
    }

    public int getComputerNum() {
        return computerNum;
    }

    public void setComputerNum(int computerNum) {
        this.computerNum = computerNum;
    }

    public boolean isHasWhiteboard() {
        return hasWhiteboard;
    }

    public void setHasWhiteboard(boolean hasWhiteboard) {
        this.hasWhiteboard = hasWhiteboard;
    }

    public boolean isHasMainComputer() {
        return hasMainComputer;
    }

    public void setHasMainComputer(boolean hasMainComputer) {
        this.hasMainComputer = hasMainComputer;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getClassRoomName() {
        return classRoomName;
    }

    public void setClassRoomName(String classRoomName) {
        this.classRoomName = classRoomName;
    }

    public int getPreviousCapacity() {
        return previousCapacity;
    }

    public void setPreviousCapacity(int previousCapacity) {
        this.previousCapacity = previousCapacity;
    }

    public boolean isIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

}

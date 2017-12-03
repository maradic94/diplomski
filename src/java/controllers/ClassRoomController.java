/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import app_logic.ClassRoomBean;
import app_logic.LocationBean;
import app_logic.ReservationBean;
import entity.ClassRoom;
import entity.Location;
import entity.Reservation;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import util.Message;
import app_logic.ClassRoomBeanRemote;
import app_logic.LocationBeanRemote;
import app_logic.ReservationBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author Korisnik
 */
@ManagedBean(name = "classRoomController")
@RequestScoped
public class ClassRoomController {

    private List<ClassRoom> allClassRooms;
    private List<Location> allLocations;
    private ClassRoom classRoom;
    
    
    LocationBeanRemote locationBean;
    
    
    ClassRoomBeanRemote classRoomBean;
    
    
    ReservationBeanRemote reservationBean;
    private int alertType;
    private String messageContent;

    private int capacity;
    private int computerNum;
    private boolean hasMain;
    private boolean hasWhiteBoard;
    private String location;
    private String classRoomName;

    //edit
    private String newLocation;

    private void initLocations() {
        try {
            allLocations = locationBean.getAllLocations();
        } catch (Exception ex) {
            System.out.println("Locations not loaded");
        }
    }

    private void initClassRooms() {
        try {
            allClassRooms = classRoomBean.getAllClassRooms();
        } catch (Exception ex) {
            System.out.println("ClassRooms not loaded");
        }
    }

    public ClassRoomController() {
        locationBean = new LocationBean();
        classRoomBean = new ClassRoomBean();
        reservationBean = new ReservationBean();
        initLocations();
        initClassRooms();
    }

    private void saveClassRoom() {
        alertType = Message.ALERT_OK;
        messageContent = Message.getMessageFromResources("msgOkAddClassRoom") + "\n";
        try {
            classRoomBean.saveClassRoom(classRoom);
        } catch (Exception ex) {
            alertType = Message.ALERT_DANGER;
            messageContent = Message.getMessageFromResources("msgBadRegister") + "\n";
        }
        resetAllData();
    }

    public void deleteClassRoom(ClassRoom classRoom) {
        alertType = Message.ALERT_OK;
        messageContent = Message.getMessageFromResources("msgOkDeleteClassRoom") + "\n";

        List<Reservation> list = getReservationForClassroom(classRoom);
        if (list != null && list.size() > 0) {
            classRoom.setCapacity(classRoom.getPreviousCapacity());
            classRoom.setPreviousCapacity(classRoom.getCapacity());
            alertType = Message.ALERT_WARNING;
            messageContent = Message.getMessageFromResources("noDeleteClassroom") + "\n";
            return;
        }

        classRoom.setIsDeleted(true);
        try {
            classRoomBean.updateClassRoom(classRoom);
            allClassRooms.remove(classRoom);
        } catch (Exception ex) {
        }
    }

    private void resetAllData() {
        classRoom = null;
        capacity = 0;
        computerNum = 0;
        hasMain = false;
        hasWhiteBoard = false;
        location = null;
        classRoomName = null;
    }

    public void insertNewClassRoom() {
        classRoom = new ClassRoom();
        classRoom.setCapacity(capacity);
        classRoom.setComputerNum(computerNum);
        classRoom.setHasMainComputer(hasMain);
        classRoom.setHasWhiteboard(hasWhiteBoard);
        classRoom.setClassRoomName(classRoomName);

        Location myLoc = null;
        for (Location l : allLocations) {
            if (l.getLocationName().equals(location)) {
                myLoc = l;
                break;
            }
        }

        classRoom.setLocation(myLoc);

        saveClassRoom();
    }

    private void updateClassRoom(ClassRoom clsR) {
        alertType = Message.ALERT_OK;
        messageContent = Message.getMessageFromResources("succEditClassroom") + "\n";

        try {
            classRoomBean.updateClassRoom(clsR);
        } catch (Exception ex) {
            alertType = Message.ALERT_DANGER;
            messageContent = Message.getMessageFromResources("msgBadRegister") + "\n";
        }
        resetAllData();
    }

    private List<Reservation> getReservationForClassroom(ClassRoom cls) {
        List<Reservation> listReservationForClassroom = null;
        try {
            listReservationForClassroom = reservationBean.getReservationForClassroom(cls);
        } catch (Exception ex) {
            System.out.println("Reservations not loaded");
        }
        return listReservationForClassroom;
    }

    public void saveEditedClassRoom(ClassRoom cls) {
        if (cls.getCapacity() < cls.getPreviousCapacity()) {
            List<Reservation> list = getReservationForClassroom(cls);
            if (list != null && list.size() > 0) {
                cls.setCapacity(cls.getPreviousCapacity());
                cls.setPreviousCapacity(cls.getCapacity());
                alertType = Message.ALERT_WARNING;
                messageContent = Message.getMessageFromResources("capacityLowerNotAllowed") + "\n";
                return;
            }

        }

        updateClassRoom(cls);
    }

    public List<Location> getAllLocations() {
        return allLocations;
    }

    public void setAllLocations(List<Location> allLocations) {
        this.allLocations = allLocations;
    }

    public ClassRoom getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(ClassRoom classRoom) {
        this.classRoom = classRoom;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getComputerNum() {
        return computerNum;
    }

    public void setComputerNum(int computerNum) {
        this.computerNum = computerNum;
    }

    public boolean isHasMain() {
        return hasMain;
    }

    public void setHasMain(boolean hasMain) {
        this.hasMain = hasMain;
    }

    public boolean isHasWhiteBoard() {
        return hasWhiteBoard;
    }

    public void setHasWhiteBoard(boolean hasWhiteBoard) {
        this.hasWhiteBoard = hasWhiteBoard;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getClassRoomName() {
        return classRoomName;
    }

    public void setClassRoomName(String classRoomName) {
        this.classRoomName = classRoomName;
    }

    public List<ClassRoom> getAllClassRooms() {
        return allClassRooms;
    }

    public void setAllClassRooms(List<ClassRoom> allClassRooms) {
        this.allClassRooms = allClassRooms;
    }

    public String getNewLocation() {
        return newLocation;
    }

    public void setNewLocation(String newLocation) {
        this.newLocation = newLocation;
    }

}

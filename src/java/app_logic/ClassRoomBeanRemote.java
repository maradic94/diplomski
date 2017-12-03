/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app_logic;

import entity.ClassRoom;
import entity.Reservation;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Korisnik
 */
@Local
public interface ClassRoomBeanRemote {
    
    public List<ClassRoom> getAllClassRooms() throws Exception;
    public void saveClassRoom(ClassRoom clsr) throws Exception;
    public void updateClassRoom(ClassRoom clsr) throws Exception;
    public List<ClassRoom> getSearchedClassRooms(Reservation newReservation);
    public List<ClassRoom> getSearchedClassRooms(Date startDate, Date endDate);
    
}

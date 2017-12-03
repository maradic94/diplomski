/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app_logic;

import entity.ClassRoom;
import entity.Reservation;
import entity.User;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Korisnik
 */
@Local
public interface ReservationBeanRemote {
     public List<Reservation> getReservationForClassroom(ClassRoom cls) throws Exception;
     public List<Reservation> getAllReservations(User teacher);
     public List<Reservation> getAllReservations(Date dateS, Date dateE);
     public List<Reservation> getPassedReservations(User teacher);
     public void updateReservation(Reservation reservation) throws Exception;
     public List<Reservation> getReservationsForClassRoomOnly(ClassRoom clsr);
     public void saveReservation(Reservation reservation) throws Exception;
     public List<Reservation> getReservationsByDateAndClassRoom(ClassRoom clsr, Date startDate, Date endDate);
    
}

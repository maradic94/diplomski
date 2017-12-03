/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import app_logic.ClassRoomBean;
import app_logic.ReservationBean;
import entity.ClassRoom;
import entity.Course;
import entity.Reservation;
import entity.SessionEntity;
import entity.Teacher;
import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import util.Message;
import util.Util;
import app_logic.ClassRoomBeanRemote;
import app_logic.ReservationBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author Korisnik
 */
@ManagedBean(name = "teacherSearchClassroomController")
@SessionScoped
public class TeacherSearchClassroomController {

    private Date selectedDate = new Date();
    private int selectedHours = 8;
    private int selectedMin = 0;

    private int selectedHoursDuration = 0;
    private int selectedMinDuration = 0;
    
    
    ReservationBeanRemote reservationBean;
    private boolean enableAdvance;
    
    
    ClassRoomBeanRemote classRoomBean;
    private int minCapacity;
    private int minComputerNum;
    private boolean hasMain;
    private boolean hasWhiteboard;
    private boolean allowMultiple;

    private Date dateStartReservation;
    private Date dateEndReservation;

    private List<ClassRoom> searchedClassRooms;
    //  for multiple reservations
    private List<ClassRoom> multipleReservationClassRooms;
    private List<ClassroomReservationCombination> combinations;

    private Reservation newReservation = new Reservation();
    private Course selectedCourse;
    private int purpose;

    private int alertType;
    private String messageContent = "";

    private int messageType;
    private String messageReservContent = "";

    private ClassRoom classRoom;

    public TeacherSearchClassroomController() {
        classRoomBean = new ClassRoomBean();
        reservationBean = new ReservationBean();
    }

    public List<Course> getTeachersCourses() {
        SessionEntity sessionEntity = Util.getSessionEntity();
        Teacher myTeacher = (Teacher) sessionEntity.getMyUser();

        List<Course> courses = new LinkedList<Course>();
        for (Course c : myTeacher.getTeachingCourses()) {
            courses.add(c);
        }

        Course emptyCourse = new Course();
        emptyCourse.setCourseName(Message.getMessageFromResources("none"));
        courses.add(emptyCourse);
        return courses;
    }

    public void searchAdvanced() {
        Iterator<ClassRoom> classRoomIter = searchedClassRooms.iterator();
        while (classRoomIter.hasNext()) {

            ClassRoom cls = classRoomIter.next();

            if (cls.getCapacity() < minCapacity) {
                classRoomIter.remove();
                continue;
            }

            if (cls.getComputerNum() < minComputerNum) {
                classRoomIter.remove();
                continue;
            }

            if (hasMain == true) {
                if (!cls.isHasMainComputer()) {
                    classRoomIter.remove();
                    continue;
                }
            }

            if (hasWhiteboard == true) {
                if (!cls.isHasWhiteboard()) {
                    classRoomIter.remove();
                    continue;
                }
            }
        }
    }

    private void filterCapacity() {
        Iterator<ClassRoom> classRoomIter = searchedClassRooms.iterator();
        while (classRoomIter.hasNext()) {

            ClassRoom cls = classRoomIter.next();

            if (cls.getCapacity() < minCapacity) {
                classRoomIter.remove();
                continue;
            }
        }
    }

    private void searchWithoutCapacity() {
        Iterator<ClassRoom> classRoomIter = searchedClassRooms.iterator();
        while (classRoomIter.hasNext()) {

            ClassRoom cls = classRoomIter.next();

            if (cls.getComputerNum() < minComputerNum) {
                classRoomIter.remove();
                continue;
            }

            if (hasMain == true) {
                if (!cls.isHasMainComputer()) {
                    classRoomIter.remove();
                    continue;
                }
            }

            if (hasWhiteboard == true) {
                if (!cls.isHasWhiteboard()) {
                    classRoomIter.remove();
                    continue;
                }
            }
        }
    }

    private void subset(LinkedList<ClassRoom> list, int index) {
        ClassRoom classroom = multipleReservationClassRooms.get(index);
        list.add(classroom);
        ClassroomReservationCombination newComb = new ClassroomReservationCombination();
        newComb.setListClassRooms(list);
        combinations.add(newComb);

        if (newComb.getSumCapacity() < minCapacity) {
            for (int i = index + 1; i < multipleReservationClassRooms.size(); i++) {
                subset((LinkedList<ClassRoom>) list.clone(), i);
            }
        }
    }

    private void createMultipleReservations() {
        combinations = new LinkedList<ClassroomReservationCombination>();
        int size = multipleReservationClassRooms.size();

        for (int i = 0; i < size; i++) {
            subset(new LinkedList<ClassRoom>(), i);
        }

        Iterator<ClassroomReservationCombination> iter = combinations.iterator();
        while (iter.hasNext()) {
            ClassroomReservationCombination comb = iter.next();
            if (comb.getSumCapacity() < minCapacity) {
                iter.remove();
            }

        }

        for (ClassroomReservationCombination c : combinations) {
            c.dump();
        }

    }

    private void copySearchedClassRooms() {
        multipleReservationClassRooms = new LinkedList<ClassRoom>();
        for (ClassRoom cls : searchedClassRooms) {
            multipleReservationClassRooms.add(cls);
        }
        multipleReservationClassRooms.sort(new Comparator<ClassRoom>() {

            @Override
            public int compare(ClassRoom o1, ClassRoom o2) {
                return o2.getCapacity() - o1.getCapacity();
            }
        });
    }

    public void search() {
        if ((selectedHoursDuration * 60 + selectedMinDuration) < 30) {
            alertType = Message.ALERT_WARNING;
            messageContent = Message.getMessageFromResources("errorDateDuration") + "\n";
            return;
        }

        dateStartReservation = new Date(selectedDate.getTime());
        dateStartReservation.setHours(selectedHours);
        dateStartReservation.setMinutes(selectedMin);

        long time = dateStartReservation.getTime() + (selectedHoursDuration * 60 + selectedMinDuration) * 60000;
        dateEndReservation = new Date(time);

        searchedClassRooms = classRoomBean.getSearchedClassRooms(dateStartReservation, dateEndReservation);

//        Session session = HibernateUtil.openSession();
//        session.beginTransaction();
//        try {
//            searchedClassRooms = (List<ClassRoom>) session.createQuery(
//                    "SELECT C FROM ClassRoom C  WHERE C.classRoomID NOT IN "
//                    + "     ( SELECT R.classRoom.classRoomID FROM Reservation R WHERE "
//                    + "R.isCanceled=0 AND :termStart < R.endDate AND :termEnd > R.startDate)")
//                    .setParameter("termStart", dateStartReservation)
//                    .setParameter("termEnd", dateEndReservation)
//                    .list();
//            session.getTransaction().commit();
//        } finally {
//            session.close();
//        }
        if (enableAdvance) {
            if (allowMultiple) {
                searchWithoutCapacity();
                copySearchedClassRooms();

                filterCapacity();

                if (searchedClassRooms.size() == 0) {
                    createMultipleReservations();
                }
            } else {
                searchAdvanced();
            }
        }

        alertType = 0;
        messageContent = "";

        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("./teacher_searched_classrooms.xhtml?faces-redirect=true");
        } catch (IOException ex) {
            Logger.getLogger(TeacherSearchClassroomController.class.getName()).log(Level.SEVERE, null, ex);
        }

//        Logger.getLogger(ActivityController.class.getName()).log(Level.INFO, "TIME START " + dateStartReservation.toString());
//        Logger.getLogger(ActivityController.class.getName()).log(Level.INFO, "TIME END " + dateEndReservation.toString());
    }

    public void backToSearch() {
        alertType = 0;
        messageContent = "";
        messageType = 0;
        messageReservContent = "";
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("./teacher_search_classroom.xhtml?faces-redirect=true");
        } catch (IOException ex) {
            Logger.getLogger(TeacherSearchClassroomController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void reserveCombination(ClassroomReservationCombination comb) {
        Logger.getLogger(ActivityController.class.getName()).log(Level.INFO, "USAO U REZERV" + newReservation.getCourse().getCourseName());

        if (newReservation.getDescription() == null
                || newReservation.getDescription().trim().equals("")) {

            messageType = Message.ALERT_WARNING;
            messageReservContent = Message.getMessageFromResources("descriptionEmpty");
            return;
        }
        if (newReservation.getCourse().getCourseName().equals(Message.getMessageFromResources("none"))) {
            newReservation.setCourse(null);
        }
        for (ClassRoom clsRoom : comb.getClassrooms()) {

            Date dateS = new Date(selectedDate.getTime());
            dateS.setHours(selectedHours);
            dateS.setMinutes(selectedMin);

            long time = dateS.getTime() + (selectedHoursDuration * 60 + selectedMinDuration) * 60000;
            Date dateE = new Date(time);

            newReservation.setStartDate(dateS);
            newReservation.setEndDate(dateE);

            newReservation.setClassRoom(clsRoom);
            newReservation.setPurpose(Reservation.Purpose.values()[purpose - 1]);

            SessionEntity sessionEntity = Util.getSessionEntity();
            newReservation.setTeacher((Teacher) sessionEntity.getMyUser());

            messageType = Message.ALERT_OK;
            messageReservContent = Message.getMessageFromResources("msgSuccessfullyReservated");

            try {
                reservationBean.saveReservation(newReservation);

            } catch (Exception ex) {
                messageType = Message.ALERT_DANGER;
                messageReservContent = Message.getMessageFromResources("msgBadRegister") + "\n";
            }

        }

        newReservation = new Reservation();
    }

    public void reserveClassRoom(ClassRoom clsRoom) {
        Logger.getLogger(ActivityController.class.getName()).log(Level.INFO, "USAO U REZERV" + newReservation.getCourse().getCourseName());

        if (newReservation.getDescription() == null
                || newReservation.getDescription().trim().equals("")) {

            messageType = Message.ALERT_WARNING;
            messageReservContent = Message.getMessageFromResources("descriptionEmpty");
            return;
        }

        if (newReservation.getCourse().getCourseName().equals(Message.getMessageFromResources("none"))) {
            newReservation.setCourse(null);
        }

        Date dateS = new Date(selectedDate.getTime());
        dateS.setHours(selectedHours);
        dateS.setMinutes(selectedMin);

        long time = dateS.getTime() + (selectedHoursDuration * 60 + selectedMinDuration) * 60000;
        Date dateE = new Date(time);

        newReservation.setStartDate(dateS);
        newReservation.setEndDate(dateE);

        newReservation.setClassRoom(clsRoom);
        newReservation.setPurpose(Reservation.Purpose.values()[purpose - 1]);

        SessionEntity sessionEntity = Util.getSessionEntity();
        newReservation.setTeacher((Teacher) sessionEntity.getMyUser());

        messageType = Message.ALERT_OK;
        messageReservContent = Message.getMessageFromResources("msgSuccessfullyReservated");

        try {
            reservationBean.saveReservation(newReservation);

        } catch (Exception ex) {
            messageType = Message.ALERT_DANGER;
            messageReservContent = Message.getMessageFromResources("msgBadRegister") + "\n";
        }

        searchedClassRooms.remove(clsRoom);
        newReservation = new Reservation();
    }

    public int getSelectedHoursDuration() {
        return selectedHoursDuration;
    }

    public void setSelectedHoursDuration(int selectedHoursDuration) {
        this.selectedHoursDuration = selectedHoursDuration;
    }

    public int getSelectedMinDuration() {
        return selectedMinDuration;
    }

    public void setSelectedMinDuration(int selectedMinDuration) {
        this.selectedMinDuration = selectedMinDuration;
    }

    public boolean isEnableAdvance() {
        return enableAdvance;
    }

    public void setEnableAdvance(boolean enableAdvance) {
        this.enableAdvance = enableAdvance;
    }

    public void setAdvancedSearch() {
        //  enableAdvance = !enableAdvance;
    }

    public int getMinCapacity() {
        return minCapacity;
    }

    public void setMinCapacity(int minCapacity) {
        this.minCapacity = minCapacity;
    }

    public int getMinComputerNum() {
        return minComputerNum;
    }

    public void setMinComputerNum(int minComputerNum) {
        this.minComputerNum = minComputerNum;
    }

    public boolean isHasMain() {
        return hasMain;
    }

    public void setHasMain(boolean hasMain) {
        this.hasMain = hasMain;
    }

    public boolean isHasWhiteboard() {
        return hasWhiteboard;
    }

    public void setHasWhiteboard(boolean hasWhiteboard) {
        this.hasWhiteboard = hasWhiteboard;
    }

    public boolean isAllowMultiple() {
        return allowMultiple;
    }

    public void setAllowMultiple(boolean allowMultiple) {
        this.allowMultiple = allowMultiple;
    }

    public int getSelectedHours() {
        return selectedHours;
    }

    public void setSelectedHours(int selectedHours) {
        this.selectedHours = selectedHours;
    }

    public int getSelectedMin() {
        return selectedMin;
    }

    public void setSelectedMin(int selectedMin) {
        this.selectedMin = selectedMin;
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }

    public List<ClassRoom> getSearchedClassRooms() {
        return searchedClassRooms;
    }

    public void setSearchedClassRooms(List<ClassRoom> searchedClassRooms) {
        this.searchedClassRooms = searchedClassRooms;
    }

    public Date getDateStartReservation() {
        return dateStartReservation;
    }

    public void setDateStartReservation(Date dateStartReservation) {
        this.dateStartReservation = dateStartReservation;
    }

    public Date getDateEndReservation() {
        return dateEndReservation;
    }

    public void setDateEndReservation(Date dateEndReservation) {
        this.dateEndReservation = dateEndReservation;
    }

    public Reservation getNewReservation() {
        return newReservation;
    }

    public void setNewReservation(Reservation newReservation) {
        this.newReservation = newReservation;
    }

    public Course getSelectedCourse() {
        return selectedCourse;
    }

    public void setSelectedCourse(Course selectedCourse) {
        this.selectedCourse = selectedCourse;
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

    public int getPurpose() {
        return purpose;
    }

    public void setPurpose(int purpose) {
        this.purpose = purpose;
    }

    public ClassRoom getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(ClassRoom classRoom) {
        this.classRoom = classRoom;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getMessageReservContent() {
        return messageReservContent;
    }

    public void setMessageReservContent(String messageReservContent) {
        this.messageReservContent = messageReservContent;
    }

    public List<ClassRoom> getMultipleReservationClassRooms() {
        return multipleReservationClassRooms;
    }

    public void setMultipleReservationClassRooms(List<ClassRoom> multipleReservationClassRooms) {
        this.multipleReservationClassRooms = multipleReservationClassRooms;
    }

    public List<ClassroomReservationCombination> getCombinations() {
        return combinations;
    }

    public void setCombinations(List<ClassroomReservationCombination> combinations) {
        this.combinations = combinations;
    }

}

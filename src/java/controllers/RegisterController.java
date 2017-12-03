/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import app_logic.CourseBean;
import app_logic.ModuleBean;
import app_logic.UserBean;
import app_logic.UserBeanLocal;
import util.Message;
import entity.Admin;
import entity.Course;
import entity.Module;
import entity.Student;
import entity.Student.Degree;
import entity.Teacher;
import entity.User;
import entity.User.Gender;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import app_logic.CourseBeanRemote;
import app_logic.ModuleBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author Korisnik
 */
@ManagedBean(name = "registerController")
@RequestScoped
public class RegisterController {

    public static String USER_PATH = "user.jpg";

    public static final int TYPE_ADMIN = 1;
    public static final int TYPE_STUDENT = 2;
    public static final int TYPE_TEACHER = 3;

    private static Pattern passwordRepeatCharPattern = Pattern.compile("(.)\\1{2}");
    private static Pattern passwordStartCharPattern = Pattern.compile("^([a-z]|[A-Z])");
    private static Pattern passwordContainsPattern = Pattern.compile("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])");
    
    
    ModuleBeanRemote moduleBean;
    
    
    CourseBeanRemote courseBean;
    
    
    UserBeanLocal userBean;
    
    private String userName;
    private String userPass;
    private String userConfirmPass;
    private String userFirstName;
    private String userSurrname;
    private String userEmail;
    private String userTelephone;
    private int userCity;
    private String userJMBG;
    private int userGender = 1;
    private String userCityString = Message.getMessageFromResources("Belgrade");

    private int studentYear = 1;
    private String studentModule;

    private int teacherEducation;
    private int teacherOffice;
    private List<String> teacherCourses;

    private List<Module> allModules;
    private List<Course> allCourses;

    private Admin admin;
    private Teacher teacher;
    private Student student;

    private int alertType;
    private String messageContent;

    private void initModules() {
        allModules = moduleBean.getAllModules();
    }

    private void initCourses() {
        allCourses = courseBean.getAllCourses();
    }

    public RegisterController() {
        moduleBean = new ModuleBean();
        courseBean = new CourseBean();
        userBean = new UserBean();
        initModules();
        initCourses();

    }

    private boolean checkPasswordLengthOk() {
        if (userPass.length() < 6 || userPass.length() > 12) {
            return false;
        }
        return true;
    }

    private boolean checkPasswordRepeatCharOk() {
        Matcher matcher = passwordRepeatCharPattern.matcher(userPass);
        if (matcher.find()) {
            return false;
        }
        return true;
    }

    private boolean checkPasswordStartsCharOk() {
        Matcher matcher = passwordStartCharPattern.matcher(userPass);
        if (!matcher.find()) {
            return false;
        }
        return true;
    }

    private boolean checkPasswordContainsOk() {
        Matcher matcher = passwordContainsPattern.matcher(userPass);
        if (!matcher.find()) {
            return false;
        }
        return true;
    }

    private boolean checkPasswordEquals() {
        return userConfirmPass.equals(userPass);
    }

    public void changeUserType() {
    }

    private boolean checkEmail() {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(userEmail);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    private int getRegionNum() {
        switch (userCity) {

            case 0:
                userCityString = Message.getMessageFromResources("Belgrade");
                return 71;
            case 1:
                userCityString = Message.getMessageFromResources("NewNow");
                return 80;
            case 2:
                userCityString = Message.getMessageFromResources("Loznica");
                return 77;
            case 3:
                userCityString = Message.getMessageFromResources("Kragujevac");
                return 72;
            default:
                return 0;

        }
    }

    private static int checkControlNum(String jmbg) {
        int ch00 = jmbg.charAt(0) - 48;
        int ch01 = jmbg.charAt(1) - 48;
        int ch02 = jmbg.charAt(2) - 48;
        int ch03 = jmbg.charAt(3) - 48;
        int ch04 = jmbg.charAt(4) - 48;
        int ch05 = jmbg.charAt(5) - 48;
        int ch06 = jmbg.charAt(6) - 48;
        int ch07 = jmbg.charAt(7) - 48;
        int ch08 = jmbg.charAt(8) - 48;
        int ch09 = jmbg.charAt(9) - 48;
        int ch10 = jmbg.charAt(10) - 48;
        int ch11 = jmbg.charAt(11) - 48;

        int checkSum = 11 - ((7 * (ch00 + ch06) + 6 * (ch01 + ch07) + 5 * (ch02 + ch08) + 4 * (ch03 + ch09) + 3 * (ch04 + ch10) + 2 * (ch05 + ch11)) % 11);
        if (checkSum > 9) {
            return 0;
        }
        return checkSum;
    }

    private boolean checkJMBG() {
        boolean ok = true;

        if (userJMBG.length() != 13) {
            messageContent += Message.getMessageFromResources("jmbgLengthError") + "\n";
            return true;
        }

        Gender gender = User.Gender.values()[userGender - 1];
        String uniqueNumStr = userJMBG.substring(9, 12);
        int uniqNum = -1;
        try {
            uniqNum = Integer.parseInt(uniqueNumStr);
        } catch (NumberFormatException nfe) {
            messageContent += Message.getMessageFromResources("jmbgDigitsError") + "\n";
            return false;
        }

        if (gender == Gender.FEMALE && uniqNum < 500) {
            ok = false;
            messageContent += Message.getMessageFromResources("genderJMBGError") + "\n";
        }

        if (gender == Gender.MALE && uniqNum >= 500) {
            ok = false;
            messageContent += Message.getMessageFromResources("genderJMBGError") + "\n";
        }

        if (userCity != 4) {
            String regionStr = userJMBG.substring(7, 9);
            int regionNum = -1;

            try {
                regionNum = Integer.parseInt(regionStr);
            } catch (NumberFormatException nfe) {
                messageContent += Message.getMessageFromResources("jmbgDigitsError") + "\n";
                return false;
            }

            if (regionNum != getRegionNum()) {
                messageContent += Message.getMessageFromResources("jmbgRegionError") + "\n";
                return false;
            }

        } else {
            userCityString = Message.getMessageFromResources("other");
        }

//        int chekSum = userJMBG.charAt(12) - 48;
//        if (chekSum != checkControlNum(userJMBG)) {
//            messageContent += Message.getMessageFromResources("jmbgRegionContolNumError") + "\n";
//            return false;
//        }
        return ok;
    }

    private void checkCorrectInput() {
        alertType = Message.ALERT_OK;
        messageContent = "";
        if (!checkPasswordLengthOk()) {
            alertType = Message.ALERT_WARNING;
            messageContent += Message.getMessageFromResources("passwordErrorLengthFormat") + "\n";
        }
        if (!checkPasswordContainsOk()) {
            alertType = Message.ALERT_WARNING;
            messageContent += Message.getMessageFromResources("passwordErrorContainsFormat") + "\n";

        }
        if (!checkPasswordRepeatCharOk()) {
            alertType = Message.ALERT_WARNING;
            messageContent += Message.getMessageFromResources("passwordErrorRepeatFormat") + "\n";

        }
        if (!checkPasswordStartsCharOk()) {
            alertType = Message.ALERT_WARNING;
            messageContent += Message.getMessageFromResources("passwordErrorStartsFormat") + "\n";

        }

        if (!checkPasswordEquals()) {
            alertType = Message.ALERT_WARNING;
            messageContent += Message.getMessageFromResources("confirmPassOldPass") + "\n";

        }

        if (!checkEmail()) {
            alertType = Message.ALERT_WARNING;
            messageContent += Message.getMessageFromResources("emailFormatError") + "\n";
        }

        if (!checkJMBG()) {
            alertType = Message.ALERT_WARNING;
        }
    }

    private void saveAdmin() {

        try {
            userBean.saveUser(admin);
        } catch (Exception ex) {
            alertType = Message.ALERT_DANGER;
            messageContent += Message.getMessageFromResources("msgBadRegister") + "\n";
        }

    }

    private void insertNewAdmin() {
        admin = new Admin(userName, userPass, userFirstName, userSurrname,
                User.Gender.values()[userGender - 1], userCityString, userJMBG, userTelephone, userEmail, USER_PATH);
        saveAdmin();
        resetAllData();
    }

    public void registerAdmin() {
        checkCorrectInput();
        if (alertType == Message.ALERT_OK) {
            insertNewAdmin();
        }
    }

    private Student.Degree getStudentsDegree() {
        char degreeLetter = userName.charAt(8);
        switch (degreeLetter) {
            case 'd':
                return Student.Degree.AKADEMSKE;
            case 'm':
                return Student.Degree.MASTER;
            case 'p':
                return Student.Degree.DOKTORSKE;
            default:
                alertType = Message.ALERT_WARNING;
                messageContent += Message.getMessageFromResources("studentDegreeError") + "\n";
                return null;
        }
    }

    private void checkOtherStudentDataOk() {
        if (userName.length() != 9) {
            alertType = Message.ALERT_WARNING;
            messageContent += Message.getMessageFromResources("studentUserNameLengthError") + "\n";
        }
        //prezime
//        if (userName.charAt(0) != userSurrname.charAt(0) && userName.charAt(0) != (userSurrname.charAt(0) + 32)
//                && (userName.charAt(0) + 32) != userSurrname.charAt(0)) {
//            alertType = Message.ALERT_WARNING;
//            messageContent += Message.getMessageFromResources("studentSurrnameLetterError") + "\n";
//        }
//
//        if (userName.charAt(1) != userFirstName.charAt(0) && userName.charAt(1) != (userFirstName.charAt(0) + 32)
//                && (userName.charAt(1) + 32) != userFirstName.charAt(0)) {
//            alertType = Message.ALERT_WARNING;
//            messageContent += Message.getMessageFromResources("studentFirstNameLetterError") + "\n";
//        }

    }

    private void saveStudent() {

        List<Module> modules = moduleBean.getModuleByName(studentModule);
        student.setModule(modules.get(0));
        try {
            userBean.saveUser(student);
        } catch (Exception e) {
            //toDo
            alertType = Message.ALERT_DANGER;
            messageContent += Message.getMessageFromResources("msgBadRegister") + "\n";
        }

    }

    private void insertNewStudent() {
        Degree degree = getStudentsDegree();
        if (alertType == Message.ALERT_OK) {
            Module module = null;
            for (Module m : allModules) {
                if (m.getModuleName().equals(studentModule)) {
                    module = m;
                    break;
                }
            }

            student = new Student(module, studentYear, degree, userName, userPass, userFirstName, userSurrname, User.Gender.values()[userGender - 1],
                    userCityString, userJMBG, userTelephone, userEmail, USER_PATH);
            saveStudent();
            resetAllData();
        }

    }

    private void saveTeacher() {

        for (String courseName : teacherCourses) {
            List<Course> courses = courseBean.getTeacherCourses(courseName);
            Course course = courses.get(0);
            course.getTeachers().add(teacher);
            teacher.getTeachingCourses().add(course);
        }
        try {
            userBean.saveUser(teacher);
        } catch (Exception e) {
            //toDo
            alertType = Message.ALERT_DANGER;
            messageContent += Message.getMessageFromResources("msgBadRegister") + "\n";

        }

    }

    public void registerStudent() {
        checkCorrectInput();
        checkOtherStudentDataOk();
        if (alertType == Message.ALERT_OK) {
            insertNewStudent();
        }
    }

    public void resetAllData() {
        userName = null;
        userPass = null;
        userConfirmPass = null;
        userCityString = null;
        userEmail = null;
        userFirstName = null;
        userGender = 0;
        userJMBG = null;
        userSurrname = null;
        userTelephone = null;
        userCity = 0;
    }

    private void insertNewTeacher() {
        Teacher.Education education = Teacher.Education.values()[teacherEducation - 1];
        teacher = new Teacher(education, teacherOffice, userName, userPass, userFirstName, userSurrname,
                User.Gender.values()[userGender - 1], userCityString, userJMBG, userTelephone, userEmail, USER_PATH);
        saveTeacher();
        resetAllData();
    }

    public void registerTeacher() {
        checkCorrectInput();
        if (alertType == Message.ALERT_OK) {
            insertNewTeacher();
        }

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getUserConfirmPass() {
        return userConfirmPass;
    }

    public void setUserConfirmPass(String userConfirmPass) {
        this.userConfirmPass = userConfirmPass;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserSurrname() {
        return userSurrname;
    }

    public void setUserSurrname(String userSurrname) {
        this.userSurrname = userSurrname;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserTelephone() {
        return userTelephone;
    }

    public void setUserTelephone(String userTelephone) {
        this.userTelephone = userTelephone;
    }

    public int getUserCity() {
        return userCity;
    }

    public void setUserCity(int userCity) {
        this.userCity = userCity;
    }

    public String getUserJMBG() {
        return userJMBG;
    }

    public void setUserJMBG(String userJMBG) {
        this.userJMBG = userJMBG;
    }

    public int getUserGender() {
        return userGender;
    }

    public void setUserGender(int userGender) {
        this.userGender = userGender;
    }

    public int getStudentYear() {
        return studentYear;
    }

    public void setStudentYear(int studentYear) {
        this.studentYear = studentYear;
    }

    public String getStudentModule() {
        return studentModule;
    }

    public void setStudentModule(String studentModule) {
        this.studentModule = studentModule;
    }

    public int getTeacherEducation() {
        return teacherEducation;
    }

    public void setTeacherEducation(int teacherEducation) {
        this.teacherEducation = teacherEducation;
    }

    public int getTeacherOffice() {
        return teacherOffice;
    }

    public void setTeacherOffice(int teacherOffice) {
        this.teacherOffice = teacherOffice;
    }

    public List<String> getTeacherCourses() {
        return teacherCourses;
    }

    public void setTeacherCourses(List<String> teacherCourses) {
        this.teacherCourses = teacherCourses;
    }

    public List<Module> getAllModules() {
        return allModules;
    }

    public void setAllModules(List<Module> allModules) {
        this.allModules = allModules;
    }

    public List<Course> getAllCourses() {
        return allCourses;
    }

    public void setAllCourses(List<Course> allCourses) {
        this.allCourses = allCourses;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
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

}

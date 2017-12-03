 package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

@Entity
//ovo mi treba jer moju klasu User nasledjuju Admin,Teacher,User. Po ovoj koloni se diskriminisu
@DiscriminatorColumn(name = "userType")
@NamedQueries(
        value = {
            //za logovanje
            @NamedQuery(name = "findUserByNamePass", query = "from User where userName = :userName and userPass= :userPass"),
            @NamedQuery(name = "korisnikNameLike", query = "from User where userName like :filter"),
            //kada admin odabere opciju: Approve requests, trazi prikaz svih usera koji nisu odobreni, ni deaktivirani
            @NamedQuery(name = "findNotApprovedUsers", query = "from User where isDeactivated = 0 and isApproved = 0"),
            @NamedQuery(name = "findAllUsers", query = "from User where isDeactivated = 0 and userName != :userName")

        }
)
//strategija nasledjivanja, posebna klasa za svaku od klasa koje nasledjuju.
//prednost jer kad bi dodavala nove subklase.
//prednost jer su tabele normalizovane, nema redudantnosti
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements Serializable {

    public enum Gender {

        MALE, FEMALE
    }

    @Id
    //userID je autoinkrement, ofc
    @GeneratedValue
    protected int userID;

    @Column(nullable = false, unique = true, name = "username")
    protected String userName;

    @Column(nullable = false, name = "userpass")
    protected String userPass;

    @Column(nullable = false, name = "firstname")
    protected String firstName;

    @Column(nullable = false, name = "surrname")
    protected String surrName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    protected Gender gender;

    @Column(nullable = false)
    protected String city;

    @Column(nullable = false, length = 13, unique = true, name = "jmbg")
    protected String JMBG;

    protected String telephone;
    protected String email;
    protected String picture;

    @Column(nullable = false, name = "is_approved")
    protected Integer isApproved;

    @Column(nullable = false, name = "is_deactivated")
    protected Integer isDeactivated;

    @Transient
    private String type;

    @Transient
    private int typeNum;

    public User() {
    }

    public User(String userName, String userPass, String firstName, String surrName, Gender gender, String city, String JMBG, String telephone, String email, String picture) {
        this.userName = userName;
        this.userPass = userPass;
        this.firstName = firstName;
        this.surrName = surrName;
        this.gender = gender;
        this.city = city;
        this.JMBG = JMBG;
        this.telephone = telephone;
        this.email = email;
        this.picture = picture;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurrName() {
        return surrName;
    }

    public void setSurrName(String surrName) {
        this.surrName = surrName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getJMBG() {
        return JMBG;
    }

    public void setJMBG(String JMBG) {
        this.JMBG = JMBG;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Integer isApproved) {
        this.isApproved = isApproved;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTypeNum() {
        return typeNum;
    }

    public void setTypeNum(int typeNum) {
        this.typeNum = typeNum;
    }

    public int getIsDeactivated() {
        return isDeactivated;
    }

    public void setIsDeactivated(Integer isDeactivated) {
        this.isDeactivated = isDeactivated;
    }

    @Override
    public String toString() {
        return "User{" + "userID=" + userID + ", userName=" + userName + ", userPass=" + userPass + ", firstName=" + firstName + ", surrName=" + surrName + ", gender=" + gender + ", city=" + city + ", JMBG=" + JMBG + ", telephone=" + telephone + ", email=" + email + ", picture=" + picture + ", isApproved=" + isApproved + '}';
    }

    public boolean isMale() {
        return gender == Gender.MALE;
    }

}

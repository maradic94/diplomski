/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app_logic;

import entity.User;
import java.util.List;
import javax.ejb.Local;


/**
 *
 * @author Korisnik
 */
@Local

public interface UserBeanLocal {
    
    public List<User> userLogin(String username, String pass) throws Exception;
    public void updateUser(User user) throws Exception;
    public List<User> findByUserNameAndPass(String username, String pass) throws Exception;
    public void saveUser(User user) throws Exception;
    public List<User> getUserRequests() throws Exception;
    public List<User> getUsers(String username);
    public void deleteUser(User user) throws Exception;
    
}

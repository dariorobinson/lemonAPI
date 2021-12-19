package com.revature.lemon.user;

import com.revature.lemon.common.exceptions.ResourceNotFoundException;
import com.revature.lemon.user.dtos.LoginRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    Logger logger = LogManager.getLogger();
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Checks to see if the user exists in the database already, if yes get the user from the database else add them in
     * Discord user id can never change but their discriminator and username can change
     * todo: suggest to find by id, then compare username and discriminator to loginRequest's,
     *  then if the resulting user is different, we should update the user in the users table...
     *  This can be problematic because if there was a user which has not logged in a long time
     *  and someone had their name changed on discord to the other user's and they log in, an
     *  error will occur due to the unique constraint on users
     * @param loginRequest user to authenticate
     * @return the user that was requested to be logged in
     */
    public User login(LoginRequest loginRequest){
//        System.out.println(loginRequest.toString());
        //check if user exists, return null for yes
        if (userRepository.findById(loginRequest.getId()).isPresent()){
            logger.warn("User was found in the database");
            return userRepository.findById(loginRequest.getId())
                                 .orElseThrow(Error::new);
        }
        else {//if the database doesn't have this user, save the new user info;
            logger.warn("User was not found in database, authenticating with Autho2");
            User newUser = new User(loginRequest.getId(),
                                    loginRequest.getUsername(),
                                    loginRequest.getDiscriminator());
            //TODO: Need a Centralized Exception Handling Module to handle exceptions may raised during data persistence
            userRepository.save(newUser);
            return newUser;
        }
    }
}

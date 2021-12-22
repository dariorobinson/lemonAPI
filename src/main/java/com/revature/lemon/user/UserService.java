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
     * Checks to see if the user exists in the database already, if yes get the user from the database else make a new user and add that in
     * @param loginRequest user to authenticate
     * @return the user that was requested to be logged in
     */
    public User login(LoginRequest loginRequest){

        User user = new User(loginRequest.getId(), loginRequest.getUsername(), loginRequest.getDiscriminator());
        return userRepository.save(user);

    }

    public User findUserById(String userId) {
        return userRepository.findById(userId)
                             .orElseThrow(ResourceNotFoundException::new);
    }
}

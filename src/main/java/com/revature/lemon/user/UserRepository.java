package com.revature.lemon.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    //??? discuss with Qi
    //Optional<User> findUserById(String id);

    //???? discuss with Qi
    //@Override
    //<S extends User> S save(S entity);

    User findUserByUsernameAndDiscriminator(String username, String discriminator);
}

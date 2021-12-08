package com.revature.lemon.daos;

import java.util.List;

public interface crudDAO {

    //CRUD: Create, Read, Update, Delete
    public interface CrudDAO<T> {
        //Create
        T save(T newObj);

        //Read
        List<T> findAll();
        T findById(String id);

        //Update
        boolean updateBalance(T updatedObj);

        //Remove
        boolean removeById(String id);
    }
}

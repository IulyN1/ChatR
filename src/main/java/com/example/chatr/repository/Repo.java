package com.example.chatr.repository;

import com.example.chatr.domain.Entity;
import com.example.chatr.exceptions.RepoException;

import java.util.Collection;

public interface Repo<ID, T extends Entity<ID>> {
    /**
     * Adds an object T to the repo
     *
     * @param t generic Entity object
     * @throws Exception if the operation fails
     */
    void add(T t) throws Exception;

    /**
     * Updates an object T in the repo
     *
     * @param t generic Entity object
     * @throws Exception if the operation fails
     */
    void update(T t) throws Exception;

    /**
     * Deletes an object T from the repo
     *
     * @param id the id of the generic object Entity
     * @return the deleted object
     * @throws RepoException if the object doesn't exist
     */
    T delete(ID id) throws RepoException;

    /**
     * Searches a object by ID
     *
     * @param id the id of the generic object
     * @return the found object
     * @throws RepoException if the object doesn't exist
     */
    T findById(ID id) throws RepoException;

    /**
     * Gets all the entities in the repo
     *
     * @return an iterable collection with all the entities
     */
    Collection<T> findAll();
}

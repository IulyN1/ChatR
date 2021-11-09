package repository;

import domain.Entity;
import exceptions.RepoException;

import java.util.Collection;

public interface Repo<ID, T extends Entity<ID>> {
    /**
     * Adds an object T to the repo
     * @param t generic Entity object
     * @throws Exception if the operation fails
     */
    void add(T t) throws Exception;

    /**
     * Updates an object T in the repo
     * @param t generic Entity object
     * @throws Exception if the operation fails
     */
    void update(T t) throws Exception;

    /**
     * Deletes an object T from the repo
     * @param id the id of the generic object Entity
     * @return the deleted object
     * @throws RepoException if the object doesn't exist
     */
    T delete(ID id) throws RepoException;

    /**
     * Searches a object by ID
     * @param id the id of the generic object
     * @return the found object
     * @throws RepoException if the object doesn't exist
     */
    T find_by_id(ID id) throws RepoException;

    /**
     * Gets all the entities in the repo
     * @return an iterable collection with all the entities
     */
    Collection<T> find_all();
}

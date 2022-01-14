package com.example.chatr.repository.inMemoryRepo;

import com.example.chatr.domain.Entity;
import com.example.chatr.exceptions.RepoException;
import com.example.chatr.repository.Repo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InMemoryRepo<ID, T extends Entity<ID>> implements Repo<ID, T> {
    private List<T> list;

    /**
     * Constructir for InMemoryRepo
     */
    public InMemoryRepo() {
        list = new ArrayList<>();
    }

    /**
     * Adds the object in the list
     *
     * @param t generic Entity object
     * @throws Exception if the operation fails
     */
    @Override
    public void add(T t) throws Exception {
        list.add(t);
    }

    /**
     * Updates the object
     *
     * @param t generic Entity object
     * @throws Exception if the operation fails
     */
    @Override
    public void update(T t) throws Exception {
        for (T value : list)
            if (value.getId() == t.getId()) {
                value.modify(t);
                return;
            }
        throw new RepoException("ID not existent!\n");
    }

    /**
     * Deletes the object from the list
     *
     * @param id the id of the generic object Entity
     * @return the deleted object
     * @throws RepoException if the id is not existent
     */
    @Override
    public T delete(ID id) throws RepoException {
        for (int i = 0; i < list.size(); i++)
            if (list.get(i).getId() == id) {
                return list.remove(i);
            }
        throw new RepoException("ID not existent!\n");
    }

    /**
     * Searches an object by id
     *
     * @param id the id of the generic object
     * @return the found object
     * @throws RepoException if the id is not existent
     */
    @Override
    public T findById(ID id) throws RepoException {
        for (T t : list) {
            if (t.getId() == id)
                return t;
        }
        throw new RepoException("ID not existent!\n");
    }

    /**
     * @return an iterable collection of all the objects in the list
     */
    @Override
    public Collection<T> findAll() {
        return list;
    }
}

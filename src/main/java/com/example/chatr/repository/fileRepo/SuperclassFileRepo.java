package com.example.chatr.repository.fileRepo;

import com.example.chatr.domain.Entity;
import com.example.chatr.exceptions.RepoException;
import com.example.chatr.repository.inMemoryRepo.InMemoryRepoEntity;

public abstract class SuperclassFileRepo<T extends Entity<Integer>> extends InMemoryRepoEntity<T> {
    protected String fileName;

    /**
     * Constructor for SuperclassFileRepo
     *
     * @param fileName the name of the file where the data is located
     * @throws Exception if the operation fails
     */
    public SuperclassFileRepo(String fileName) throws Exception {
        super();
        this.fileName = fileName;
        load();
    }

    /**
     * Loads the data from the file
     *
     * @throws Exception if the operation fails
     */
    protected abstract void load() throws Exception;

    /**
     * Saves the data to the file
     */
    protected abstract void save();

    /**
     * Adds an object without saving to file
     *
     * @param t generic object Entity
     * @throws Exception if the operation fails
     */
    public void addNoSave(T t) throws Exception {
        super.add(t);
    }

    /**
     * Adds an object to the repo with saving to file
     *
     * @param t generic Entity object
     * @throws Exception if the operation fails
     */
    @Override
    public void add(T t) throws Exception {
        super.add(t);
        save();
    }

    /**
     * Updates the object in the repo
     *
     * @param t generic Entity object
     * @throws Exception if the operation fails
     */
    @Override
    public void update(T t) throws Exception {
        super.update(t);
        save();
    }

    /**
     * Deletes an object from the repo
     *
     * @param id the id of the generic object Entity
     * @return the deleted object
     * @throws RepoException if the object doesn't exist
     */
    @Override
    public T delete(Integer id) throws RepoException {
        T t = super.delete(id);
        save();
        return t;
    }
}

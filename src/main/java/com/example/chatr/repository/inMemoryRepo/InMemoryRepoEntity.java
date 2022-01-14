package com.example.chatr.repository.inMemoryRepo;

import com.example.chatr.domain.Entity;
import com.example.chatr.exceptions.RepoException;

public class InMemoryRepoEntity<T extends Entity<Integer>> extends InMemoryRepo<Integer, T> {
    private int id;

    /**
     * Constructor for InMemoryRepoEntity
     */
    public InMemoryRepoEntity() {
        id = 0;
    }

    /**
     * Adds an object to the repo
     *
     * @param t generic Entity object
     * @throws Exception if the object already exists
     */
    @Override
    public void add(T t) throws Exception {
        t.setId(id);
        for (T el : super.findAll())
            if (el.equals(t))
                throw new RepoException("It already exists!\n");
        id++;
        super.add(t);
    }

    /**
     * Updates an object in the repo
     *
     * @param t generic Entity object
     * @throws Exception if the new object already exists
     */
    @Override
    public void update(T t) throws Exception {
        for (T el : super.findAll())
            if (el.equals(t))
                throw new RepoException("It already exists!\n");
        super.update(t);
    }
}

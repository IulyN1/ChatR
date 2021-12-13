package com.example.chatr.domain;

public abstract class Entity<ID> {
    ID id;

    /**
     * Constructor for Entity
     *
     * @param id generic object ID
     */
    public Entity(ID id) {
        this.id = id;
    }

    /**
     * @return the id
     */
    public ID getId() {
        return id;
    }

    /**
     * Sets a new id
     *
     * @param id generic object ID
     */
    public void setId(ID id) {
        this.id = id;
    }

    /**
     * Modifies the entity
     *
     * @param entity the new entity for the same ID
     */
    public abstract void modify(Entity<ID> entity);
}

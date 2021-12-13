package com.example.chatr.utils;

import java.util.Objects;

public class Pair<T1, T2> {
    T1 t1;
    T2 t2;

    /**
     * Constructor for Pair
     *
     * @param t1 generic object T1
     * @param t2 generic object T2
     */
    public Pair(T1 t1, T2 t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    /**
     * Gets the first element of pair
     *
     * @return the first element
     */
    public T1 getFirst() {
        return t1;
    }

    /**
     * Gets the second element of pair
     *
     * @return the second element
     */
    public T2 getSecond() {
        return t2;
    }

    /**
     * Checks if 2 pairs are equal
     *
     * @param o the other pair Object
     * @return true if they are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(t1, pair.t1) && Objects.equals(t2, pair.t2);
    }

    /**
     * Gets the hash code of the object
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(t1, t2);
    }
}

package com.example.chatr.validators;

public interface StrategyValidator<T> {
    /**
     * Validates the object
     *
     * @param t generic object T
     * @throws Exception if the validation fails
     */
    void validate(T t) throws Exception;
}

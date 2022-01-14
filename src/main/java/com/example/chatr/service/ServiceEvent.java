package com.example.chatr.service;

import com.example.chatr.domain.Event;
import com.example.chatr.exceptions.RepoException;
import com.example.chatr.repository.Repo;
import com.example.chatr.validators.StrategyValidator;

import java.util.Collection;

public class ServiceEvent {
    private final Repo<Integer, Event> eventRepo;
    private final StrategyValidator<Event> eventStrategyValidator;

    public ServiceEvent(Repo<Integer, Event> eventRepo, StrategyValidator<Event> eventStrategyValidator) {
        this.eventRepo = eventRepo;
        this.eventStrategyValidator = eventStrategyValidator;
    }

    public void addEvent(Event event) throws Exception {
        eventStrategyValidator.validate(event);
        eventRepo.add(event);
    }

    public void updateEvent(Event event) throws Exception {
        eventStrategyValidator.validate(event);
        eventRepo.update(event);
    }

    public Event deleteEvent(int id) throws RepoException {
        return eventRepo.delete(id);
    }

    public Event findEventById(int id) throws RepoException {
        return eventRepo.findById(id);
    }

    public Collection<Event> getAllEvent() {
        return eventRepo.findAll();
    }

}

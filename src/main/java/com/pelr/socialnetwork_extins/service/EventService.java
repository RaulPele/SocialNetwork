package com.pelr.socialnetwork_extins.service;

import com.pelr.socialnetwork_extins.domain.Event;
import com.pelr.socialnetwork_extins.domain.User;
import com.pelr.socialnetwork_extins.repository.database.EventsDBRepository;
import com.pelr.socialnetwork_extins.utils.Observable;
import com.pelr.socialnetwork_extins.utils.Observer;

import java.time.LocalDateTime;

public class EventService{
    private EventsDBRepository eventsRepository;

    public EventService(EventsDBRepository eventsRepository) {
        this.eventsRepository = eventsRepository;
    }

    public Event save(User creator, String title, String description, LocalDateTime date, String location) {
        Event event = new Event(creator, title, description, location, date);

        return eventsRepository.save(event);
    }

    public Event findOne(Long eventId) {
        return null;
    }

    public Iterable<Event> findAll() {
        return eventsRepository.findAll();
    }

    public void attendToEvent(User user, Event event) {
        event.addParticipant(user);
        eventsRepository.attendToEvent(user.getID(), event.getID());
    }

    public void cancelAttendingToEvent(User user, Event event) {
        event.removeParticipant(user);
        eventsRepository.cancelAttendingToEvent(user.getID(), event.getID());
    }
}

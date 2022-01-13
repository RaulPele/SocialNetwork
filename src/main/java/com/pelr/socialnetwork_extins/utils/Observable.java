package com.pelr.socialnetwork_extins.utils;

import com.pelr.socialnetwork_extins.controllers.EventCardController;
import com.pelr.socialnetwork_extins.controllers.EventPageController;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable {
    List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void notifyObservers(){
        observers.forEach(Observer::update);
    }

    public void notifyEventControllerObservers() {
        observers.forEach(observer -> {
            if(observer instanceof EventCardController || observer instanceof EventPageController)  {
                observer.update();
            }
        });
    }
}

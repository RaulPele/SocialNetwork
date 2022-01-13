package com.pelr.socialnetwork_extins.utils;

import com.pelr.socialnetwork_extins.controllers.EventCardController;
import com.pelr.socialnetwork_extins.controllers.EventPageController;
import com.pelr.socialnetwork_extins.controllers.HomePageController;

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

    public void notifyHomePageController() {
        //observers.removeIf(observer -> observer instanceof EventCardController);
        observers.forEach(observer -> {
            if(observer instanceof HomePageController)  {
                //System.out.println("INSTANCE OF HOMEPAGECONTROLLER\n");
                observer.update();
            }
        });
    }

    public void removeAllEventCardObservers() {
        observers.removeIf(observer -> observer instanceof EventCardController);
    }
}

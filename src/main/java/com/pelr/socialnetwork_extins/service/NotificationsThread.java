package com.pelr.socialnetwork_extins.service;

import com.pelr.socialnetwork_extins.controllers.HomePageController;
import javafx.application.Platform;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationsThread extends Thread {
    private Controller controller;
    private HomePageController homePageController;

    public NotificationsThread(Controller controller) {
        this.controller = controller;
    }

    public void setHomePageController(HomePageController homePageController) {
        this.homePageController = homePageController;
    }

    private void checkNotifications() {
        controller.getAttendingEvents().forEach(event ->{
            long timeDifferenceInSeconds = ChronoUnit.SECONDS.between(LocalDateTime.now(), event.getDate());
            long timeDifferenceInMinutes = timeDifferenceInSeconds / 60;
            long secondsRemaining = timeDifferenceInSeconds % 60;
            long timeDifferenceInHours = timeDifferenceInMinutes / 60;
            long minutesRemaining = timeDifferenceInMinutes % 60;
            long timeDifferenceInDays = timeDifferenceInHours /24;
            long hoursRemaining = timeDifferenceInHours % 24;

            if(timeDifferenceInSeconds > 0) {
                if (timeDifferenceInMinutes == 1 && secondsRemaining == 0) {
                    Platform.runLater(() -> {
                        homePageController.receiveNotification("1 minute until the " + event.getTitle() +" event!\n");
                    });
                    System.out.println("1 minute until the " + event.getTitle() +" event!\n");
                } else if(timeDifferenceInMinutes == 2 && secondsRemaining == 0) {
                    Platform.runLater(() -> {
                        homePageController.receiveNotification("2 minutes until the " + event.getTitle() +" event!\n");
                    });
                    System.out.println("2 minutes until the " + event.getTitle() +" event!\n");
                } else if(timeDifferenceInMinutes == 5 && secondsRemaining == 0) {
                    Platform.runLater(() -> {
                        homePageController.receiveNotification("5 minutes until the " + event.getTitle() +" event!\n");
                    });
                    System.out.println("5 minutes until the " + event.getTitle() +" event!\n");
                } else if(timeDifferenceInMinutes == 10 && secondsRemaining == 0) {
                    Platform.runLater(() -> {
                        homePageController.receiveNotification("10 minutes until the " + event.getTitle() +" event!\n");
                    });
                    System.out.println("10 minutes until the " + event.getTitle() +" event!\n");
                } else if(timeDifferenceInMinutes == 30 && secondsRemaining == 0) {
                    Platform.runLater(() -> {
                        homePageController.receiveNotification("30 minutes until the " + event.getTitle() +" event!\n");
                    });
                    System.out.println("30 minutes until the " + event.getTitle() +" event!\n");
                } else if(timeDifferenceInHours == 1 && minutesRemaining == 0 && secondsRemaining == 0) {
                    Platform.runLater(() -> {
                        homePageController.receiveNotification("1 hour until the " + event.getTitle() +" event!\n");
                    });
                    System.out.println("1 hour until the " + event.getTitle() +" event!\n");
                } else if(timeDifferenceInHours == 6 && minutesRemaining == 0 && secondsRemaining == 0){
                    Platform.runLater(() -> {
                        homePageController.receiveNotification("6 hours until the " + event.getTitle() +" event!\n");
                    });
                    System.out.println("6 hours until the " + event.getTitle() +" event!\n");
                } else if(timeDifferenceInDays == 1 && hoursRemaining == 0 && minutesRemaining == 0 && secondsRemaining == 0) {
                    Platform.runLater(() -> {
                        homePageController.receiveNotification("1 day until the " + event.getTitle() +" event!\n");
                    });
                    System.out.println("1 day until the " + event.getTitle() +" event!\n");
                }else if(timeDifferenceInDays == 2 && hoursRemaining == 0 && minutesRemaining == 0 && secondsRemaining == 0) {
                    Platform.runLater(() -> {
                        homePageController.receiveNotification("2 days until the " + event.getTitle() +" event!\n");
                    });
                    System.out.println("2 days until the " + event.getTitle() +" event!\n");
                } else if(timeDifferenceInDays == 3 && hoursRemaining == 0 && minutesRemaining == 0 && secondsRemaining == 0) {
                    Platform.runLater(() -> {
                        homePageController.receiveNotification("3 days until the " + event.getTitle() +" event!\n");
                    });
                    System.out.println("3 days until the " + event.getTitle() +" event!\n");
                } else if(timeDifferenceInDays == 4 && hoursRemaining == 0 && minutesRemaining == 0 && secondsRemaining == 0) {
                    Platform.runLater(() -> {
                        homePageController.receiveNotification("4 days until the " + event.getTitle() +" event!\n");
                    });
                    System.out.println("4 days until the " + event.getTitle() +" event!\n");
                } else if(timeDifferenceInDays == 5 && hoursRemaining == 0 && minutesRemaining == 0 && secondsRemaining == 0) {
                    Platform.runLater(() -> {
                        homePageController.receiveNotification("5 days until the " + event.getTitle() +" event!\n");
                    });
                    System.out.println("5 days until the " + event.getTitle() +" event!\n");
                }
            }else if(timeDifferenceInSeconds == 0) {
                Platform.runLater(() -> {
                    homePageController.receiveNotification(event.getTitle() + " is happening now!");
                });
                System.out.println(event.getTitle() + " is happening now!");
            }
        });
    }

    @Override
    public void run() {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (controller.getAuthentication()) {
                    if(controller.userIsLoggedIn()) {
                        checkNotifications();
                    }else {
                        timer.cancel();

                    }
                }
            }
        }, 0, 1000);
    }
}

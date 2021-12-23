package com.pelr.socialnetwork_extins.domain;

import java.time.LocalDateTime;

/**
 * Friendship entity class
 */

public class Friendship extends Entity<Tuple<Long, Long>> {
    private LocalDateTime date;
    private Status status;



    /**
     * Creates a friendship entity.
     */

    public Friendship()
    {
        date = LocalDateTime.now();
    }

    /**
     * Returns the date the friendship was made
     * @return date - LocalDateTime object
     */

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date)
    {
        this.date = date;
    }

    public Status getStatus()
    {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

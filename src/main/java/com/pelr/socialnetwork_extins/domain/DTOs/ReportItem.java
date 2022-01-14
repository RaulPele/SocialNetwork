package com.pelr.socialnetwork_extins.domain.DTOs;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportItem implements Comparable<ReportItem>{
    LocalDateTime date;
    String content;

    public ReportItem(LocalDateTime date, String content) {
        this.date = date;
        this.content = content;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int compareTo(ReportItem o) {
        if(date.isBefore(o.getDate())) {
            return -1;
        }
        if(date.isAfter(o.getDate())) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return date.format(DateTimeFormatter.ofPattern("yyyy:MM:dd hh:mm:ss")) + ": " + content;
    }
}

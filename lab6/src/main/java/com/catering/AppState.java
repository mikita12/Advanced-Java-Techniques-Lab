package com.catering;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AppState {

    private LocalDate currentDate = LocalDate.now();

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(LocalDate currentDate) {
        this.currentDate = currentDate;
    }
}

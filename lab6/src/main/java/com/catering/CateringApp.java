package com.catering;

import com.catering.menu.ConsoleMenu;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class CateringApp {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(CateringApp.class, args);
        ConsoleMenu menu = ctx.getBean(ConsoleMenu.class);
        menu.run();
        ctx.close();
    }
}

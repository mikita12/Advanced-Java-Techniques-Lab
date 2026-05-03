package com.catering.menu;

import com.catering.AppState;
import com.catering.entity.*;
import com.catering.service.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class ConsoleMenu {

    private final ClientService clientService;
    private final OfferService offerService;
    private final OrderService orderService;
    private final ActivityService activityService;
    private final AppState appState;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleMenu(ClientService clientService, OfferService offerService,
                       OrderService orderService, ActivityService activityService, AppState appState) {
        this.clientService = clientService;
        this.offerService = offerService;
        this.orderService = orderService;
        this.activityService = activityService;
        this.appState = appState;
    }

    public void run() {
        System.out.println("=== Catering Service ===");
        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> manageClients();
                case "2" -> manageOffers();
                case "3" -> createOrder();
                case "4" -> registerActivity();
                case "5" -> checkPayment();
                case "6" -> showReports();
                case "7" -> changeDate();
                case "8" -> logReminders();
                case "0" -> running = false;
                default -> System.out.println("Unknown option.");
            }
        }
        System.out.println("Goodbye!");
    }

    private void printMainMenu() {
        System.out.println("\n--- MENU [current date: " + appState.getCurrentDate() + "] ---");
        System.out.println("1. Manage clients");
        System.out.println("2. Manage offers");
        System.out.println("3. Create order");
        System.out.println("4. Register payment / delivery");
        System.out.println("5. Check if order is paid");
        System.out.println("6. Reports");
        System.out.println("7. Change current date");
        System.out.println("8. Log payment reminders to file");
        System.out.println("0. Exit");
        System.out.print("Choice: ");
    }

    // ---- Clients ----

    private void manageClients() {
        System.out.println("\n1. List clients  2. Add client  3. Edit client");
        System.out.print("Choice: ");
        switch (scanner.nextLine().trim()) {
            case "1" -> listClients();
            case "2" -> addClient();
            case "3" -> editClient();
        }
    }

    private void listClients() {
        List<Client> clients = clientService.findAll();
        if (clients.isEmpty()) System.out.println("No clients.");
        else clients.forEach(System.out::println);
    }

    private void addClient() {
        System.out.print("First name: ");
        String first = scanner.nextLine().trim();
        System.out.print("Last name: ");
        String last = scanner.nextLine().trim();
        System.out.print("Client number: ");
        String num = scanner.nextLine().trim();
        clientService.save(new Client(first, last, num));
        System.out.println("Client added.");
    }

    private void editClient() {
        listClients();
        System.out.print("Client ID: ");
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            Optional<Client> opt = clientService.findById(id);
            if (opt.isEmpty()) { System.out.println("Not found."); return; }
            Client client = opt.get();
            System.out.print("New first name [" + client.getFirstName() + "]: ");
            String first = scanner.nextLine().trim();
            System.out.print("New last name [" + client.getLastName() + "]: ");
            String last = scanner.nextLine().trim();
            if (!first.isEmpty()) client.setFirstName(first);
            if (!last.isEmpty()) client.setLastName(last);
            clientService.save(client);
            System.out.println("Client updated.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID.");
        }
    }

    // ---- Offers ----

    private void manageOffers() {
        System.out.println("\n1. List offers  2. Add offer");
        System.out.print("Choice: ");
        switch (scanner.nextLine().trim()) {
            case "1" -> listOffers();
            case "2" -> addOffer();
        }
    }

    private void listOffers() {
        List<Offer> offers = offerService.findAll();
        if (offers.isEmpty()) System.out.println("No offers.");
        else offers.forEach(System.out::println);
    }

    private void addOffer() {
        try {
            System.out.print("Type (BREAKFAST/LUNCH/DINNER): ");
            OfferType type = OfferType.valueOf(scanner.nextLine().trim().toUpperCase());
            System.out.print("Name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Price: ");
            BigDecimal price = new BigDecimal(scanner.nextLine().trim());
            offerService.save(new Offer(type, name, price));
            System.out.println("Offer added.");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid type or price.");
        }
    }

    // ---- Orders ----

    private void createOrder() {
        System.out.println("\n--- CREATE ORDER ---");
        listClients();
        System.out.print("Client ID: ");
        try {
            Long clientId = Long.parseLong(scanner.nextLine().trim());
            Optional<Client> clientOpt = clientService.findById(clientId);
            if (clientOpt.isEmpty()) { System.out.println("Client not found."); return; }

            System.out.println("\nAvailable offers:");
            listOffers();
            System.out.print("Offer IDs (comma-separated, e.g. 1,3): ");
            String[] parts = scanner.nextLine().trim().split(",");
            List<Offer> offers = new ArrayList<>();
            for (String part : parts) {
                offerService.findById(Long.parseLong(part.trim())).ifPresent(offers::add);
            }
            if (offers.isEmpty()) { System.out.println("No valid offers selected."); return; }

            BigDecimal total = offers.stream().map(Offer::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            System.out.println("Total: " + total + " PLN");

            System.out.print("Delivery address: ");
            String address = scanner.nextLine().trim();
            System.out.print("Delivery date (YYYY-MM-DD): ");
            LocalDate deliveryDate = LocalDate.parse(scanner.nextLine().trim());

            Order order = orderService.createOrder(clientOpt.get(), offers, address, deliveryDate);
            System.out.println("Order created: " + order);
        } catch (NumberFormatException | DateTimeParseException e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }

    // ---- Activities ----

    private void registerActivity() {
        System.out.println("\n--- REGISTER PAYMENT / DELIVERY ---");
        orderService.findAll().forEach(System.out::println);
        System.out.print("Order ID: ");
        try {
            Long orderId = Long.parseLong(scanner.nextLine().trim());
            Optional<Order> orderOpt = orderService.findById(orderId);
            if (orderOpt.isEmpty()) { System.out.println("Order not found."); return; }
            Order order = orderOpt.get();

            System.out.println("1. Payment  2. Delivery");
            System.out.print("Choice: ");
            String choice = scanner.nextLine().trim();
            if ("1".equals(choice)) {
                System.out.print("Amount: ");
                BigDecimal amount = new BigDecimal(scanner.nextLine().trim());
                Activity a = activityService.registerPayment(order, appState.getCurrentDate(), amount);
                System.out.println("Payment registered: " + a);
                System.out.println("Order status: " + orderService.findById(orderId).map(o -> o.getStatus().name()).orElse("?"));
            } else if ("2".equals(choice)) {
                Activity a = activityService.registerDelivery(order, appState.getCurrentDate());
                System.out.println("Delivery registered: " + a);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    private void checkPayment() {
        System.out.print("Order ID: ");
        try {
            Long orderId = Long.parseLong(scanner.nextLine().trim());
            boolean paid = orderService.isPaid(orderId);
            System.out.println("Order #" + orderId + " is " + (paid ? "PAID" : "NOT PAID"));
            List<Activity> activities = activityService.findByOrder(orderId);
            if (activities.isEmpty()) System.out.println("No activities recorded.");
            else activities.forEach(System.out::println);
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID.");
        }
    }

    // ---- Reports ----

    private void showReports() {
        System.out.println("\n1. All orders  2. Unpaid orders");
        System.out.print("Choice: ");
        String choice = scanner.nextLine().trim();
        if ("1".equals(choice)) {
            List<Order> orders = orderService.findAll();
            if (orders.isEmpty()) System.out.println("No orders.");
            else orders.forEach(System.out::println);
        } else if ("2".equals(choice)) {
            List<Order> unpaid = orderService.findUnpaid();
            if (unpaid.isEmpty()) System.out.println("No unpaid orders.");
            else unpaid.forEach(System.out::println);
        }
    }

    // ---- Date & Reminders ----

    private void changeDate() {
        System.out.println("Current date: " + appState.getCurrentDate());
        System.out.print("New date (YYYY-MM-DD): ");
        try {
            LocalDate newDate = LocalDate.parse(scanner.nextLine().trim());
            appState.setCurrentDate(newDate);
            System.out.println("Date changed to: " + newDate);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Use YYYY-MM-DD.");
        }
    }

    private void logReminders() {
        List<Order> unpaid = orderService.findUnpaid();
        activityService.logPaymentReminders(unpaid, appState.getCurrentDate());
        System.out.println("Reminders written to catering-reminders.log (" + unpaid.size() + " unpaid orders).");
    }
}

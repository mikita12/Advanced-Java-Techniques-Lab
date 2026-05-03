package com.catering.service;

import com.catering.entity.*;
import com.catering.repository.ActivityRepository;
import com.catering.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ActivityService {

    private static final Logger log = LoggerFactory.getLogger(ActivityService.class);

    private final ActivityRepository activityRepository;
    private final OrderRepository orderRepository;

    public ActivityService(ActivityRepository activityRepository, OrderRepository orderRepository) {
        this.activityRepository = activityRepository;
        this.orderRepository = orderRepository;
    }

    public Activity registerPayment(Order order, LocalDate date, BigDecimal amount) {
        Activity activity = new Activity();
        activity.setType(ActivityType.PAYMENT);
        activity.setOrder(order);
        activity.setDate(date);
        activity.setAmount(amount);
        activityRepository.save(activity);

        BigDecimal totalPaid = activityRepository.findByOrderIdAndType(order.getId(), ActivityType.PAYMENT)
                .stream().map(Activity::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalPaid.compareTo(order.getTotalAmount()) >= 0) {
            order.setStatus(OrderStatus.PAID);
            orderRepository.save(order);
            log.info("Order #{} marked as PAID. Total paid: {} PLN", order.getId(), totalPaid);
        }

        return activity;
    }

    public Activity registerDelivery(Order order, LocalDate date) {
        Activity activity = new Activity();
        activity.setType(ActivityType.DELIVERY);
        activity.setOrder(order);
        activity.setDate(date);
        activity.setAmount(BigDecimal.ZERO);
        activityRepository.save(activity);

        order.setStatus(OrderStatus.DELIVERED);
        orderRepository.save(order);
        log.info("Order #{} marked as DELIVERED on {}", order.getId(), date);

        return activity;
    }

    public List<Activity> findByOrder(Long orderId) {
        return activityRepository.findByOrderId(orderId);
    }

    public void logPaymentReminders(List<Order> unpaidOrders, LocalDate currentDate) {
        if (unpaidOrders.isEmpty()) {
            log.info("Payment reminder check on {} - no unpaid orders found.", currentDate);
            return;
        }
        for (Order order : unpaidOrders) {
            if (!order.getDeliveryDate().isAfter(currentDate)) {
                log.warn("PAYMENT REMINDER [{}]: Order #{} - client: {} {} - address: {} - due: {} - amount: {} PLN - UNPAID",
                        currentDate,
                        order.getId(),
                        order.getClient().getFirstName(),
                        order.getClient().getLastName(),
                        order.getAddress(),
                        order.getDeliveryDate(),
                        order.getTotalAmount());
            } else {
                log.info("Reminder check [{}]: Order #{} - delivery date {} not yet reached, skipping.",
                        currentDate, order.getId(), order.getDeliveryDate());
            }
        }
    }
}

package com.catering.repository;

import com.catering.entity.Activity;
import com.catering.entity.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByOrderId(Long orderId);
    List<Activity> findByOrderIdAndType(Long orderId, ActivityType type);
}

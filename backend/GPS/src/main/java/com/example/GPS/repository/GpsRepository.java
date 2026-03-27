package com.example.GPS.repository;

import com.example.GPS.entity.GpsData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GpsRepository extends JpaRepository<GpsData, Long> {
}

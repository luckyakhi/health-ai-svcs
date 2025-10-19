package com.example.tasksapi.persistence.repository;

import com.example.tasksapi.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {
}


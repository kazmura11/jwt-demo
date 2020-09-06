package com.example.jwt_demo.common.spring.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.jwt_demo.common.spring.models.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	public Optional<User> findByEmailAddressEqualsAndDeletedIsFalse(String emailAddress);

	public Optional<User> findByEmailAddressEquals(String emailAddress);

	public Optional<User> findByUserId(@Param("userId") Long userId);

}

package com.example.jwt_demo.common.spring.services;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.jwt_demo.common.spring.models.entities.User;
import com.example.jwt_demo.common.spring.repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	public Optional<User> getUser(String emailAddress) {
		return userRepository.findByEmailAddressEqualsAndDeletedIsFalse(emailAddress);
	}

	/**
	 * マスターから登録用。メールアドレスではなく、Idで検索する
	 */
	public Optional<User> getInculudeDeleted(Long userId){
		return userRepository.findByUserId(userId);
	}

	/**
	 * 削除済みのも含めてすべてのユーザーを取得する
	 */
	public List<User> findAll() {
		return userRepository.findAll();
	}

	/**
	 * ユーザーを新規作成する
	 */
	public Optional<User> create(String name, String password, String emailAddress,
			boolean adminFlag) {
		Optional<User> searchUser = userRepository.findByEmailAddressEquals(emailAddress);
		if (searchUser.isPresent()) {
			log.info("user already exists");
			return Optional.empty();
		}
		Date now = new Date();
		User user = new User();
		user.setName(name);
		user.setPassword(new BCryptPasswordEncoder().encode(password));
		user.setEmailAddress(emailAddress);
		user.setAdminFlag(adminFlag);
		user.setCreatedAt(now);
		user.setUpdatedAt(new Timestamp(now.getTime()));
		User saveUser = null;
		try {
			saveUser = userRepository.save(user);
		} catch(Exception e) {
			log.error(e.getMessage());
			return Optional.empty();
		}
		log.info("save succeeded!");
		return Optional.of(saveUser);
	}

	public Optional<User> update(User updateUser) {
		Optional<User> result = getInculudeDeleted(updateUser.getUserId());
		if (!result.isPresent()) {
			return Optional.empty();
		}
		Date now = new Date();
		User user = result.get();
		if (updateUser.getEmailAddress() != null) {
			user.setEmailAddress(updateUser.getEmailAddress());
		}
		if (updateUser.getName() != null) {
			user.setName(updateUser.getName());
		}
		if (updateUser.getPassword() != null && updateUser.getPassword().length() != 0) {
			user.setPassword(new BCryptPasswordEncoder().encode(updateUser.getPassword()));
		}
		user.setAdminFlag(updateUser.isAdminFlag());
		user.setUpdatedAt(new Timestamp(now.getTime()));
		userRepository.save(user);

		return Optional.of(user);
	}

	public Optional<User> delete(Long userId) {
		Optional<User> result = userRepository.findByUserId(userId);
		if (!result.isPresent()) {
			log.error("not found!");
			return Optional.empty();
		}
		Date now = new Date();
		User user = result.get(); 
		user.setDeleted(true);
		user.setUpdatedAt(new Timestamp(now.getTime()));
		try {
			userRepository.save(user);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Optional.empty();
		}
		return Optional.of(user);
	}

	public Optional<User> resotre(Long userId) {
		Optional<User> result = userRepository.findByUserId(userId);
		if (!result.isPresent()) {
			log.error("not found!");
			return Optional.empty();
		}
		Date now = new Date();
		User user = result.get();
		user.setDeleted(false);
		user.setUpdatedAt(new Timestamp(now.getTime()));
		try {
			userRepository.save(user);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return Optional.empty();
		}
		return Optional.of(user);
	}

}

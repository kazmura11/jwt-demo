package com.example.jwt_demo.common.spring.models.entities;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.Version;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "email_address", nullable = false)
	private String emailAddress;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "admin_flag", nullable = false)
	private boolean adminFlag;

	@Column(name = "deleted", nullable = false)
	private boolean deleted;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", nullable = false)
	private Date createdAt;

	@Version
	@Column(name = "updated_at", nullable = false)
	private Timestamp updatedAt;

}

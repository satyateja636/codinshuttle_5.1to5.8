package com.codingshuttle.SecurityApp.SecurityApplication.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codingshuttle.SecurityApp.SecurityApplication.entities.Session;
import com.codingshuttle.SecurityApp.SecurityApplication.entities.User;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long>{

	List<Session> findByUser(User user);

	Optional<Session> findByRefreshToken(String refreshToken);

}

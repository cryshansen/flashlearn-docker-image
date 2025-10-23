package com.artog.flashlearn.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.artog.flashlearn.model.User;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long userid);

    boolean existsByEmail(String email);
	void save(Class<User> class1);

}

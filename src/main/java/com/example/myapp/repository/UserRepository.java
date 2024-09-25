package com.example.myapp.repository;

import com.example.myapp.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByMobileNumber(String mobileNumber);
  List<User> findByManagerId(Long managerId);
}

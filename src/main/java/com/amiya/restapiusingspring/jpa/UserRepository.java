package com.amiya.restapiusingspring.jpa;

import com.amiya.restapiusingspring.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
}

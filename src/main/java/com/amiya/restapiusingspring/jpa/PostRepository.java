package com.amiya.restapiusingspring.jpa;

import com.amiya.restapiusingspring.user.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Integer> {
}

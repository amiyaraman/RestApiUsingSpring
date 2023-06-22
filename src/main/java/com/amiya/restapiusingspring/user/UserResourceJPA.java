package com.amiya.restapiusingspring.user;

import com.amiya.restapiusingspring.exception.UserNotFoundException;
import com.amiya.restapiusingspring.jpa.PostRepository;
import com.amiya.restapiusingspring.jpa.UserRepository;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserResourceJPA {

    private UserRepository repository;
    private PostRepository postRepository;

    public UserResourceJPA(UserRepository repository,PostRepository postRepository) {
        this.repository=repository;
        this.postRepository=postRepository;
    }

    @GetMapping("/jpa/users")
    public List<User> retrieveAllUser(){
        return repository.findAll();
    }
    @GetMapping("/jpa/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable int id){
        Optional<User> user=repository.findById(id);
        if(user.isEmpty())
            throw  new UserNotFoundException("id:"+id);
        EntityModel<User> entityModel = EntityModel.of(user.get());
        WebMvcLinkBuilder link =linkTo(methodOn(this.getClass()).retrieveAllUser());
        entityModel.add(link.withRel("all-user"));

        return entityModel;
    }
    //Post /User

    @PostMapping("/jpa/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody  User user){
        User savedUser = repository.save(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();

    }
    @DeleteMapping("/jpa/users/{id}")
    public void deleteUser(@PathVariable int id){
      repository.deleteById(id);
    }

    @GetMapping("jpa/users/{id}/posts")
    public List<Post> retrievePostsForUser(@PathVariable int id){
        Optional<User> user = repository.findById(id);
        if(user.isEmpty())
            throw new UserNotFoundException("id:"+id);
        return user.get().getPosts();
    }
    @PostMapping("jpa/users/{id}/posts")
    public ResponseEntity<Post> createPostForUser(@PathVariable int id ,@Valid @RequestBody Post post){
        Optional<User> user = repository.findById(id);
        if(user.isEmpty())
            throw new UserNotFoundException("id:"+id);

        post.setUser(user.get());
        Post savedPost = postRepository.save(post);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPost.getId())
                .toUri();

        return ResponseEntity.created(location).build();




    }
    @GetMapping("jpa/users/{id}/posts/{postId}")
    public Post retrievePostForUser(@PathVariable int id, @PathVariable int postId){
        Optional<User> user = repository.findById(id);
        if(user.isEmpty())
            throw new UserNotFoundException("id:" + id);
        Optional<Post> post=postRepository.findById(postId);
        if(post.isEmpty())
            throw new UserNotFoundException("id:"+id);
        return post.get();
    }

}

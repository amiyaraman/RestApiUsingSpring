package com.amiya.restapiusingspring.user;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Component
public class UserDaoService {

    private static List<User> users = new ArrayList<>();
    private  static  int userCount;

    static {
        users.add(new User(1,"Adam", LocalDate.now().minusYears(30)));
        users.add(new User(2,"Eve", LocalDate.now().minusYears(25 )));
        users.add(new User(3,"Jim", LocalDate.now().minusYears(20)));
    }

    public List<User> findAll() {
        return users;
    }
    public  User findOne(int id){
        Predicate<? super User> predicate = user -> user.getId()==id;
        return users.stream().filter(predicate).findFirst().orElse(null);
    }
    public  User save(User user){
        userCount=users.size();
        user.setId(++userCount);
        users.add(user);
        return user;
    }
    public  void deleteById(int id){
        Predicate<? super User> predicate = user -> user.getId()==id;
         users.removeIf(predicate);
    }
}

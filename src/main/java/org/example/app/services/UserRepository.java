package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.example.web.dto.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
public class UserRepository implements ProjectRepository<User> {
  Logger logger =  Logger.getLogger(this.getClass());
  private final List<User> userList = new ArrayList<>();

 @Override
 public List<User> retreiveAll() {
    return userList;
 }

 @Override
 public void store(User user) {
  userList.add(user);
 }

 @Override
 public boolean removeItemById(Integer bookIdToRemove) {
  return false;
 }
}

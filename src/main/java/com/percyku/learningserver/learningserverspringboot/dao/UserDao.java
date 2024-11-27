package com.percyku.learningserver.learningserverspringboot.dao;


import com.percyku.learningserver.learningserverspringboot.dto.UserRegisterRequest;
import com.percyku.learningserver.learningserverspringboot.model.User;

public interface UserDao {

    User getUserById(Long memberId);

    User getUserByEmail(String email);

    Long createUser(User user);

    Long update(User user);
}

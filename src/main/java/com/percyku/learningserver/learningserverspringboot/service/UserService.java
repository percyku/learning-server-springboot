package com.percyku.learningserver.learningserverspringboot.service;

import com.percyku.learningserver.learningserverspringboot.dto.UserRegisterRequest;
import com.percyku.learningserver.learningserverspringboot.util.Member;

public interface UserService {


    Long createUser(UserRegisterRequest member);

    Member getMemberById(Long memberId);

    Member getMemberByEmail(String email);


}

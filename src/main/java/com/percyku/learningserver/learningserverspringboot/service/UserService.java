package com.percyku.learningserver.learningserverspringboot.service;

import com.percyku.learningserver.learningserverspringboot.dto.UserRegisterRequest;
import com.percyku.learningserver.learningserverspringboot.util.Member;

public interface UserService {


    Long createUser(UserRegisterRequest member);

    Long update( String userEmail ,UserRegisterRequest updateMember);

    Member getMemberById(Long memberId);

    Member getMemberByEmail(String email);


}

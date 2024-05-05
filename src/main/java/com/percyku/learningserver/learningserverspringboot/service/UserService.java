package com.percyku.learningserver.learningserverspringboot.service;

import com.percyku.learningserver.learningserverspringboot.util.Member;

public interface UserService {


    Long createUser(Member member);

    Member getMemberById(Long memberId);

    Member getMemberByEmail(String email);


}

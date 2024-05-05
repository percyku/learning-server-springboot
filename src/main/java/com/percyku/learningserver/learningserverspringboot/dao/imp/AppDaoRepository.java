package com.percyku.learningserver.learningserverspringboot.dao.imp;

import com.percyku.learningserver.learningserverspringboot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppDaoRepository extends JpaRepository<User,Long> {


}

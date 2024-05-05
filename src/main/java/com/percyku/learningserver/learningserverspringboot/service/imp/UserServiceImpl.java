package com.percyku.learningserver.learningserverspringboot.service.imp;

import com.percyku.learningserver.learningserverspringboot.dao.UserDao;
import com.percyku.learningserver.learningserverspringboot.util.Member;
import com.percyku.learningserver.learningserverspringboot.model.Role;
import com.percyku.learningserver.learningserverspringboot.model.User;
import com.percyku.learningserver.learningserverspringboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDao userDao;

    @Override
    @Transactional
    public Long createUser(Member member) {
        User user = null;
        List<Role> theRole =new ArrayList<>();
        boolean enabled=false;
        if(member.getRoles()!= null){
            for(String roleName : member.getRoles()) {
                Role tmpRole = new Role();
                long roleId = 0;
                if ("ROLE_STUDENT".equals(roleName)) {
                    roleId = 1;
                } else if ("ROLE_INSTRUCTOR".equals(roleName)) {
                    roleId = 2;
                } else if ("ROLE_ADMIN".equals(roleName)) {
                    roleId = 3;
                }
                tmpRole.setId(roleId);
                tmpRole.setName(roleName);
                theRole.add(tmpRole);
            }
            enabled=true;
        }
        String hashedPassword = passwordEncoder.encode(member.getPassword());
        if(enabled){
            user =new User(member.getUsername(),
                    member.getFirst_name(),
                    member.getLast_name(),
                    member.getEmail(),
                    hashedPassword,
                    enabled,
                    theRole);
        }else {
            user =new User(member.getUsername(),
                    member.getFirst_name(),
                    member.getLast_name(),
                    member.getEmail(),
                    hashedPassword,
                    enabled);
        }
        System.out.println(user.toString());



        return userDao.createUser(user);
    }

    @Override
    public Member getMemberById(Long memberId) {
        User user =userDao.getUserById(memberId);
        Member member =new Member();

        member.setEmail(user.getEmail());
        member.setFirst_name(user.getFirstName());
        member.setLast_name(user.getLastName());
        member.setUsername(user.getUserName());

        List<String>theRole =new ArrayList<>();
        if(user.isEnabled()){
            for(Role tmpRole : user.getRoles()){
                theRole.add(tmpRole.getName());
            }
        }
        member.setRoles(theRole);


        return member;
    }

    @Override
    public Member getMemberByEmail(String email) {
        User user =userDao.getUserByEmail(email);
        Member member =new Member();

        member.setId(user.getId());
        member.setEmail(user.getEmail());
        member.setFirst_name(user.getFirstName());
        member.setLast_name(user.getLastName());
        member.setUsername(user.getUserName());

        List<String>theRole =new ArrayList<>();
        if(user.isEnabled()){
            for(Role tmpRole : user.getRoles()){
                theRole.add(tmpRole.getName());
            }
        }
        member.setRoles(theRole);


        return member;

    }
}

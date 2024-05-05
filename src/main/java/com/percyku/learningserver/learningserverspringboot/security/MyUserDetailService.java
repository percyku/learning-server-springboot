package com.percyku.learningserver.learningserverspringboot.security;

import com.percyku.learningserver.learningserverspringboot.dao.UserDao;
import com.percyku.learningserver.learningserverspringboot.model.Role;
import com.percyku.learningserver.learningserverspringboot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user =userDao.getUserByEmail(username);
        if(user == null){
            throw new UsernameNotFoundException("User not found for:" + username);
        }else{
            String userEmail = user.getEmail();
            String userPassword = user.getPassword();
            Collection<SimpleGrantedAuthority> authorities = mapRolesToAuthorities(user.getRoles());
            return new org.springframework.security.core.userdetails.User(userEmail, userPassword, authorities);
        }
    }

    private Collection<SimpleGrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (Role tempRole : roles) {
            SimpleGrantedAuthority tempAuthority = new SimpleGrantedAuthority(tempRole.getName());
            authorities.add(tempAuthority);
        }

        return authorities;
    }



}

package com.percyku.learningserver.learningserverspringboot.controller;


import com.percyku.learningserver.learningserverspringboot.dto.UserRegisterRequest;
import com.percyku.learningserver.learningserverspringboot.util.Member;
import com.percyku.learningserver.learningserverspringboot.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.regex.Pattern;

@Validated
@RestController
public class UserController {

    @Autowired
    UserService userService;


    @PostMapping("/register")
    public ResponseEntity<Member> register(@RequestBody @Valid UserRegisterRequest member){

        long result = userService.createUser(member);
        if(result == -1){
            throw new CommonException("This user had been exist: "+member.getEmail());
        }
        Member theMember =userService.getMemberById(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(theMember);
    }

    @PostMapping("/updateProfile")
    public ResponseEntity<String> updateProfile(HttpServletRequest request, HttpServletResponse response,
                                                Authentication authentication, @RequestBody @Valid UserRegisterRequest member){

        String userEmail = authentication.getName();

        Long userId =  userService.update(userEmail,member);

        if(userId==-1){
            throw new CommonException("This user had been exist: "+member.getEmail());
        }

        if(userId==-2){
            throw new CommonException("We cannot find this User: "+userEmail);
        }

        CookieClearingLogoutHandler cookies = new CookieClearingLogoutHandler("JSESSIONID","XSRF-TOKEN");
        cookies.logout(request,response,authentication);

        return ResponseEntity.status(HttpStatus.OK).body("update successful");
    }



    @GetMapping("/loginforlearnsys")
    public ResponseEntity<Member> loginforlearnsys(Authentication authentication,
                                                   @RequestParam(value="role", required=true) String role
                                                   ){
        String username = authentication.getName();
        Member theMember = userService.getMemberByEmail(username);
        if(theMember== null){
            throw new CommonException("This user didn't exist: "+username);
        }

        if(theMember.getRoles().contains(role)){
            theMember.setRole(role.split("_")[1]);
        }else{
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(theMember);
    }


    @GetMapping("/logoutSuccess")
    public ResponseEntity<String> logoutSuccess(){

        return  ResponseEntity.status(HttpStatus.OK).body("logout");
    }


}

package com.percyku.learningserver.learningserverspringboot.controller;


import com.percyku.learningserver.learningserverspringboot.util.Member;
import com.percyku.learningserver.learningserverspringboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
public class UserController {

    @Autowired
    UserService userService;


    @PostMapping("/register")
    public ResponseEntity<Member> register(@RequestBody Member member){

        long result = userService.createUser(member);
        Member theMember =userService.getMemberById(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(theMember);
    }

    @GetMapping("/loginforlearnsys")
    public ResponseEntity<Member> loginforlearnsys(Authentication authentication,
                                                   @RequestParam(value="role", required=true) String role
                                                   ){
        String username = authentication.getName();
        Member theMember = userService.getMemberByEmail(username);

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

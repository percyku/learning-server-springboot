package com.percyku.learningserver.learningserverspringboot;

import com.percyku.learningserver.learningserverspringboot.dao.AppDao;
import com.percyku.learningserver.learningserverspringboot.dao.imp.AppDaoImpl;
import com.percyku.learningserver.learningserverspringboot.dao.imp.AppDaoRepository;
import com.percyku.learningserver.learningserverspringboot.model.Course;
import com.percyku.learningserver.learningserverspringboot.model.Review;
import com.percyku.learningserver.learningserverspringboot.model.Role;
import com.percyku.learningserver.learningserverspringboot.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class LearningServerSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearningServerSpringbootApplication.class, args);
	}


}

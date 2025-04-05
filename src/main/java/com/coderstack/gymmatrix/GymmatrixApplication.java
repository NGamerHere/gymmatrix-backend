package com.coderstack.gymmatrix;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GymmatrixApplication {

    public static int getUserID(HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("sessionData");
        return (Integer) claims.get("id");
    }

    public static void main(String[] args) {
        SpringApplication.run(GymmatrixApplication.class, args);
    }

}

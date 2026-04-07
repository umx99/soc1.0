package com.soc.siem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SiemBackendApplication {
    public static void main(String[] args) {
    var context = SpringApplication.run(SiemBackendApplication.class, args);
    
    // This will print AFTER the app starts to prove Spring is working
    System.out.println("\n*****************************************");
    System.out.println("SOC SYSTEM IS RUNNING AND VERIFIED");
    System.out.println("*****************************************\n");
    }
}
package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class Application {

	/*String path = Application.class.getResource("").getPath();
	//C:\Users\whddl\Downloads
	File file = new File(path + "bc0ce796b0f09867.xlsx");*/
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}

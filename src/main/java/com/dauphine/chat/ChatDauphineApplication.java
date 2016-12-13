package com.dauphine.chat;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletRegistration;
import java.text.SimpleDateFormat;
import java.util.Calendar;


@SpringBootApplication
public class ChatDauphineApplication {

	public static void main(String[] args) {
        final Logger LOGGER = LogManager.getLogger(ChatDauphineApplication.class);

		SpringApplication.run(ChatDauphineApplication.class, args);

        LOGGER.info(String.format("Lauching %s at %s", ChatDauphineApplication.class.getSimpleName(), new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime())));

    }


    @Bean public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet){
          ServletRegistrationBean registrationBean = new ServletRegistrationBean(dispatcherServlet);
          registrationBean.addUrlMappings("/");
          return registrationBean;
    }


}

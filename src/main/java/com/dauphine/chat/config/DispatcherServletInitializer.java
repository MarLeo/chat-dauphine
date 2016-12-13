package com.dauphine.chat.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;


/**
 * Created by marti on 10/12/2016.
 */
public class DispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] { WebSecurityConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] { WebConfig.class, WebSocketConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/","/home" };
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration){
        registration.setInitParameter("dispatcherOptionsRequest", "true");
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException{
        super.onStartup(servletContext);
    }

}

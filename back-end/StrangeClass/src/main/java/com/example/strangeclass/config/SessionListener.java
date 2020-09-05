package com.example.strangeclass.config;

import com.example.strangeclass.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Slf4j
@WebListener
public class SessionListener implements HttpSessionListener {

    @Autowired
    UserService userService;

    @Override
    public void sessionCreated(HttpSessionEvent se) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        try {
            userService.logout(se.getSession());
        } catch (Exception ex) {
//            ex.printStackTrace();
            log.info("自动销毁Session, 用户已经主动登出");
        }
    }
}

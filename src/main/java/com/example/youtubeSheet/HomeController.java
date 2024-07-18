package com.example.youtubeSheet;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Controller
public class HomeController {

    @GetMapping("/")
    public String home(){

        return "redirect:/post/list";    }


    @Value("${server.port}")
    private String port;

    @Value("${server.serverAddress}")
    private String serverAddress;

    @Value("${serverName}")
    private String serverName;

    @GetMapping("/serverCheck")
    public ResponseEntity<?> serverCheck(){
        Map<String,String> responseData=new TreeMap<>();

        responseData.put("Server address",serverAddress);
        responseData.put("Server name", serverName);
        responseData.put("Port number",port);

        return ResponseEntity.ok(responseData);
    }

}

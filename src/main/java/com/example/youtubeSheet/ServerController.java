package com.example.youtubeSheet;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Controller
public class ServerController {

    @Value("${server.port}")
    private String port;

    @Value("${server.serverAddress}")
    private String serverAddress;

    @Value("${serverName}")
    private String serverName;

    @GetMapping("/")
    public String home(){

        return "redirect:/post/list";    }


    @GetMapping("/serverCheck")
    public ResponseEntity<?> serverCheck(){
        Map<String,String> responseData=new TreeMap<>();

        responseData.put("Server address",serverAddress);
        responseData.put("Server name", serverName);
        responseData.put("Port number",port);

        return ResponseEntity.ok(responseData);
    }

}

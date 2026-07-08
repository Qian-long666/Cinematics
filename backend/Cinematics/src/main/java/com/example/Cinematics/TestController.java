package com.example.Cinematics;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/hello")     //浏览器访问 /hello 发送请求，服务端返回“Hello World”
    public String hello(){
        return "Hello World";
    }

}

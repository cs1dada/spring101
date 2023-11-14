package org.dandan;

import org.dandan.utils.SpringContextHolder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Hello world!
 *
 */
@RestController
@SpringBootApplication
public class spring101Application
{
    public static void main( String[] args ) throws Exception {

        SpringApplication.run(spring101Application.class, args);


    }

    @Bean
    public SpringContextHolder springContextHolder(){
            return new SpringContextHolder();
    }

    /**
     * 访问首页提示
     *
     * @return /
     */
    @GetMapping("/")
    public String index() {
        return "Backend service started successfully";
    }
}

package br.com.erudio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import br.com.erudio.config.FileStorageConfig;

@SpringBootApplication
@EnableConfigurationProperties({
    FileStorageConfig.class
})
public class Startup {

    public static void main(String[] args) {
        SpringApplication.run(Startup.class, args);
        
        /*Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
        passwordEncoder.setDefaultPasswordEncoderForMatches(new Pbkdf2PasswordEncoder());
        
        
        //DelegatingPasswordEncoder passwordEncoder = (DelegatingPasswordEncoder) PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String result = passwordEncoder.encode("admin234");
        System.out.println("My hash " + result);*/
    }
}

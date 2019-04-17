package br.com.erudio.controllers;

import static org.springframework.http.ResponseEntity.ok;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.erudio.data.vo.security.AuthenticationRequestVO;
import br.com.erudio.repository.UserRepository;
import br.com.erudio.security.jwt.JwtTokenProvider;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider tokenProvider;
    
    @Autowired
    UserRepository repository;

    @RequestMapping(method = RequestMethod.POST, value = "/signin", consumes = { "application/json", "application/xml", "application/x-yaml" },
    	    produces = { "application/json", "application/xml", "application/x-yaml" })
    public ResponseEntity signin(@RequestBody AuthenticationRequestVO data) {

        try {
            String username = data.getUsername();
            
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, data.getPassword());

            
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            //String token = jwtTokenProvider.createToken(username, this.repository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException("Username " + username + "not found")).getRoles());
            var user = this.repository.findByUsername(username);
            var token = "";
            
            //TODO
            if (user != null) {
                token = tokenProvider.createToken(username, Arrays.asList( "ADMIN"));
            } else {
                throw new UsernameNotFoundException("Username " + username + "not found");
            }

            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("token", token);
            return ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }
}

package br.com.erudio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.erudio.data.vo.v1.security.AccountCredentialsVO;
import br.com.erudio.data.vo.v1.security.TokenVO;
import br.com.erudio.services.AuthServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Authentication Endpoint") 
@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    AuthServices authServices;
    
    @Operation(summary = "Authenticates a user and returns a token")
    @SuppressWarnings("rawtypes")
    @PostMapping(value = "/signin" )
    public ResponseEntity signin(@RequestBody AccountCredentialsVO data) {
        return authServices.signin(data);
    }
    
    @Operation(summary = "Refresh token for authenticated user and returns a token")
    @SuppressWarnings("rawtypes")
    @PutMapping(value = "/refresh" )
    public ResponseEntity refreshToken(@RequestBody TokenVO tokenVO) {
        
        if (tokenVO == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Ivalid client request");
        var token = authServices.refreshToken(tokenVO);
        if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Ivalid client request");
        return ResponseEntity.ok(token);
    }
}

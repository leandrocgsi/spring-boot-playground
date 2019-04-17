package br.com.erudio.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.erudio.data.vo.security.UserVO;
import br.com.erudio.services.UserService;

@RestController
@RequestMapping(value = "/api/user/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping()
    public ResponseEntity<UserVO> getAuthenticatedUser() {
    	UserVO user = service.getAuthenticatedUser();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}

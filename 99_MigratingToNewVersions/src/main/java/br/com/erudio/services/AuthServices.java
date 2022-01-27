package br.com.erudio.services;

import static org.springframework.http.ResponseEntity.ok;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.erudio.repository.UserRepository;
import br.com.erudio.security.AccountCredentialsVO;
import br.com.erudio.security.LoginResponseVO;
import br.com.erudio.security.jwt.JwtTokenProvider;

@Service
public class AuthServices {
	
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtTokenProvider tokenProvider;
	
	@Autowired
	UserRepository repository;
	
	@SuppressWarnings("rawtypes")
	public ResponseEntity signin(AccountCredentialsVO data) {
		try {
			var username = data.getUsername();
			var pasword = data.getPassword();
			
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, pasword));
			
			var user = repository.findByUsername(username);
			
			var loginResponse = new LoginResponseVO();
			
			if (user != null) {
				loginResponse.setToken(tokenProvider.createToken(username, user.getRoles()));
				loginResponse.setUsername(username);
			} else {
				throw new UsernameNotFoundException("Username " + username + " not found!");
			}
			return ok(loginResponse);
			
		} catch (AuthenticationException e) {
			throw new BadCredentialsException("Invalid username/password supplied!");
		}
	}
}

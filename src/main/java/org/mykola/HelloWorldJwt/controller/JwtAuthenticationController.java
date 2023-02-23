package org.mykola.HelloWorldJwt.controller;

import org.mykola.HelloWorldJwt.config.JwtTokenUtil;
import org.mykola.HelloWorldJwt.model.JwtRequest;
import org.mykola.HelloWorldJwt.model.JwtResponse;
import org.mykola.HelloWorldJwt.service.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@RestController
@CrossOrigin
public class JwtAuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JwtUserDetailsService userDetailsService;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest, HttpServletRequest request) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);
//


        printHttpRequestHeader(request);

//
        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void printHttpRequestHeader(HttpServletRequest request) {
        String remoteAddress=request.getRemoteAddr();
        String forwarded = request.getHeader("X-FORWARDED-FOR");
        System.out.println("\n======== Request Headers =========");
        System.out.println("===========\nRemoteAddress is " + remoteAddress+"\n===========");
        System.out.println("===========\nForwardedAddress is " + forwarded+"\n===========");
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            System.out.println("Header "+key+" "+value);
        }
        System.out.println("========== Request Headers END =========\n");
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }


}

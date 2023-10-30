package security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import security.demo.Entities.User;
import security.demo.dto.AuthRequest;
import security.demo.service.JwtService;
import security.demo.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/public")
    public String publicPage() {
        return "public page";
    }


    @GetMapping("/user")
    @PreAuthorize("hasAuthority('USER')")
    public String UserPage() {
        return "user page";
    }


    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String AdminPage() {
        return "Admin page";
    }


    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        userService.newUser(user);
        return user;
    }


    @PostMapping("/authenticate")
    public String authenticatedAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("invalid user");
        }
    }
}

package com.example.batchconfig.security.auth;

import com.example.batchconfig.event.RegistrationCompleteEvent;
import com.example.batchconfig.security.user.User;
import com.example.batchconfig.security.user.UserService;
import com.example.batchconfig.security.user.token.VerificationToken;
import com.example.batchconfig.security.user.token.VerificationTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;
  private final ApplicationEventPublisher publisher;
  private final VerificationTokenRepository tokenRepository;
  private final UserService userService;
  @GetMapping("/verifyEmail")
  public String verifyEmail(@RequestParam("token") String token){
    VerificationToken theToken = tokenRepository.findByToken(token);
    if (theToken.getUser().isEnabled()){
      return "This account has already been verified, please, login.";
    }
    String verificationResult = userService.validateToken(token);
    if (verificationResult.equalsIgnoreCase("valid")){
      return "Email verified successfully. Now you can login to your account";
    }
    return "Invalid verification token";
  }

  @PostMapping
  public String registerUser(@RequestBody RegistrationRequest registrationRequest, final HttpServletRequest request){
    User user = userService.registerUser(registrationRequest);
    publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
    return "Success!  Please, check your email for to complete your registration";
  }
  public String applicationUrl(HttpServletRequest request) {
    return "http://" +request.getServerName()+":"+request.getServerPort()+request.getContextPath();
  }
  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
      @RequestBody RegisterRequest request
  ) {
    return ResponseEntity.ok(service.register(request));
  }
  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request
  ) {
    return ResponseEntity.ok(service.authenticate(request));
  }

  @PostMapping("/refresh-token")
  public void refreshToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws IOException {
    service.refreshToken(request, response);
  }


}

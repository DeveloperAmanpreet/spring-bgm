package com.app.preRegister;

import com.app.common.user.UserService;
import com.app.common.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@CrossOrigin( origins = {"*"} , maxAge = 3600)
@RestController
@RequestMapping(Constants.URI.PRE_REGISTER)
public class RegisterController {

  private final RestTemplate restTemplate;

  private final PreRegisterService preRegisterService;

  public RegisterController(RestTemplate restTemplate, PreRegisterService preRegisterService){
    this.restTemplate = restTemplate;
    this.preRegisterService = preRegisterService;
  }

  @PostMapping
  public ResponseEntity<ResponseDTO> createCaptcha(@RequestBody RegisterDTO registerDTO) {
    log.info("createCaptcha: {}, {}", registerDTO.getToken(), registerDTO.getEmail());
    ResponseDTO responseDTO = new ResponseDTO();
    try {
      UriComponentsBuilder ucb = UriComponentsBuilder.newInstance().scheme("https").host("www.google.com").path("/recaptcha/api/siteverify");
      ucb.queryParam("secret", "6LeIxAcTAAAAAGG-vFI1TnRWxMZNFuojJ4WifJWe").queryParam("response", registerDTO.getToken());
      URI uri = ucb.build().encode().toUri();
      ResponseEntity<String> response = restTemplate.postForEntity(uri, null, String.class);
      log.info(response.toString());

      if (response.getStatusCode().value() == 200) {
        boolean result = preRegisterService.registerUser(registerDTO.getEmail());
        if(result){
          responseDTO.setError(null);
          responseDTO.setMessage("Registration Successful");
          return ResponseEntity.ok(responseDTO);
        }
      }

      responseDTO.setError("Possible Bot");
      responseDTO.setMessage("Registration Failed");
      return ResponseEntity.badRequest().body(responseDTO);
    } catch (IllegalArgumentException e) {
      log.error("Error Registering User: {}. Error: {}", registerDTO.getToken(), e.getMessage());
      responseDTO.setMessage(e.getMessage());
      responseDTO.setError(e.getMessage());
     return ResponseEntity.badRequest().body(responseDTO);
    } catch (Exception e) {
      log.error("Error Registering token: {}. Error: {}", registerDTO.getToken(), e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }
}

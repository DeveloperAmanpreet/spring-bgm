package com.app.google;


import com.app.common.utils.Constants;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.URI.CAPTCHA)
public class CaptchaController {


  @PostMapping
  public void createCaptcha() {

  }

}

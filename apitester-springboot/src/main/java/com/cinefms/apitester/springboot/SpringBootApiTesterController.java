package com.cinefms.apitester.springboot;

import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cinefms.apitester.annotations.ApiIgnore;
import com.cinefms.apitester.springmvc.controllers.ApiTesterController;

@PropertySource("classpath:apitester.properties")
@RestController
@RequestMapping(value={"${apitester.prefix}"})
@ApiIgnore
public class SpringBootApiTesterController extends ApiTesterController {

}

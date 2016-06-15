package com.cinefms.apitester.webapp;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cinefms.apitester.springmvc.controllers.ApiTesterController;

@RestController
@RequestMapping(value={""})
public class WebappApiTesterController extends ApiTesterController {

}

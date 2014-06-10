package com.cinefms.apitester.core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.cinefms.apitester.model.info.ApiCall;

@Component
public class ApitesterService {
	
	private List<ApiCall> calls = new ArrayList<ApiCall>();

}

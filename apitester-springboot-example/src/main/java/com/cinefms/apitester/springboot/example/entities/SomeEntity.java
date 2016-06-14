package com.cinefms.apitester.springboot.example.entities;

import java.util.ArrayList;
import java.util.List;

public class SomeEntity {
	
	private List<AnotherEntity> others = new ArrayList<>();

	public List<AnotherEntity> getOthers() {
		return others;
	}

	public void setOthers(List<AnotherEntity> others) {
		this.others = others;
	}

}

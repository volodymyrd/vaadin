package com.gmail.volodymyrdotsenko.vaadindemo.spring.beans;

import java.io.Serializable;

public class UtilsBean implements Serializable {
	public UtilsBean() {
		System.out.println("init UtilsBean");
	}

	public String getVersion() {
		return "version: 1.0";
	}
}
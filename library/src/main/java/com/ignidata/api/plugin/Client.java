package com.ignidata.api.plugin;

import java.io.Serializable;
import java.util.List;

public class Client implements Serializable {
	Object id;
	String userId;
    String userAgent; // new requirement for first-time users correct id
	List<Partner> partner;
	String gender;
	String country;
	int age;
}


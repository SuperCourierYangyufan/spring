package com.luban.dao;

import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;


public class IndexDao1 {
	public IndexDao1(){
		System.out.println("dao1-init");
	}
	public void query(){
		System.out.println("index1");
	}


}

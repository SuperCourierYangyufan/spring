package com.luban.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 杨宇帆
 * @create 2020-05-29
 */
@Component
public class BaseImpl implements Base {
	@Autowired
	private App app;

	@Override
	public void query() {
		System.out.println("i am is base");
	}
}

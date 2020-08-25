package com.luban.test;

import com.luban.app.Appconfig;
import com.luban.dao.Base;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author 杨宇帆
 * @create 2020-05-14
 */
@EnableAspectJAutoProxy
public class Test {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Appconfig.class);
		Base bean = applicationContext.getBean(Base.class);
		bean.query();

	}
}

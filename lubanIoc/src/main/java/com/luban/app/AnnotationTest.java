package com.luban.app;

/**
 * @author 杨宇帆
 * @create 2020-05-29
 */

import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AnnotationTest {

	@Pointcut("execution(* com.luban.dao..*.query(..))")
	public void logAop() {
	}


	@Before("logAop()")
	public void logBefore() {
		System.out.println("我是前置处理器");
	}

	@After("logAop()")
	public void logAfterReturning() {
		System.out.println("我是后置处理器");
	}
}

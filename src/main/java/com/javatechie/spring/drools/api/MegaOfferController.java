package com.javatechie.spring.drools.api;

import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MegaOfferController {
	/*
	 * @Autowired
	 * 
	 * @Qualifier("kieSession") private KieSession session;
	 */
	
	@Autowired
	ApplicationContext applicationContext;

	@PostMapping("/order")
	public Order orderNow(@RequestBody Order order) {
		KieSession session = (KieSession) applicationContext.getBean("kieSession");
		session.insert(order);
		session.fireAllRules();
		return order;
	}

}

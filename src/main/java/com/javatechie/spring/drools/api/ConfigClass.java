package com.javatechie.spring.drools.api;

import java.io.IOException;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.Message;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
@Configuration
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ConfigClass {
	
	private KieServices kieServices = KieServices.Factory.get();

	private KieFileSystem getKieFileSystem() throws IOException {
		/*
		 * KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
		 * kieFileSystem.write(ResourceFactory.newClassPathResource("offer.drl"));
		 */
		
		KieServices ks = KieServices.Factory.get();
		KieRepository kr = ks.getRepository();
		KieFileSystem kfs = ks.newKieFileSystem();

		kfs.write(ResourceFactory.newClassPathResource("rules/offer.drl", this.getClass()));
		
		return kfs;

	}

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public KieContainer getKieContainer() throws IOException {
		System.out.println("Container created...");
		getKieRepository();
		KieBuilder kb = kieServices.newKieBuilder(getKieFileSystem());
		kb.buildAll();
		KieModule kieModule = kb.getKieModule();
		KieContainer kContainer = kieServices.newKieContainer(kieModule.getReleaseId());
		return kContainer;

	}

	private void getKieRepository() {
		final KieRepository kieRepository = kieServices.getRepository();
		kieRepository.addKieModule(new KieModule() {
			public ReleaseId getReleaseId() {
				return kieRepository.getDefaultReleaseId();
			}
		});
	}

	@Bean(name = "kieSession")
	@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public KieSession getKieSession() throws IOException {
		System.out.println("session created...");
		return getKieContainer().newKieSession();
		//return session();

	}
	
	
	public KieSession session() {
		KieServices ks = KieServices.Factory.get();
		KieRepository kr = ks.getRepository();
		KieFileSystem kfs = ks.newKieFileSystem();

		kfs.write(ResourceFactory.newClassPathResource("rules/offer.drl", this.getClass()));

		KieBuilder kb = ks.newKieBuilder(kfs);

		kb.buildAll(); // kieModule is automatically deployed to KieRepository if successfully built.
		if (kb.getResults().hasMessages(Message.Level.ERROR)) {
			throw new RuntimeException("Build Errors:\n" + kb.getResults().toString());
		}

		KieContainer kContainer = ks.newKieContainer(kr.getDefaultReleaseId());

		KieSession kSession = kContainer.newKieSession();
		return kSession;
		
	}



}

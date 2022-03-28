package com.javatechie.spring.drools.api.file;

import java.util.Collection;

import org.drools.core.impl.KnowledgeBaseFactory;
import org.drools.core.marshalling.impl.ProtobufMessages.KnowledgeBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.Message;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.StatefulKnowledgeSession;

public class RuleRunner {

	public void runRules(String[] rules, Object[] facts) {
		
		  
		/*
		 * KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		 * KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		 * 
		 * for (int i = 0; i < rules.length; i++) { String ruleFile = rules[i];
		 * System.out.println("Loading file: " + ruleFile);
		 * kbuilder.add(ResourceFactory.newClassPathResource(ruleFile,
		 * RuleRunner.class), ResourceType.DRL); }
		 * 
		 * Collection<KnowledgePackage> pkgs = kbuilder.getKnowledgePackages();
		 * kbase.addKnowledgePackages(pkgs);
		 * 
		 * ResourceFactory.getResourceChangeNotifierService().start();
		 * ResourceFactory.getResourceChangeScannerService().start();
		 * 
		 * StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		 * 
		 * for (int i = 0; i < facts.length; i++) { Object fact = facts[i];
		 * System.out.println("Inserting fact: " + fact); ksession.insert(fact); }
		 * 
		 * ksession.fireAllRules();
		 */
		 }

	public void readFiles(Object[] facts) {
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

		for (int i = 0; i < facts.length; i++) {
			Object fact = facts[i];
			System.out.println("Inserting fact: " + fact);
			kSession.insert(fact);
		}

		System.out.println("Fire All Rules...");
		kSession.fireAllRules();
		kSession.dispose();
	}

	public static void main(String[] args) {
		RuleRunner ruleRunner = new RuleRunner();
		ruleRunner.readFiles(args);
	}
	
	
}

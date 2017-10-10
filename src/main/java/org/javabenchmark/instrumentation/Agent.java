package org.javabenchmark.instrumentation;

import java.lang.instrument.Instrumentation;

import javassist.ClassPool;
import javassist.CtClass;

public class Agent {

	public static void premain(String agentArgs, Instrumentation inst) {
//		System.out.println("is Pattern modifiable: " + inst.isModifiableClass(java.util.regex.Pattern.class));
//		System.out.println("is Matcher modifiable: " + inst.isModifiableClass(java.util.regex.Matcher.class));
//		System.out.println("is String modifiable: " + inst.isModifiableClass(java.lang.String.class));

		// registers the transformer
//		inst.addTransformer(new SleepingClassFileTransformer());
		
		String log_name="regex";
		if(agentArgs!= null){
			log_name=agentArgs;
		}
		inst.addTransformer(new RegexClassFileTransformer2(log_name));

	}
}

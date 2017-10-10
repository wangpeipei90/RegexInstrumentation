package org.javabenchmark.instrumentation;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

public class Agent3 {

    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new ClassFileTransformer() {
            public byte[] transform(ClassLoader classLoader, String s, Class<?> aClass, ProtectionDomain protectionDomain, byte[] bytes) throws IllegalClassFormatException {

                if ("java/util/regex/Pattern".equals(s)) {
                    // Javassist
                    try {
                        ClassPool cp = ClassPool.getDefault();
                        CtClass cc = cp.get("java.util.regex.Pattern");
                        CtClass[] params1 = { cp.get("java.lang.String") };
                        CtMethod m1 = cc.getDeclaredMethod("compile",params1);
                      //  MethodInfo methodInfo = m.getMethodInfo();
                       // MethodParametersAttribute table = (MethodParametersAttribute) methodInfo.getCodeAttribute().getAttribute(javassist.bytecode.MethodParametersAttribute.tag);
//                        int numberOfLocalVariables = table.tableLength(); 
//                        System.out.println("local number vars: "+table.tableLength());
//                        System.out.println("len: "+table.length());
//                        System.out.println("i: "+table.);
                        //m.addLocalVariable("elapsedTime", CtClass.longType);
//                        m.insertBefore("elapsedTime = System.currentTimeMillis();");
                        m1.insertBefore("System.out.println(\"------Pattern compile(String regex)---regex: \"+$1);");
//                        m.insertAfter("{elapsedTime = System.currentTimeMillis() - elapsedTime;"
                         //       + "System.out.println(\"Method Executed in ms: \" + elapsedTime);}");
                        
                        CtClass[] params2 = { cp.get("java.lang.String"), CtClass.intType };
        				CtMethod m2 = cc.getDeclaredMethod("compile", params2);
        				m2.insertBefore(
        						"System.out.println(\"------Pattern compile(String regex)---regex: \" + $1+\"---flags: \"+$2);");

                        byte[] byteCode = cc.toBytecode();
                        cc.detach();
                        return byteCode;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                return null;
            }
        });
    }
}
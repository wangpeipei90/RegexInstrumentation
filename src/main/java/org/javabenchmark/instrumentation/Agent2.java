package org.javabenchmark.instrumentation;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class Agent2 {
    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new ClassFileTransformer() {
            public byte[] transform(ClassLoader classLoader, String s, Class aClass,
        			ProtectionDomain protectionDomain, byte[] bytes) throws IllegalClassFormatException {

                // Intercept the call to the class Stuff
                if ("java/util/regex/Pattern".equals(s)) {
                    // ASM Code
                    ClassReader reader = new ClassReader(bytes);
                    ClassWriter writer = new ClassWriter(reader, 0);
                    ClassPrinter visitor = new ClassPrinter(writer);
                    reader.accept(visitor, 0);
                    return writer.toByteArray();
                }
                return null;
            }
        });
    }
}

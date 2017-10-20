package org.javabenchmark.instrumentation;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.regex.Pattern;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * java.util.regex.Pattern;java.util.regex.Matcher;java.lang.String
 *
 * @author peipei
 *
 */
public class RegexClassFileTransformer4 implements ClassFileTransformer {
	private static String log_file;
	public RegexClassFileTransformer4(String log_name) {
		// TODO Auto-generated constructor stub
		log_file="/home/peipei/RepoReaper/logs/"+log_name+".log";
	}

	private void logRegexString(CtMethod m, String s) throws CannotCompileException{
		StringBuilder sbs = new StringBuilder();
		// sbs.append( "StringBuilder sb = new StringBuilder();" );
		// sbs.append( "sb.append("+s+");");
		sbs.append( "try {" );
//		sbs.append( "   java.io.FileWriter  fw  = new java.io.FileWriter( \""+log_file+"\", true );" );
		sbs.append( "   java.io.OutputStreamWriter fw = new java.io.OutputStreamWriter(new java.io.FileOutputStream( \""+log_file+"\", true ), java.nio.charset.StandardCharsets.UTF_8);");
		sbs.append( "   java.io.PrintWriter out = new java.io.PrintWriter( fw, true );" );

		sbs.append("	java.lang.StackTraceElement[] stackTrace=java.lang.Thread.currentThread().getStackTrace();");
		sbs.append("	int count=0;");
		sbs.append("	for(int i=0;i<stackTrace.length;i++){");
		sbs.append("		java.lang.StackTraceElement element=stackTrace[i];");
		sbs.append("		if(element.getMethodName().equals(\"invoke\") && element.getClassName().equals(\"java.lang.reflect.Method\") && element.getLineNumber()==606 && element.getFileName().equals(\"Method.java\")){");
		sbs.append("			count+=1;");
		sbs.append( "		}" );
		sbs.append( "	}" );
		
		sbs.append("	if(count>1){");
		sbs.append( "   	out.println("+s+");" );
		sbs.append( "	}" );

		sbs.append( "   fw.close();" );
		sbs.append( "   out.close();" );
		sbs.append( "} catch (java.io.IOException e) {" );
		sbs.append( "   e.printStackTrace();" );
		sbs.append( "}" );
    		System.out.println(sbs);
		m.insertBefore("{" + sbs.toString() + ";}");
  }

	public byte[] transform(ClassLoader loader, String className, Class classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] bytes) throws IllegalClassFormatException {
		//System.out.println("RegexClassFileTransformer");
		ClassPool cp;
		CtClass cc;

		if (className.equals("java/util/regex/Pattern")) {
			try {
				cp = ClassPool.getDefault();
				cc = cp.get("java.util.regex.Pattern");

				// static Pattern java.util.regex.Pattern compile(String regex)
				CtClass[] params1 = { cp.get("java.lang.String") };
				CtMethod m1 = cc.getDeclaredMethod("compile", params1);
				//m1.insertBefore("System.out.println(\"------Pattern compile(String regex)---regex: \"+$1);");
				logRegexString(m1,"\"Pattern compile(String regex)---regex: \"+$1");
				
				// static Pattern java.util.regex.Pattern compile(String regex,
				// int flags)
				CtClass[] params2 = { cp.get("java.lang.String"), CtClass.intType };
				CtMethod m2 = cc.getDeclaredMethod("compile", params2);
//				m2.insertBefore(
//						"System.out.println(\"------Pattern compile(String regex)---regex: \" + $1+\"---flags: \"+$2);");
				logRegexString(m2,"\"Pattern compile(String regex)---regex: \" + $1+\"---flags: \"+$2");
				
				// Matcher java.util.regex.Pattern matcher(CharSequence input)
				CtMethod m3 = cc.getDeclaredMethod("matcher");
				//m3.insertBefore("System.out.println(\"------Pattern matcher(CharSequence input)---regex: \" + this.pattern+\"---input: \"+$1);");
				logRegexString(m3,"\"Pattern matcher(CharSequence input)---regex: \" + this.pattern+\"---input: \"+$1");

				// static boolean java.util.regex.Pattern matches(String regex,
				// CharSequence input)
				CtMethod m4 = cc.getDeclaredMethod("matches");
//				m4.insertBefore(
//						"System.out.println(\"------Pattern matches(String regex, CharSequence input)---regex: \" + $1+\"---input: \"+$2);");
				logRegexString(m4,"\"Pattern matches(String regex, CharSequence input)---regex: \" + $1+\"---input: \"+$2");

				// String[] java.util.regex.Pattern split(CharSequence input)
				CtClass[] params5 = { cp.get("java.lang.CharSequence") };
				CtMethod m5 = cc.getDeclaredMethod("split", params5);
//				m5.insertBefore("System.out.println(\"------Pattern split(CharSequence input)---regex: \" + this.pattern+\"---input: \" + $1);");
				logRegexString(m5,"\"Pattern split(CharSequence input)---regex: \" + this.pattern+\"---input: \" + $1");

				// String[] java.util.regex.Pattern split(CharSequence input,
				// int limit)
				CtClass[] params6 = { cp.get("java.lang.CharSequence"), CtClass.intType };
				CtMethod m6 = cc.getDeclaredMethod("split", params6);
			//	m6.insertBefore(
			//			"System.out.println(\"------Pattern split(CharSequence input, int limit)---regex: \" + this.pattern+\"---input: \" + $1+\"---limit: \"+$2);");
				logRegexString(m6,"\"Pattern split(CharSequence input, int limit)---regex: \" + this.pattern+\"---input: \" + $1+\"---limit: \"+$2");

				byte[] byteCode = cc.toBytecode();
				cc.detach();
				return byteCode;
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CannotCompileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (className.equals("java/util/regex/Matcher")) {
			try {
				cp = ClassPool.getDefault();
				cc = cp.get("java.util.regex.Matcher");
				// public Matcher java.util.regex.Matcher
				// appendReplacement(StringBuffer sb, String replacement)
//				CtMethod m1 = cc.getDeclaredMethod("appendReplacement");
//				m1.insertBefore(
//						"System.out.println(\"------Matcher appendReplacement(StringBuffer sb, String replacement)---replace: \" + $2);");

				// public StringBuffer java.util.regex.Matcher
				// appendTail(StringBuffer sb)
//				CtMethod m2 = cc.getDeclaredMethod("appendTail");
//				m2.insertBefore(
//						"System.out.println(\"------Matcher appendTail(StringBuffer sb)\");");

				// boolean java.util.regex.Matcher find()
				CtMethod m3 = cc.getDeclaredMethod("find", null);
				//m3.insertBefore(
				//		"System.out.println(\"------Matcher find()---regex: \"+this.parentPattern+\"---input: \"+this.text);");
				logRegexString(m3,"\"Matcher find()---regex: \"+this.parentPattern+\"---input: \"+this.text");

				// boolean java.util.regex.Matcher find(int start)
				CtClass[] params4 = { CtClass.intType };
				CtMethod m4 = cc.getDeclaredMethod("find", params4);
				//m4.insertBefore(
				//		"System.out.println(\"------Matcher find(int start)---regex: \"+this.parentPattern+\"---input: \"+this.text+\"---start: \" + $1);");
				logRegexString(m4,"\"Matcher find(int start)---regex: \"+this.parentPattern+\"---input: \"+this.text+\"---start: \" + $1");

				// boolean java.util.regex.Matcher hitEnd()
//				CtMethod m5 = cc.getDeclaredMethod("hitEnd");
//				m5.insertBefore(
//						"System.out.println(\"------Matcher hitEnd()\");");

				// boolean java.util.regex.Matcher hasAnchoringBounds()
//				CtMethod m6 = cc.getDeclaredMethod("hasAnchoringBounds");
//				m6.insertBefore(
//						"System.out.println(\"------Matcher hasAnchoringBounds()\");");

				// boolean java.util.regex.Matcher hasTransparentBounds()
//				CtMethod m7 = cc.getDeclaredMethod("hasTransparentBounds");
//				m7.insertBefore(
//						"System.out.println(\"------Matcher hasTransparentBounds()	\");");

				// boolean java.util.regex.Matcher lookingAt()
				CtMethod m8 = cc.getDeclaredMethod("lookingAt");
				//m8.insertBefore(
				//		"System.out.println(\"------Matcher lookingAt()---regex: \"+this.parentPattern+\"---input: \"+this.text);");
				logRegexString(m8,"\"Matcher lookingAt()---regex: \"+this.parentPattern+\"---input: \"+this.text");

				// boolean java.util.regex.Matcher matches()
				CtMethod m9 = cc.getDeclaredMethod("matches");
				//m9.insertBefore(
				//		"System.out.println(\"------Matcher matches()---regex: \"+this.parentPattern+\"---input: \"+this.text);");
				logRegexString(m9,"\"Matcher matches()---regex: \"+this.parentPattern+\"---input: \"+this.text");

				// Matcher java.util.regex.Matcher reset(CharSequence input)
				CtClass[] params10 = { cp.get("java.lang.CharSequence")};
				CtMethod m10 = cc.getDeclaredMethod("reset", params10);
				//m10.insertBefore(
				//		"System.out.println(\"------Matcher reset(CharSequence input)---regex: \"+this.parentPattern+\"---input: \"+this.text+\"---newinput: \" + $1);");
				logRegexString(m10,"\"Matcher reset(CharSequence input)---regex: \"+this.parentPattern+\"---input: \"+this.text+\"---newinput: \" + $1");

				// Matcher java.util.regex.Matcher reset()
				CtMethod m11 = cc.getDeclaredMethod("reset", null);
				//m11.insertBefore(
				//		"System.out.println(\"------Matcher reset()\");");
				logRegexString(m11,"\"Matcher reset()\"");

				// Matcher java.util.regex.Matcher usePattern(Pattern
				// newPattern)
				CtMethod m12 = cc.getDeclaredMethod("usePattern");
				//m12.insertBefore(
				//		"System.out.println(\"------Matcher usePattern(Pattern newPattern)---regex: \"+this.parentPattern+\"---input: \"+this.text+\"---newregex: \" + $1);");
				logRegexString(m12,"\"Matcher usePattern(Pattern newPattern)---regex: \"+this.parentPattern+\"---input: \"+this.text+\"---newregex: \" + $1");

				byte[] byteCode = cc.toBytecode();
				cc.detach();
				return byteCode;
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CannotCompileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (className.equals("java/lang/String")) {
			try {
				cp = ClassPool.getDefault();
				cc = cp.get("java.lang.String");

				// boolean java.lang.String matches(String regex)
				//return Pattern.matches(regex, this);
				CtMethod m1 = cc.getDeclaredMethod("matches");
				//m1.insertBefore(
				//		"System.out.println(\"------String matches(String regex)---regex: \" + $1+\"---input: \"+this);");
				logRegexString(m1,"\"------String matches(String regex)---regex: \" + $1+\"---input: \"+this");

				// String java.lang.String replaceAll(String regex, String
				// replacement)
				//return Pattern.compile(regex).matcher(this).replaceAll(replacement);
//				CtMethod m2 = cc.getDeclaredMethod("replaceAll");
//				m2.insertBefore(
//						"System.out.println(\"------String replaceAll(String regex, String replacement)---regex: \" + $1+\"---replace: \"+$2);");

				// String java.lang.String replaceFirst(String regex, String
				// replacement)
				//return Pattern.compile(regex).matcher(this).replaceFirst(replacement);
//				CtMethod m3 = cc.getDeclaredMethod("replaceFirst");
//				m3.insertBefore(
//						"System.out.println(\"------String replaceFirst(String regex, String replacement)---regex: \" + $1+\"---replace: \"+$2);");
//
				// String[] java.lang.String split(String regex)
				//return split(regex, 0);
				CtClass[] params4 = { cp.get("java.lang.String")};
				CtMethod m4 = cc.getDeclaredMethod("split",params4);
				//m4.insertBefore(
				//		"System.out.println(\"------String split(String regex)---regex: \" + $1+\"---input: \"+this);");
				logRegexString(m4,"\"------String split(String regex)---regex: \" + $1+\"---input: \"+this");

				// String[] java.lang.String split(String regex, int limit)
				// if{...return ...}return Pattern.compile(regex).split(this, limit);
				CtClass[] params5 = { cp.get("java.lang.String"), CtClass.intType};
				CtMethod m5 = cc.getDeclaredMethod("split",params5);
				//m5.insertBefore(
				//		"System.out.println(\"------String split(String regex, int limit)---regex: \" + $1+\"---input: \"+this+\"---limit\"+$2);");
				logRegexString(m5,"\"------String split(String regex, int limit)---regex: \" + $1+\"---input: \"+this+\"---limit\"+$2");

				byte[] byteCode = cc.toBytecode();
				cc.detach();
				return byteCode;
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CannotCompileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}

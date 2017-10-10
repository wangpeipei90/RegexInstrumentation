package org.javabenchmark.instrumentation;

import org.junit.Test;

public class TestExample1 {
	@Test
	public void main() {
	
		String[] words = { "{apf", "hum_", "dkoe", "12f" };

		for (String s : words) {
			if (s.matches("[a-z]+")) {
				System.out.println(s);
			}
		}
	}
}

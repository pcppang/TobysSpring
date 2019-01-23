package com.david.learningtest.template;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;

public class CalcSumTest {
	@Test
	public void sumOfNumbers() throws IOException {
		String path = getClass().getResource("").getPath() + "numbers.txt";
		
		Calculator calculator = new Calculator();
		int sum = calculator.calcSum(path);
		
		assertThat(sum, is(10));
	}
}

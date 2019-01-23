package com.david.learningtest.template;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;

public class CalcSumTest {
	Calculator calculator;
	String numFilePath;
	
	@Test
	public void sumOfNumbers() throws IOException {
		this.calculator = new Calculator();
		this.numFilePath = getClass().getResource("").getPath() + "numbers.txt";
		
		assertThat(calculator.calcSum(this.numFilePath), is(10));
	}
}

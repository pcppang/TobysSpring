package com.david.learningtest.template;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class CalcSumTest {
	Calculator calculator;
	String numFilePath;
	
	@Before
	public void setUp() {
		this.calculator = new Calculator();
		this.numFilePath = getClass().getResource("").getPath() + "numbers.txt";
	}
	
	@Test
	public void sumOfNumbers() throws IOException {	
		assertThat(calculator.calcSum(this.numFilePath), is(10));
	}
	
	@Test
	public void multipleOfNumbers() throws IOException {	
		assertThat(calculator.calcMultiple(this.numFilePath), is(24));
	}
}

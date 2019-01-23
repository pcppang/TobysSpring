package com.david.learningtest.template;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

public class CalcSumTest {
	@Test
	public void sumOfNumbers() throws IOException {
		String path = CalcSumTest.class.getResource("").getPath() + "numbers.txt";
		
		BufferedReader br = new BufferedReader(new FileReader(path));
		
		Integer sum = 0;
		String line = null;
		
		while ((line = br.readLine()) != null) {
			sum += Integer.valueOf(line);
		}
		
		br.close();
		
		assertThat(sum, is(10));
	}
}

package com.david.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
	public int calcSum(String path) throws IOException {
		BufferedReader br = null;
		Integer sum = 0;
		String line = null;

		try {
			br = new BufferedReader(new FileReader(path));
			
			while ((line = br.readLine()) != null) {
				sum += Integer.valueOf(line);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sum;
	}
}

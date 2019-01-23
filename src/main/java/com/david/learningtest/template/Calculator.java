package com.david.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
	public int calcSum(String filepath) throws IOException {
		BufferedReaderCallback sumCallback = new BufferedReaderCallback() {
			@Override
			public Integer doSomethingWithReader(BufferedReader br) throws IOException {
				Integer sum = 0;
				String line = null;

				while ((line = br.readLine()) != null) {
					sum += Integer.valueOf(line);
				}
					
				return sum;
			}
		};
		return fileReadTemplate(filepath, sumCallback);
	}
	
	public Integer fileReadTemplate(String path, BufferedReaderCallback callback) throws IOException {
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(path));
			int ret = callback.doSomethingWithReader(br);
			return ret;
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
	}
}

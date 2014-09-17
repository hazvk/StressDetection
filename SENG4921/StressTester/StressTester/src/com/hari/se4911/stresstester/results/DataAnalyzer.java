package com.hari.se4911.stresstester.results;

import java.util.ArrayList;
import java.util.List;

public class DataAnalyzer {

	List<StressResult> results;
	
	public DataAnalyzer(List<StressResult> results) {
		this.results = results;
	}

	public DataAnalyzer() {
		results = new ArrayList<StressResult>();
	}
	
	public void analyze() {
		// TODO Auto-generated method stub
	}
	
	public void add(StressResult sr) {
		results.add(sr);
	}

}

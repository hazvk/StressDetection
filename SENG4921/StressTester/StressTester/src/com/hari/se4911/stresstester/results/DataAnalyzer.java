package com.hari.se4911.stresstester.results;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataAnalyzer {

	List<StressResult> results;
	private StressResult v1;
	private StressResult v2;
	
	private Map<String, Float> weightVector;
	private float a;
	private float b;
	
	public DataAnalyzer(List<StressResult> results) {
		this.results = results;
		weightVector = new HashMap<>();
		this.analyze();
	}

	public DataAnalyzer() {
		results = new ArrayList<StressResult>();
		a = 0;
		b = 0;
		weightVector = new HashMap<>();
	}
	
	public void analyze() {
		idPoints();
		setWeightVector();
		findMidpoint();
		float[] exp1 = getExp(v1);
		float[] exp2 = getExp(v2);
		findEqn(exp1, exp2);
		
	}
	
	private void idPoints() {
		Map<String, List<StressResult>> dividePoints = categorize();
		findShortest(dividePoints.get("pos"), dividePoints.get("neg"));
	}
	
	private Map<String, List<StressResult>> categorize() {
		List<StressResult> pos = new ArrayList<>();
		List<StressResult> neg = new ArrayList<>();
		for (StressResult s: results) {
			if (s.isStressed()) pos.add(s);
			else neg.add(s);
		}
		
		Map<String, List<StressResult>> ans = new HashMap<String, List<StressResult>>();
		ans.put("pos", pos);
		ans.put("neg", neg);
		
		return ans;
	}
	
	private void findShortest(List<StressResult> pos, List<StressResult> neg) {
		// TODO Auto-generated method stub
		// implement algorithm here
		
	}

	private void setWeightVector() {
		weightVector.put("accel0", v1.getAvgCountTurns()[0] - v2.getAvgCountTurns()[0]);
		weightVector.put("accel1", v1.getAvgCountTurns()[1] - v2.getAvgCountTurns()[1]);
		weightVector.put("hydro", v1.getAvgHydro() - v2.getAvgHydro());
		weightVector.put("voice0", v1.getAvgVoice()[0] - v2.getAvgVoice()[0]);
		weightVector.put("voice1", v1.getAvgVoice()[1] - v2.getAvgVoice()[1]);
	}

	private void findMidpoint() {
		// TODO Auto-generated method stub
		// DO WE NEED THIS?
		
	}

	private void findEqn(float[] exp1, float[] exp2) {
		a = (exp1[1]-exp2[1])/(exp1[0]-exp2[0]);
		b = exp1[1] - a*exp1[0];
		
	}

	public void add(StressResult sr) {
		results.add(sr);
		this.analyze();
	}
	
	private float[] getExp(StressResult v) {
		Float sum = (float) 0;
		sum += weightVector.get("accel0")*v.getAvgCountTurns()[0];
		sum += weightVector.get("accel1")*v.getAvgCountTurns()[1];
		sum += weightVector.get("hydro")*v.getAvgHydro();
		sum += weightVector.get("voice0")*v.getAvgVoice()[0];
		sum += weightVector.get("voice1")*v.getAvgVoice()[1];
		
		float[] ans = new float[2];
		ans[0] = sum;
		if (v.isStressed())
			ans[1] = 1;
		else ans[1] = -1;
		
		return ans;
	}
	
	public float substitute(StressResult v) {
		Float sum = (float) 0;
		sum += weightVector.get("accel0")*v.getAvgCountTurns()[0];
		sum += weightVector.get("accel1")*v.getAvgCountTurns()[1];
		sum += weightVector.get("hydro")*v.getAvgHydro();
		sum += weightVector.get("voice0")*v.getAvgVoice()[0];
		sum += weightVector.get("voice1")*v.getAvgVoice()[1];
		
		return a*sum + b; 
	}
	

}

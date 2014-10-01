package com.hari.se4911.stresstester.results;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

public class DataAnalyzer {

	List<StressResult> res;
	private StressResult v1;
	private StressResult v2;
	
	private Map<String, Float> weightVector;
	private float a;
	private float b;
	private Map<String, Float> midVector;
	
	public DataAnalyzer(List<StressResult> results) {
		this.res = results;
		this.analyze();
	}

	public DataAnalyzer() {
		res = new ArrayList<StressResult>();
		a = 0;
		b = 0;
		
	}
	
	public void analyze() {
		List<StressResult> results = excludeOutliers();
		idPoints(results);
		setWeightVector(results);
		findMidpoint(results);
		float[] exp1 = getExp(v1);
		float[] exp2 = getExp(v2);
		findEqn(exp1, exp2);
		
	}
	
	private List<StressResult> excludeOutliers() {
		List<StressResult> results = new ArrayList<StressResult>(res);
		for (StressResult s: results) {
			try {
				if (!s.isThresholdStressed() &&
						s.isStressed()) {
					results.remove(s);
				}
			} catch (NoResultsException e) {
				res.remove(s);
			}
		}
		return results;
	}

	private void idPoints(List<StressResult> results) {
		v1 = null;
		v2 = null;
		Map<String, List<StressResult>> dividePoints = categorize(results);
		findShortest(dividePoints.get("pos"), dividePoints.get("neg"));
	}
	
	private Map<String, List<StressResult>> categorize(List<StressResult> results) {
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
		// TODO better algorithm
		double minDistance = 0;
		for (StressResult p: pos) {
			for (StressResult n: neg) {
				if (v1 == null || v2 == null) {
					v1 = p;
					v2 = n;
					minDistance = findDist(v1, v2);
				} else {
					double dist = findDist(p,n);
					if (dist < minDistance) {
						v1 = p;
						v2 = n;
						minDistance = dist;
					}
				}
			}
		}
	}

	private double findDist(StressResult p, StressResult n) {
		double params = 0;
		params += Math.pow((v1.getAvgCountTurns()[0] - v2.getAvgCountTurns()[0]),2);
		params += Math.pow((v1.getAvgCountTurns()[1] - v2.getAvgCountTurns()[1]),2);
		params += Math.pow((v1.getAvgHydro() - v2.getAvgHydro()),2);
		params += Math.pow((v1.getAvgVoice()[0] - v2.getAvgVoice()[0]),2);
		params += Math.pow((v1.getAvgVoice()[1] - v2.getAvgVoice()[1]),2);
		
		return Math.sqrt(params);
	}

	private void setWeightVector(List<StressResult> results) {
		weightVector = new HashMap<>();
		weightVector.put("accel0", v1.getAvgCountTurns()[0] - v2.getAvgCountTurns()[0]);
		weightVector.put("accel1", v1.getAvgCountTurns()[1] - v2.getAvgCountTurns()[1]);
		weightVector.put("hydro", v1.getAvgHydro() - v2.getAvgHydro());
		weightVector.put("voice0", v1.getAvgVoice()[0] - v2.getAvgVoice()[0]);
		weightVector.put("voice1", v1.getAvgVoice()[1] - v2.getAvgVoice()[1]);
	}

	private void findMidpoint(List<StressResult> results) {
		// DO NOT NEED UNLESS USING OTHER IMPLEMENTATION
		midVector = new HashMap<String, Float>();
		midVector.put("accel0", (v1.getAvgCountTurns()[0] + v2.getAvgCountTurns()[0])/2);
		midVector.put("accel1", (v1.getAvgCountTurns()[1] + v2.getAvgCountTurns()[1])/2);
		midVector.put("hydro", (v1.getAvgHydro() + v2.getAvgHydro())/2);
		midVector.put("voice0", (v1.getAvgVoice()[0]+ v2.getAvgVoice()[0])/2);
		midVector.put("voice1", (v1.getAvgVoice()[1] + v2.getAvgVoice()[1])/2);
		
	}

	private void findEqn(float[] exp1, float[] exp2) {
		a = (exp1[1]-exp2[1])/(exp1[0]-exp2[0]);
		b = exp1[1] - a*exp1[0];
		StringBuilder logRes = new StringBuilder();
		logRes.append("SUMMARY - Boundary determined by:\n");
		logRes.append("Weight Vector: [ ");
		int i = 0;
		for (String s: weightVector.keySet()) {
			logRes.append(weightVector.get(s) * a);
			i++;
			if (i != weightVector.keySet().size()) {
				logRes.append(",");
			}
			logRes.append(" ");
		}
		logRes.append("]\n");
		logRes.append("b = " + b);
		Log.v("DataAnalyzer", logRes.toString());
	}

	public void add(StressResult sr) {
		res.add(sr);
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

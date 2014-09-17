package com.hari.se4911.stresstester.results;

import java.util.ArrayList;
import java.util.Map;

public class StressResult {

	private float averageCountTurns;
	private float avHydro;
	private float avVoice;
	private boolean isStressed;
	
	private Map<String, ArrayList<Float>> accelRes;
	private ArrayList<float[]> hydroRes;
	private ArrayList<Short> voiceRes;
	
	public StressResult() {
		initResults();
	}

	public StressResult(Map<String, ArrayList<Float>> accelRes,
			ArrayList<float[]> hydroRes, ArrayList<Short> voiceRes) {
		this.accelRes = accelRes;
		this.hydroRes = hydroRes;
		this.voiceRes = voiceRes;
		initResults();
	}
	
	private void initResults() {
		averageCountTurns = 0;
		avHydro = 0;
		avVoice = 0;
		isStressed = false;
	}
	
	public void analyze() throws NoResultsException {
		analyzeAccel();
		analyzeHydro();
		analyzeVoice();
	}

	
	private void analyzeAccel() throws NoResultsException {
		if (accelRes == null) throw new NoResultsException();
		else {
			averageCountTurns = accelRes.get("x").size();			
		}
		
	}

	private void analyzeHydro() throws NoResultsException {
		if (hydroRes == null) throw new NoResultsException();
		else {
			avHydro = hydroRes.size();		
		}
		
	}

	private void analyzeVoice() throws NoResultsException {
		if (voiceRes == null) throw new NoResultsException();
		else if (voiceRes.equals(new ArrayList<Short>())) avVoice = -1;
		else {
			int countShout = 0;
			int sum = 0;
			int total = voiceRes.size();
			for (Short v: voiceRes) {
				if (Math.abs(v) >= 100) {
					countShout++;
				}
				sum += Math.abs(v);
			}
			float fractionShouting = (float) (countShout/total);
			//TODO: use this
			
			avVoice = (float) (sum/total);
		}
		
	}
	
	/*
	 * *********************GETTERS AND SETTERS**********************
	 */

	public float getAverageCountTurns() {
		return averageCountTurns;
	}

	public void setAverageCountTurns(int averageCountTurns) {
		this.averageCountTurns = averageCountTurns;
	}

	public float getAvHydro() {
		return avHydro;
	}

	public void setAvHydro(float avHydro) {
		this.avHydro = avHydro;
	}

	public float getAvVoice() {
		return avVoice;
	}

	public void setAvVoice(int avVoice) {
		this.avVoice = avVoice;
	}

	public boolean isStressed() {
		return isStressed;
	}

	public void setStressed(boolean isStressed) {
		this.isStressed = isStressed;
	}
	
	/*
	 * ***********************FOR DataAnalyzer***********************
	 */
	
	public void setAccel(String next) {
		setAverageCountTurns(Integer.parseInt(next));
		
	}

	public void setHydro(String next) {
		setAvHydro(Float.parseFloat(next));
		
	}

	public void setVoice(String next) {
		setAvVoice(Integer.parseInt(next));
		
	}

}

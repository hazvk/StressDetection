package com.hari.se4911.stresstester.results;

import java.util.ArrayList;
import java.util.Map;

public class StressResult {

	private int averageCountTurns = 0;
	private float avHydro = 0;
	private int avVoice = 0;
	private boolean isStressed = false;
	
	public StressResult() {
		//TODO
	}

	public StressResult(Map<String, ArrayList<Float>> accelRes,
			ArrayList<float[]> hydroRes, ArrayList<Short> voiceRes) {
		// TODO Auto-generated constructor stub
	}
	
	public void analyze() {
		
	}

	
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

	public int getAvVoice() {
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

package com.hari.se4911.stresstester.results;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class StressResult {

	private float[] avgCountTurns;
	private float avgHydro;
	private float[] avgVoice;
	private float y;
	
	public StressResult() {
		initResults();
	}

	public StressResult(int[] accelRes,
			ArrayList<float[]> hydroRes, 
			ArrayList<Short> voiceRes) throws NoResultsException {
		initResults();
		analyzeAccel(accelRes);
		analyzeHydro(hydroRes);
		analyzeVoice(voiceRes);
		y = 0;
	}
	
	private void initResults() {
		avgCountTurns = new float[2];
		avgHydro = 0;
		avgVoice = new float[2];
		y = 0;
	}
	
	public void analyze(DataAnalyzer da) throws NoResultsException {
		this.y = da.substitute(this);
	}

	
	private void analyzeAccel(int[] accelRes) throws NoResultsException {
		int[] toComp = {0, 0, 0};
		if (accelRes == toComp) throw new NoResultsException();
		else {
			avgCountTurns[0] = accelRes[0]/accelRes[2];
			avgCountTurns[1] = accelRes[1]/accelRes[2];
		}
		
	}

	private void analyzeHydro(ArrayList<float[]> hydroRes) throws NoResultsException {
		if (hydroRes == null) throw new NoResultsException();
		else {
			int sum = 0;
			int total = hydroRes.size();
			for (float[] h: hydroRes) {
				sum += h[0];
			}
			avgHydro = sum/total;	
		}
		
	}

	private void analyzeVoice(ArrayList<Short> voiceRes) throws NoResultsException {
		if (voiceRes == null) throw new NoResultsException();
		else if (voiceRes.equals(new ArrayList<Short>())) {
			avgVoice[0] = -1;
			avgVoice[1] = -1;
		}
		else {
			int countShout = 0;
			int sum = 0;
			int total = voiceRes.size();
			for (Short v: voiceRes) {
				if (Math.abs(v) >= 75) {
					countShout++;
				}
				sum += Math.abs(v);
			}
			avgVoice[0] = countShout/total;
			avgVoice[1] = sum/total;
		}
		
	}
	
	/*
	 * *********************GETTERS AND SETTERS**********************
	 */

	public float[] getAvgCountTurns() {
		return avgCountTurns;
	}

	public void setAvgCountTurns(float[] avgCountTurns) {
		this.avgCountTurns = avgCountTurns;
	}

	public float getAvgHydro() {
		return avgHydro;
	}

	public void setAvgHydro(float avgHydro) {
		this.avgHydro = avgHydro;
	}

	public float[] getAvgVoice() {
		return avgVoice;
	}

	public void setAvgVoice(float[] avgVoice) {
		this.avgVoice = avgVoice;
	}
	

	private float getY() {
		return y;
	}

	public boolean isStressed() {
		return y >= 0;
	}

	/*
	 * ***********************FOR DataAnalyzer***********************
	 */
	
	public void setAccel(String xAvg, String yAvg) 
			throws NumberFormatException {
		float[] avgs = new float[2];
		avgs[0] = Float.parseFloat(xAvg);
		avgs[1] = Float.parseFloat(yAvg);
		setAvgCountTurns(avgs);
		
	}

	public void setHydro(String next) throws NumberFormatException {
		setAvgHydro(Float.parseFloat(next));
		
	}

	public void setVoice(String fracShout, String avgAmp) 
			throws NumberFormatException {
		float[] data = new float[2];
		data[0] = Float.parseFloat(fracShout);
		data[1] = Float.parseFloat(avgAmp);
		setAvgVoice(data);
		
	}

	public void setY(String next) {
		this.y = Float.parseFloat(next);
		
	}

	public void writeToFile(String f) throws IOException, NullPointerException {
		StringBuilder ans = new StringBuilder();
		ans.append(this.getAvgCountTurns()[0]).append(",");
		ans.append(this.getAvgCountTurns()[1]).append(",");
		ans.append(this.getAvgHydro()).append(",");
		ans.append(this.getAvgVoice()[0]).append(",");
		ans.append(this.getAvgVoice()[1]).append(",");
		ans.append(this.getY());
		
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(f, true)));
	    out.println(ans.toString());
	    out.close();
		
	}
	
	/*
	 * THRESHOLD STRESS
	 */
	public boolean isThresholdStressed() throws NoResultsException {
		boolean isStressedAccel = isStressedAccel();
		boolean isStressedHydro = isStressedHydro();
		boolean[] isStressedVoice = isStressedVoice();
		
		boolean test1 = isStressedAccel && (isStressedHydro 
				|| isStressedVoice[1]);
		boolean test2 = isStressedVoice[0] && (isStressedHydro 
				|| isStressedAccel);
		boolean test3 = isStressedVoice[1];
		boolean test4 = isStressedHydro && isStressedVoice[0] && isStressedAccel;
		
		return (test1 || test2 || test3 || test4);
	}

	
	private boolean isStressedAccel() {
		return avgCountTurns[0] > 2 && avgCountTurns[1] > 2;
	}

	private boolean isStressedHydro() {
		return avgHydro > 60;
	}

	private boolean[] isStressedVoice() {
		boolean[] ans = new boolean[2];
		ans[0] = avgVoice[0] > 0.75 && avgVoice[1] >= 75;
		ans[1] = avgVoice[0] > 0.75 && avgVoice[1] >= 120;
		return ans;
	}

}

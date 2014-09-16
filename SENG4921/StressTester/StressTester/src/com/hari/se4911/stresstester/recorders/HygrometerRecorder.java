package com.hari.se4911.stresstester.recorders;

import java.util.ArrayList;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

public class HygrometerRecorder implements SensorEventListener {
	  ArrayList<String> inBox = new ArrayList<>();
	  float[] history = new float[3];
	  
	  private boolean holdingHydro = false;
	  private boolean prevHydro = false;
	  private boolean isSweaty;
	  private ArrayList<float[]> results;
	  private String output = "";
	

	  public HygrometerRecorder() {
		results = new ArrayList<>();
	    Log.v("Hygrometer", "START");
	    
	  }

	  @Override
	  public final void onAccuracyChanged(Sensor sensor, int accuracy) {
	    // Do something here if sensor accuracy changes.
	  }

	  @Override
	  public final void onSensorChanged(SensorEvent event) {
		  String toStore = "";
		  toStore += event.values[0];
		  toStore += "\t";
		  toStore += event.values[1];
		  toStore += "\t";
		  toStore += event.values[2];
		  inBox.add(toStore);
		  for (int i = 0; i < inBox.size(); i++) {
			  output += inBox.get(i);
			  output += "\n";
			  if (i == inBox.size()-1 && (prevHydro != holdingHydro)) {
				  output += holdingHydro + "\n";
				  prevHydro = holdingHydro;
			  }
		  }
		  output += "\n";
		  output += "Changes in values:\n";
		  output += (event.values[0] - history[0]) + ",\n" + (event.values[1] - history[1]) + ",\n" + (event.values[2] - history[2]);
		  
		  for(int i = 0; i < 3; i++) {
			  history[i] = event.values[i];
		  }
		  
		  results.add(history);
		  
	  }
	  

	  protected void toggleHydro() {
		//change whether hand is moist or not
		isSweaty = !isSweaty;
	  }

	public ArrayList<float[]> getResults() {
		Log.v("Hygrometer",output);
		return results;
	}
}

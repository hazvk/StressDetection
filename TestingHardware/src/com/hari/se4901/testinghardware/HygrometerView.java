package com.hari.se4901.testinghardware;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

public class HygrometerView extends Activity implements SensorEventListener {
	  private SensorManager mSensorManager;
	  private Sensor mHumid;
	  TextView textView;
	  String[] inBox = new String[10];
	  float[] history = new float[3];
	  int totalIndex = 0;
	  private boolean holdingHydro = false;
	  private boolean prevHydro = false;
	

	  @Override
	  public final void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
	      textView = new TextView(this);
	      setContentView(R.layout.h_view);
	      final ScrollView sv = (ScrollView) findViewById(R.id.scrollView3);
	      sv.addView(textView);
	      //setContentView(textView);

	    // Get an instance of the sensor service, and use that to get an instance of
	    // a particular sensor.
	    mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    mHumid = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
	    mSensorManager.registerListener(this, mHumid, SensorManager.SENSOR_DELAY_NORMAL);
	    Log.v("Hygrometer", "START");
	    
	  }

	  @Override
	  public final void onAccuracyChanged(Sensor sensor, int accuracy) {
	    // Do something here if sensor accuracy changes.
	  }

	  @Override
	  public final void onSensorChanged(SensorEvent event) {
		  int index = totalIndex % 10;
		  String toStore = "";
		  toStore += event.values[0];
		  toStore += "\t";
		  toStore += event.values[1];
		  toStore += "\t";
		  toStore += event.values[2];
		  inBox[index] = toStore;
		  String output = "";
		  for (int i = 0; i < inBox.length; i++) {
			  output += inBox[i];
			  output += "\n";
			  if (i == index && (prevHydro != holdingHydro)) {
				  output += holdingHydro + "\n";
				  prevHydro = holdingHydro;
			  }
		  }
		  output += "\n";
		  output += "SENSOR_DELAY_NORMAL = " + SensorManager.SENSOR_DELAY_NORMAL + "\n";
		  output += "Changes in values:\n";
		  output += (event.values[0] - history[0]) + ",\n" + (event.values[1] - history[1]) + ",\n" + (event.values[2] - history[2]);
		  
		  for(int i = 0; i < 3; i++) {
			  history[i] = event.values[i];
		  }
		  
		  textView.setText(output);
		  Log.v("Hygrometer",output);
		  totalIndex++;
		
	  }

	  @Override
	  protected void onResume() {
	    // Register a listener for the sensor.
	    super.onResume();
	    mSensorManager.registerListener(this, mHumid, SensorManager.SENSOR_DELAY_NORMAL);
	  }

	  @Override
	  protected void onPause() {
	    // Be sure to unregister the sensor when the activity pauses.
	    super.onPause();
	    mSensorManager.unregisterListener(this);
	  }
	  
	  protected void toggleHydro() {
		  holdingHydro = !holdingHydro;
	  }
}

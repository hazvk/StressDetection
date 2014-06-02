package com.hari.se4901.testinghardware;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class HygrometerView extends Activity implements SensorEventListener {
	  private SensorManager mSensorManager;
	  private Sensor mHumid;
	  TextView textView;
	  String inBox = "";

	  @Override
	  public final void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
	      textView = new TextView(this);
	      setContentView(textView);

	    // Get an instance of the sensor service, and use that to get an instance of
	    // a particular sensor.
	    mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    mHumid = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
	    mSensorManager.registerListener(this, mHumid, SensorManager.SENSOR_DELAY_GAME);
	    
	  }

	  @Override
	  public final void onAccuracyChanged(Sensor sensor, int accuracy) {
	    // Do something here if sensor accuracy changes.
	  }

	  @Override
	  public final void onSensorChanged(SensorEvent event) {
	    float humidityReading = event.values[0];
	    inBox += " ";
	    inBox += humidityReading;
	    inBox += " ";
	    inBox += event.values[1];
	    inBox += event.values[2];
	    textView.setText(inBox);
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
}

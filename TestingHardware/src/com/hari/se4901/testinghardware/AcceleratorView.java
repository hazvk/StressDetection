package com.hari.se4901.testinghardware;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class AcceleratorView extends Activity implements SensorEventListener {
    TextView textView;
    StringBuilder builder = new StringBuilder();

    float [] history = new float[2];
    String [] direction = {"0","0"};
    
    ArrayList<Float> maxChanges = new ArrayList<Float>();
    
    int paceCount = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textView = new TextView(this);
        setContentView(textView);

        SensorManager manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
        manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        maxChanges.add((float) 0);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float xChange = history[0] - event.values[0];
        float yChange = history[1] - event.values[1];

        history[0] = event.values[0];
        history[1] = event.values[1];

        if (Math.abs(xChange) > 0.5){
          direction[0] = event.values[0] + ", " + xChange;
          if (Math.abs(xChange) > 
          	Math.abs(maxChanges.get(maxChanges.size()-1))) {
        	  maxChanges.add(xChange);
          }
          if (Math.abs(xChange) > 2) {
        	  paceCount++;
          }
        }

        if (Math.abs(yChange) > 0.5){
          direction[1] = event.values[1] + ", " + yChange;
        }

        builder.setLength(0);
        builder.append("Moderated change:\n");
        builder.append("x: ");
        builder.append(direction[0]);
        builder.append("\ny: ");
        builder.append(direction[1]);
        builder.append("\n");
        builder.append("\nChange:\n");
        builder.append("x: ");
        builder.append(xChange);
        builder.append("\ny: ");
        builder.append(yChange);
        builder.append("\n");
        builder.append("\nActual Values:\n");
        builder.append("x: ");
        builder.append(event.values[0]);
        builder.append("\ny: ");
        builder.append(event.values[1]);
        builder.append("\n");
        builder.append("\nMax Change\n");
        for (Float i : maxChanges) {
        	builder.append(i + " ");
        }
        builder.append("\n");
        builder.append("\nCount: " + paceCount);
        
        
        textView.setText(builder.toString());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // nothing to do here
    }
}
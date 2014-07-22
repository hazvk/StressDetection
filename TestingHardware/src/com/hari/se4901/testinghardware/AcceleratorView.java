package com.hari.se4901.testinghardware;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

public class AcceleratorView extends Activity implements SensorEventListener {
    TextView textView;
    StringBuilder builder = new StringBuilder();

    float [] history = new float[3];
    String [] direction = {"0","0","0"};
    
    Map<String, ArrayList<Float>> maxChanges = new HashMap<String, ArrayList<Float>>();
    
    int[] paceCount = {0,0,0};

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textView = new TextView(this);
        setContentView(R.layout.accelerometer_view);
        final ScrollView sv = (ScrollView) findViewById(R.id.scrollView2);
        
        sv.addView(textView);
        
        maxChanges.put("x", new ArrayList<Float>());
        maxChanges.put("y", new ArrayList<Float>());
        maxChanges.put("z", new ArrayList<Float>());
        
        SensorManager manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
        manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        maxChanges.get("x").add((float) 0);
        maxChanges.get("y").add((float) 0);
        maxChanges.get("z").add((float) 0);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float xChange = history[0] - event.values[0];
        float yChange = history[1] - event.values[1];
        float zChange = history[2] - event.values[2];

        history[0] = event.values[0];
        history[1] = event.values[1];
        history[2] = event.values[2];

        if (Math.abs(xChange) > 0.5){
			direction[0] = event.values[0] + ", " + xChange;
			if (Math.abs(xChange) > 
				Math.abs(maxChanges.get("x").get(maxChanges.get("x").size()-1))) {
				maxChanges.get("x").add(xChange);
			}
			if (Math.abs(xChange) > 2) {
				paceCount[0]++;
			}
        }

        if (Math.abs(yChange) > 0.5){
            direction[1] = event.values[1] + ", " + yChange;
            if (Math.abs(yChange) > 
            Math.abs(maxChanges.get("y").get(maxChanges.get("y").size()-1))) {
          	  maxChanges.get("y").add(xChange);
            }
            if (Math.abs(yChange) > 2) {
          	  paceCount[1]++;
            }
        }
        
        if (Math.abs(zChange) > 0.5){
            direction[2] = event.values[2] + ", " + yChange;
            if (Math.abs(zChange) > 
            Math.abs(maxChanges.get("z").get(maxChanges.get("z").size()-1))) {
          	  maxChanges.get("z").add(xChange);
            }
            if (Math.abs(zChange) > 2) {
          	  paceCount[2]++;
            }
        }

        builder.setLength(0);
        builder.append("Moderated change:\n");
        builder.append("x: ");
        builder.append(direction[0]);
        builder.append("\ny: ");
        builder.append(direction[1]);
        builder.append("\nz: ");
        builder.append(direction[2]);
        builder.append("\n");
        builder.append("\nChange:\n");
        builder.append("x: ");
        builder.append(xChange);
        builder.append("\ny: ");
        builder.append(yChange);
        builder.append("\nz: ");
        builder.append(zChange);
        builder.append("\n");
        builder.append("\nActual Values:\n");
        builder.append("x: ");
        builder.append(event.values[0]);
        builder.append("\ny: ");
        builder.append(event.values[1]);
        builder.append("\nz: ");
        builder.append(event.values[2]);
        builder.append("\n");
        builder.append("\nMax Change\n");
        ArrayList<String> dirs = new ArrayList<String>();
        for (String d : maxChanges.keySet()) {
        	dirs.add(d);
        }
        Collections.sort(dirs);
        for (String d : maxChanges.keySet()) {
        	builder.append(d + ": ");
	        for (Float i : maxChanges.get(d)) {
	        	BigDecimal bd = new BigDecimal(Float.toString(i));
	            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);  
	        	builder.append(bd + " ");
	        }
	        builder.append("\n\n");
        }
        builder.append("\n");
        builder.append("\nCount:\nx: " + paceCount[0]);
        builder.append("\ny: " + paceCount[1]);
        builder.append("\nz: " + paceCount[2]);
        
        
        textView.setText(builder.toString());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // nothing to do here
    }
}
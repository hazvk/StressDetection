package com.hari.se4911.stresstester.recorders.sensors;

import com.hari.se4911.stresstester.recorders.AcceleratorRecorder;
import com.hari.se4911.stresstester.recorders.HygrometerRecorder;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class StressSensorEventListener implements SensorEventListener {
	
	private AcceleratorRecorder aa;
	private HygrometerRecorder ha;

	public StressSensorEventListener(AcceleratorRecorder aa, HygrometerRecorder ha) {
		this.aa = aa;
		this.ha = ha;
	}
	
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            aa.onAccuracyChanged(sensor, accuracy);
        }else if (sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
        	ha.onAccuracyChanged(sensor, accuracy);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
        	aa.onSensorChanged(event);
        }else if (sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
        	ha.onSensorChanged(event);
        }
    }
}

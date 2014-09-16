package com.hari.se4911.stresstester;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.hari.se4911.stresstester.recorders.AcceleratorRecorder;
import com.hari.se4911.stresstester.recorders.HygrometerRecorder;
import com.hari.se4911.stresstester.recorders.VoiceRecorder;
import com.hari.se4911.stresstester.recorders.sensors.StressSensorEventListener;
import com.hari.se4911.stresstester.results.DataParser;
import com.hari.se4911.stresstester.results.StressResults;

public class MainActivity extends ActionBarActivity {

	private SensorManager mSensorManager;
	private SensorEventListener mSensorListener;
	AcceleratorRecorder aa;
	HygrometerRecorder ha;
	VoiceRecorder va;
	
	StressResults dataRes;
	StressResults currRes;
	
	private long ONE_MINUTE = 60*1000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initialize();
	}

	private void initialize() {
		loadData(null);
		//TODO: put something instead of null
		
	}

	private void initSensors() {
		mSensorManager = (SensorManager) this
		                .getSystemService(Context.SENSOR_SERVICE);
		mSensorListener = new StressSensorEventListener(aa, ha);

		mSensorManager.registerListener(mSensorListener, 
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(mSensorListener, 
				mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY), SensorManager.SENSOR_DELAY_NORMAL);
		
		va = new VoiceRecorder();
	}

	private void loadData(String data) {
		DataParser dp = new DataParser(data);
		dp.parse();
		dataRes = dp.getResults();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public boolean testStress() {
		initSensors();
		Timer tim = new Timer();
		TimerTask tm = new TimerTask() {
			
			@Override
			public void run() {
				stopSensors();
				returnResults();
				analyze();
			}
		};
		
		tim.schedule(tm, 0, ONE_MINUTE);
		
		TextView res = (TextView) findViewById(R.id.results);
		res.setText("done");
		return false;
		
	}
	
	protected void stopSensors() {
		mSensorManager.unregisterListener(mSensorListener);
		va.stopRecord();
	}
	
	protected void returnResults() {
		//TODO: get results from each object
		currRes = new StressResults(aa.getResults(), ha.getResults(), va.getResults());
		
	}

	private void analyze() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onResume() {
	    // Register a listener for the sensor.
		super.onResume();
	    mSensorManager.registerListener(mSensorListener, 
	    		mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	    mSensorManager.registerListener(mSensorListener, 
	    		mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY), SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	@Override
	protected void onPause() {
		// Be sure to unregister the sensor when the activity pauses.
		super.onPause();
		mSensorManager.unregisterListener(mSensorListener);
	}
	  
}

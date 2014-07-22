package com.hari.se4901.testinghardware;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity_Voice extends Activity {

	TextView tv;
	ArrayList<Short> storedVals = new ArrayList<Short>();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_voice);
        tv = (TextView) findViewById(R.id.to_print);
        tv.setText(" ");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void outputVoice(View v) {
        String toPrint = "Voice recording\n";
        String toPrintSecond = "";
        int countZero = 0;

        int minSize = AudioRecord.getMinBufferSize(12000,AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        final AudioRecord ar = new AudioRecord(MediaRecorder.AudioSource.MIC, 12000,AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT,minSize);


        short[] buffer = new short[minSize];

        Timer tim = new Timer();
        TimerTask timT = new TimerTask() {
			
			@Override
			public void run() {
				ar.stop();
				
			}
		};
        
        tim.schedule(timT, 1500);
        ar.startRecording();
        ar.read(buffer, 0, minSize);
        for (short s : buffer) {
            short blow_value=(short) Math.abs(s);
            toPrint += (blow_value + "  ");
            toPrintSecond += s + "  ";
            storedVals.add(s);
        }
        
        //tv = (TextView) findViewById(R.id.to_print);
        
        for (Short sh : storedVals) {
        	if (sh == 0) {
        		countZero++;
        	}
        }
        
        toPrint = "Number of 0's = " + countZero + "\n\n" + toPrint; 
        tv.setText(toPrint + "\n\n" + toPrintSecond);
        return;

	}
	public void outputHum(View v) {
		Intent intent = new Intent(this, HygrometerView.class);
		startActivity(intent);
	    	
    }

	public void outputAccel(View v) {
		Intent intent = new Intent(this, AcceleratorView.class);
		startActivity(intent);
		
	}
}

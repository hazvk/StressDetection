package com.hari.se4901.testinghardware;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity_Voice extends Activity {

	TextView tv;
	ArrayList<Short> storedVals = new ArrayList<Short>();
	private int SAMPLE_RATE = 12000;
	private int SAMPLE_DELAY = 75*5; 
	private int minSize;
	AudioRecord ar;
	Thread th;
	private boolean isUnderlined = false;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_voice);
        tv = (TextView) findViewById(R.id.to_print);
        tv.setText(" ");
        
        minSize = AudioRecord.getMinBufferSize(SAMPLE_RATE,
        		AudioFormat.CHANNEL_IN_MONO, 
        		AudioFormat.ENCODING_PCM_16BIT);
    	ar = new AudioRecord(MediaRecorder.AudioSource.MIC, 
    			SAMPLE_RATE,
    			AudioFormat.CHANNEL_IN_MONO, 
    			AudioFormat.ENCODING_PCM_16BIT,minSize);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it
    	//is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void outputVoice(View v) {

        OnClickListener l = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				stopOutputVoice(v);
				
			}
		};
		
        Button btn = (Button) findViewById(R.id.button1);
        btn.setOnClickListener(l);


        final short[] buffer = new short[minSize];
        ar.startRecording();

		th = new Thread(new Runnable() {
			
			@Override
			public void run() {
				String toPrint = "Voice recording<br/>";
		    	
				while (th != null && !th.isInterrupted()) {
					try {
						Thread.sleep(SAMPLE_DELAY);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					int totalRead = ar.read(buffer, 0, minSize);
					int sum = 0;
					for (int i = 0; i < totalRead; i++) {
						sum += buffer[i];
					}
					int amplitudeReading = sum/totalRead; 
					
					if (Math.abs(amplitudeReading) >= 100 && !isUnderlined) {
						toPrint += "<u>";
						isUnderlined = true;
					}
					
					if (Math.abs(amplitudeReading) < 100 && isUnderlined) {
						toPrint += "</u>";
						isUnderlined = false;
					}
					
					toPrint += (sum/totalRead) + " ";
					
					final String willBePrinted = new String(toPrint);
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							tv.setText(Html.fromHtml(willBePrinted));
						}
					});
					
				}
			}
		});
		
		th.start();

	}

	public void stopOutputVoice(View v) {
    	th.interrupt();
    	th = null;
    	ar.stop();
    	OnClickListener l = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				outputVoice(v);
				
			}
		};
		
        Button btn = (Button) findViewById(R.id.button1);
        btn.setOnClickListener(l);
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

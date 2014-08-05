package com.hari.se4901.testinghardware;

import java.util.ArrayList;
<<<<<<< HEAD

import android.app.Activity;
import android.content.Intent;
=======
import java.util.Timer;
import java.util.TimerTask;

>>>>>>> c4607c4a51aa10f0332ecf4825eda3cec1c67b23
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
<<<<<<< HEAD
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
=======
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
>>>>>>> c4607c4a51aa10f0332ecf4825eda3cec1c67b23
import android.widget.TextView;

public class MainActivity_Voice extends Activity {

	TextView tv;
	ArrayList<Short> storedVals = new ArrayList<Short>();
<<<<<<< HEAD
	private int SAMPLE_RATE = 12000;
	private int SAMPLE_DELAY = 75*5; 
	private int minSize;
	AudioRecord ar;
	Thread th;
	private boolean isUnderlined = false;
=======
>>>>>>> c4607c4a51aa10f0332ecf4825eda3cec1c67b23
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_voice);
        tv = (TextView) findViewById(R.id.to_print);
        tv.setText(" ");
<<<<<<< HEAD
        
        minSize = AudioRecord.getMinBufferSize(SAMPLE_RATE,
        		AudioFormat.CHANNEL_IN_MONO, 
        		AudioFormat.ENCODING_PCM_16BIT);
    	ar = new AudioRecord(MediaRecorder.AudioSource.MIC, 
    			SAMPLE_RATE,
    			AudioFormat.CHANNEL_IN_MONO, 
    			AudioFormat.ENCODING_PCM_16BIT,minSize);
=======
>>>>>>> c4607c4a51aa10f0332ecf4825eda3cec1c67b23
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
<<<<<<< HEAD
        // Inflate the menu; this adds items to the action bar if it
    	//is present.
=======
        // Inflate the menu; this adds items to the action bar if it is present.
>>>>>>> c4607c4a51aa10f0332ecf4825eda3cec1c67b23
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void outputVoice(View v) {
<<<<<<< HEAD

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
    
=======
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
>>>>>>> c4607c4a51aa10f0332ecf4825eda3cec1c67b23
	public void outputHum(View v) {
		Intent intent = new Intent(this, HygrometerView.class);
		startActivity(intent);
	    	
    }

	public void outputAccel(View v) {
		Intent intent = new Intent(this, AcceleratorView.class);
		startActivity(intent);
		
	}
}

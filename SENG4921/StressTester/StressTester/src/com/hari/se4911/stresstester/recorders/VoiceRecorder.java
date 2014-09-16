package com.hari.se4911.stresstester.recorders;

import java.util.ArrayList;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

public class VoiceRecorder extends Activity {

	ArrayList<Short> storedVals;
	private int SAMPLE_RATE = 12000;
	private int SAMPLE_DELAY = 75*5; 
	private int minSize;
	AudioRecord ar;
	Thread th;
	//private boolean isLoud = false;
	
    public VoiceRecorder() {
        
        minSize = AudioRecord.getMinBufferSize(SAMPLE_RATE,
        		AudioFormat.CHANNEL_IN_MONO, 
        		AudioFormat.ENCODING_PCM_16BIT);
    	ar = new AudioRecord(MediaRecorder.AudioSource.MIC, 
    			SAMPLE_RATE,
    			AudioFormat.CHANNEL_IN_MONO, 
    			AudioFormat.ENCODING_PCM_16BIT,minSize);
    	storedVals = new ArrayList<Short>();
    	
    }
    
    public void startRecord() {

        Log.v("VoiceRecording", "START");
        ar.startRecording();
        
        Runnable r = new Runnable() {
			
			@Override
			public void run() {
		    	
				while (th != null && !th.isInterrupted()) {
					try {
						Thread.sleep(SAMPLE_DELAY);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					short[] buffer = new short[minSize * 2];
					
					int totalRead = ar.read(buffer, 0, minSize);
					//Log.d("TOTAL_READ", buffer.length + " " + minSize + " " + totalRead);
					int sum = 0;
					for (int i = 0; i < totalRead; i++) {
						sum += buffer[i];
					}
					
					int amplitudeReading;
					try {
						amplitudeReading = sum/totalRead;
					} catch (ArithmeticException ae) {
						ae.printStackTrace();
						amplitudeReading = 0;
					}
					 
					
					/*
					 * analyser logic
					 * 
					 * 	if (Math.abs(amplitudeReading) >= 100 && !isLoud) {
					 * 		isLoud = true;
					 *	}
					 *	
					 *	if (Math.abs(amplitudeReading) < 100 && isLoud) {
					 *		isLoud = false;
					 *	}
					 *
					 */
						
					
					Log.v("VoiceRecording", "Voice Recording is: " + String.valueOf(amplitudeReading));
					
				}
			}
		};

		th = new Thread(r);
		th.start();

	}

	public void stopRecord() {
    	th.interrupt();
    	th = null;
    	ar.stop();
    	Log.v("VoiceRecording", "STOP");
    	Log.v("VoiceRecording", this.printResults());
    }

	private String printResults() {
		String ans = "";
		for (Short sh: storedVals) {
			ans += sh + " ";
		}
		return ans;
	}

	public ArrayList<Short> getResults() {
		return storedVals;
	}
    
}

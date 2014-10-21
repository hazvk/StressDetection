package com.hari.se4911.stresstester.recorders;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

public class VoiceRecorder extends Activity {

	ArrayList<Float> storedVals;
	private int SAMPLE_RATE = 12000;
	private int SAMPLE_DELAY = 75*5; 
	private int minSize;
	private AudioRecord ar;
	private Thread th;
	//private boolean isLoud = false;
	
    public VoiceRecorder() {
        
        minSize = AudioRecord.getMinBufferSize(SAMPLE_RATE,
        		AudioFormat.CHANNEL_IN_MONO, 
        		AudioFormat.ENCODING_PCM_16BIT);
    	ar = new AudioRecord(MediaRecorder.AudioSource.MIC, 
    			SAMPLE_RATE,
    			AudioFormat.CHANNEL_IN_MONO, 
    			AudioFormat.ENCODING_PCM_16BIT,minSize);
    	storedVals = new ArrayList<Float>();
    	
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
					
					float amplitudeReading;
					try {
						amplitudeReading = sum/totalRead;
						Log.v("VoiceRecording", "Voice Recording is: " +
								String.valueOf(amplitudeReading));
						storedVals.add(amplitudeReading);
					} catch (ArithmeticException ae) {
						Log.v("VoiceRecording", "Recording stopped.");
						ae.printStackTrace();
					}
					
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

	public String printResults() {
		String ans = "";
		Iterator<Float> it = storedVals.iterator();
		while (it.hasNext()) {
			Float f = it.next(); 
			ans += f + " ";
		}
		return ans;
	}

	public ArrayList<Float> getResults() {
		return storedVals;
	}
	
	public boolean isRecording() {
		if (ar.getRecordingState() == AudioRecord.RECORDSTATE_STOPPED)
			return false;
		else return true;
	}

}

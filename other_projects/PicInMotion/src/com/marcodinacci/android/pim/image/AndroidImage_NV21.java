package com.marcodinacci.android.pim.image;

import android.util.Log;

/**
 * @author Marco Dinacci <marco.dinacci@gmail.com>
 */
public class AndroidImage_NV21 extends AbstractAndroidImage {

	private static final String TAG = "AndroidImage_NV21";

	public AndroidImage_NV21(byte[] data, Size<Integer, Integer> size) {
		super(data, size);
	}

	@Override
	public boolean isDifferent(AndroidImage other, int pixel_threshold, 
			int threshold) {
		
		if(!assertImage(other)) {
			return false;
		}
		
		byte[] otherData = other.get();
		int totDifferentPixels = 0;
		
		// For the sake of this demo just use a 640x480 image.
		int width = 480;
		int height = 640;
		int size = height*width; // 640x480
		for (int i = 0, ij=0; i < height; i++) {
			for (int j = 0; j < width; j++,ij++) {
				int pix = (0xff & ((int) mData[ij])) - 16;
				int otherPix = (0xff & ((int) otherData[ij])) - 16;
				
				/*int r = (((int) mData[ij]) >> 16) & 0xff;
				int g = (((int) mData[ij]) >> 8) & 0xff;
				int b = (((int) mData[ij])) & 0xff;*/
				
				// Reconstruct 16 bit rgb565 value from two bytes
				if (i == height/2) {
					
				  int rgb565 = (mData[ij] & 255) | ((mData[ij + 1] & 255) << 8);
				
				  // Extract raw component values (range 0..31 for g and b, 0..63 for g)  
				  int b5 = rgb565 & 0x1f;
				  int g6 = (rgb565 >> 5) & 0x3f;
				  int r5 = (rgb565 >> 11) & 0x1f;
				
				  // Scale components up to 8 bit: 
				  // Shift left and fill empty bits at the end with the highest bits,
				  // so 00000 is extended to 000000000 but 11111 is extended to 11111111
				  int b = (b5 << 3) | (b5 >> 2);
				  int g = (g6 << 2) | (g6 >> 4);
				  int r = (r5 << 3) | (r5 >> 2); 
				
				  // The rgb888 value, store in an array or buffer...
				  //int rgb = (r << 16) | (g << 8) | b;
				
				  Log.d(TAG, "colours are " + r + " " + g + " " + b);
				}
				
				if (pix < 0) pix = 0;
				if (pix > 255) pix = 255;
				if (otherPix < 0) otherPix = 0;
				if (otherPix > 255) otherPix = 255;

				if (i == height/2 && (j > width/2 -3 && j < width/2 + 2)) {
					Log.d(TAG, "pix vs otherPix: " + pix + " " + otherPix);
				}
				if(Math.abs(pix - otherPix) >= 1) {
					totDifferentPixels++;
				}
			}
		}
		
		Log.v(TAG, "end colours picture with totDifferentPixels = " + totDifferentPixels);
		
		if(totDifferentPixels == 0) totDifferentPixels = 1;
		Log.d(TAG, "Number of different pixels: " + totDifferentPixels + " -> " 
				+ (100 / ( size / totDifferentPixels) ) + "%");
		
		return totDifferentPixels > threshold;
	}
	
	@Override
	public AndroidImage toGrayscale() {
		// TODO to implement.
		return this;
	}
}

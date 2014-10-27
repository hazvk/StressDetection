package com.hari.se4911.stresstester.results;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataParser {
	
	private File dataFile;
	private List<StressResult> results;
	
	public DataParser(File f) {
		this.dataFile = f;
		results = new ArrayList<StressResult>();
	}

	public List<StressResult> getResults() {
		return results;
	}

	public void parse() throws FileNotFoundException {
		//Get scanner instance
        Scanner lineScanner = new Scanner(dataFile);
        
        //Get all tokens and store them in some data structure
        while (lineScanner.hasNextLine()) 
        {
        	String line = lineScanner.nextLine();
        	Scanner scanner = new Scanner(line);
        	scanner.useDelimiter(",");
        	
        	StressResult sr = new StressResult();
        	
        	try {
        		String xAvg = "";
            	String yAvg = ""; 
            	if(scanner.hasNext())
            		xAvg = scanner.next();
            	if(scanner.hasNext())
            		yAvg = scanner.next();
            	sr.setAccel(xAvg, yAvg);
            	if(scanner.hasNext())
            		sr.setHydro(scanner.next());
            	String fracShout = "";
            	String avgAmp = "";
            	if(scanner.hasNext())
            		fracShout = scanner.next();
            	if(scanner.hasNext())
            		avgAmp = scanner.next();
            	sr.setVoice(fracShout, avgAmp);
        		if(scanner.hasNext())
            		sr.setY(scanner.next());
        		if(scanner.hasNext())
            		sr.setCalculatedY(scanner.next());

        		results.add(sr);
        		scanner.close();
        	} catch (NumberFormatException ne) {
        		ne.printStackTrace();
        		continue;
        	}
        	
        }
        lineScanner.close();

	}

}

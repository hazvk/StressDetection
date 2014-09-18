package com.hari.se4911.stresstester.results;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class DataParser {
	
	private String dataFile;
	private List<StressResult> results;

	public DataParser(String dataFile) {
		this.dataFile = dataFile;
	}

	public List<StressResult> getResults() {
		return results;
	}

	public void parse() throws FileNotFoundException, NumberFormatException {
		//Get scanner instance
		File f = new File(dataFile);
        Scanner lineScanner = new Scanner(f);
        
        //Get all tokens and store them in some data structure
        while (lineScanner.hasNextLine()) 
        {
        	String line = lineScanner.nextLine();
        	Scanner scanner = new Scanner(line);
        	scanner.useDelimiter(",");
        	
        	StressResult sr = new StressResult();
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
        		sr.setStressed(Boolean.parseBoolean(scanner.next()));

    		results.add(sr);
    		scanner.close();
        }
        lineScanner.close();

	}

}

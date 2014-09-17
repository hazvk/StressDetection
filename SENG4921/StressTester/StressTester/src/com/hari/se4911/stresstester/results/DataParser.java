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

	public void parse() throws FileNotFoundException {
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
        	if(scanner.hasNext())
        		sr.setAccel(scanner.next());
        	if(scanner.hasNext())
        		sr.setHydro(scanner.next());
        	if(scanner.hasNext())
        		sr.setVoice(scanner.next());
    		if(scanner.hasNext())
        		sr.setStressed(Boolean.parseBoolean(scanner.next()));

    		results.add(sr);
    		scanner.close();
        }
        lineScanner.close();

	}

}

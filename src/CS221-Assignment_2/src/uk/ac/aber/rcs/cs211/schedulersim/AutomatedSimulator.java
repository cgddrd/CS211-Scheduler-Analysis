package uk.ac.aber.rcs.cs211.schedulersim;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import uk.ac.aber.rcs.cs211.schedulersim.scheduler.*;

/**
 * Provides an automated interface to the existing @link{uk.ac.aber.rcs.cs211.schedulersim.SimulatorApplication}
 * to enable large numbers of simulation tests to be performed quickly, without the need for human interaction. 
 * 
 * Interacts directly with the @link{uk.ac.aber.rcs.cs211.schedulersim.Simulator} class to allow the GUI to be bypassed, 
 * enabling the core simulator to be controlled without the need for a user. 
 * 
 * @author Connor Goddard (clg11)
 * @see uk.ac.aber.rcs.cs211.schedulersim.AbstractScheduler
 */
public class AutomatedSimulator {

	/** Collection of test process jobs loaded in from external data files. */
	private ArrayList<Job> jobQueue;
	
	/** Array of completed jobs returned by the simulator upon completion. */
	private Job[] finishedList;
	
	/** Collection of results data obtained throuh the automated tests. */
	private ArrayList<ResultValue> results = new ArrayList<ResultValue>();

	private Simulator testSimulator;
	
	public AutomatedSimulator() {

			try {
				
				//Initiate and run the automated tests. 
				runTest(5000, new LotterySchedulingWeighted(), "/home/connor/workspace/CS221-Assignment_2/src/Test_ManyCPUBlocks.jobs");
				
				runTest(5000, new LotteryScheduling(), "/home/connor/workspace/CS221-Assignment_2/src/Test_ManyCPUBlocks.jobs");
				
				runTest(5000, new ShortestTimeRemaining(), "/home/connor/workspace/CS221-Assignment_2/src/Test_ManyCPUBlocks.jobs");
				
				runTest(5000, new FirstComeFirstServed(), "/home/connor/workspace/CS221-Assignment_2/src/Test_ManyCPUBlocks.jobs");
			
			} catch (IOException e) {
				
				System.out.println("ERROR: Test cannot complete.");
				e.printStackTrace();
			}

	}

	/**
	 * Bootstrap method used to instantiate a new simulator without the need for the 
	 * GUI provided by the default.
	 * @param args Arguments specified upon initiation of the Java application.
	 */
	public static void main(String[] args) {

		new AutomatedSimulator();

	}

	/**
	 * Performs an automated simulation for a specified job queue, using a specified 
	 * scheduling algorithm for a specific number of times, collecting test results as 
	 * it goes. 
	 * 
	 * Once all the tests have been completed, all the obtained test results are exported to file for 
	 * further analysis. 
	 * @param noOfTests The number of times a specific simulation should be run as part of the automated test.
	 * @param newScheduler The scheduling algorithm that is to be used in the simulation.
	 * @param fileName The path of the data file containing the job queue to be used in the simulation.
	 * @throws FileNotFoundException If the specified data file cannot be found.
	 * @throws IOException If the test results cannot be exported. 
	 */
	public void runTest(int noOfTests, Scheduler newScheduler, String fileName) throws FileNotFoundException,IOException {

		//Assign the selected scheduler name and current date to the name of the exported test resukts file.
		String csvName = newScheduler.getClass().getSimpleName() + " - " + new Date(System.currentTimeMillis()).toString() + ".csv";
		
		//Repeat the simulation for a specified number of runs
		for (int i = 0; i < noOfTests; i++) {
			
			//Load in the job queue from the specified data file.
			jobQueue = SimulatorLoader.Load(fileName);

			//Instantiate a new simulator using the specified scheduler and job queue.
			testSimulator = new Simulator(newScheduler, jobQueue);
			
			//Run the simulator until it has completed.
			while (!testSimulator.hasfinished()) {
				
				testSimulator.singleStep();

			}

			//Once completed, obtain the colleciton of completed jobs. 
			finishedList = testSimulator.getFinishedQueue();

			//Record the test results of the simulation.
			tallyAllResults(finishedList);

		}
		
		//displayResults();
		
		/*
		 * Once all simulation runs have been completed,
		 * export all of the test results obtain to an external '.csv' file.
		 */
		exportCSV(csvName);
		
		//Reset the collection of test results ready for the next automated test. 
		resetTests();
	}
	
	/**
	 * Coordinates the recording of the various test result data required from the completed
	 * simulation run. 
	 * 
	 * @param jobs The collection of finished jobs returned from the completed simulation run.
	 */
	public void tallyAllResults(Job[] jobs) {
		
		int total = 0;
		int mean = 0;
		
		/*
		 * Calculate the mean total elasped time for all jobs in
		 * the finished queue.
		 */
		for (int i = 0; i < jobs.length; i++) {
			total += jobs[i].getElapsedDuration();
		}	
		
		mean = total / jobs.length;
		
		//Tally the results for the mean elasped time to finish a job.
		tallyResultData(mean, ResultType.MEAN_ELAPSED_TIME);
		
		//Tally the results for the total number of cpu cycles.
		tallyResultData(testSimulator.getCpuTicks(), ResultType.CPU_TIME);
		
		//Tally the results for the total number of context switches.
		tallyResultData(testSimulator.getContextSwitchCount(), ResultType.CONTEXT_SWITCHES);
		
		//Tally the results for the total idle count.
		tallyResultData(testSimulator.getIdleTimeCount(), ResultType.IDLE_TIME);
		
		//Tally the results for the time duration to complete the first job in the queue.
		tallyResultData(jobs[0].getElapsedDuration(), ResultType.FIRST_JOB_DURATION);
		
	}
	
	/**
	 * Handles the recording and tallying of frequencies recorded for the various
	 * test result data obtain from the completed simulation run. 
	 * 
	 * @param value The raw test result value obtained from the simulation run.
	 * @param dataType The type of test result data to be recorded. 
	 */
	public void tallyResultData(int value, ResultType dataType) {
		
		boolean isFound = false;
		int loopValue = 0;
		
		ResultValue currentResult;
		
		/*
		 * Loop through the existing collection of results data until the 
		 * end of the collection is reached, or a matching result is located.
		 */
		while (!isFound && loopValue < results.size()) {
			
			//Obtain the current result value in the colleciton. 
			currentResult = results.get(loopValue);
			
			/*
			 * Check if the result values of the new and current result data match, before checking 
			 * if the result data types match.
			 */
			if (currentResult.getValue() == value && currentResult.getType().equals(dataType)) {
				
				/*
				 * If both match, then the same value for result data type has 
				 * already been recorded before. Therefore instead of duplicating data,
				 * simply increment the tally of the existing result value. 
				 */
				currentResult.incrementTally();
				isFound = true;
				
			} else {
			
				//Otherwise if they do not match, continue through the collection.
				loopValue++;
				
			}
			
		}
		
		/*
		 * If at the end of the result data collection the value has not been located,
		 * add this new value to result data collection, as it is a new unique test result. 
		 */
		if (!isFound) {
			
			ResultValue newResult = new ResultValue(dataType, value);
			newResult.incrementTally();
			
			results.add(newResult);
			
		}
		
	}
	
	/**
	 * Displays the entire collection of result data to the console. 
	 * (USED FOR DE-BUGGING PURPOSES).
	 */
	public void displayResults() {
		
		for (ResultValue currentResult : results) {
			
			System.out.println(currentResult.getType() + " / Value: " + currentResult.getValue() + " / Tally: " + currentResult.getTally());
		
		}
	}
	
	/**
	 * Exports the entire collection of results data for an automated test to an external 
	 * '.csv' file to allow for further analysis using spreadsheet software. 
	 * 
	 * @param fileName Name of the file to be exported to disk.
	 */
	public void exportCSV(String fileName) {
		
		/*
		 * Create a temporary bespoke collection sorter that will sort
		 * the collection of test results into a logical order to aid the 
		 * user when analysing using spreadsheet software. 
		 */
		Collections.sort(results, new Comparator<ResultValue>(){
			  public int compare(ResultValue s1, ResultValue s2) {
				
				//Firstly, sort result type by enum value.
				int sort = s1.getType().compareTo(s2.getType());  
				
				//Before sorting these by value (lowest-highest).
				return sort == 0 ? s1.getValue() - s2.getValue() : sort;
			  }
			});
		
		try {
			
			//Open a file stream.
		    FileWriter writer = new FileWriter(fileName);
		    
		    //OUtput the table headers in CSV format
		    writer.append("Result,Value,Frequency\n");
		    
		    //LOop through every result value in the collection.
		    for (ResultValue currentResult : results) {
		    	
		    	//Obtain the data type and out the appropriate CSV text.
		    	switch (currentResult.getType()) {
		    	
		    		case MEAN_ELAPSED_TIME: 
		    			writer.append("Mean Elapsed Time," + currentResult.getValue() + "," + currentResult.getTally() + "\n");
		    			break;
		    		
		    		case CONTEXT_SWITCHES:
		    			writer.append("Context Switches," + currentResult.getValue() + "," + currentResult.getTally() + "\n");
		    			break;
		    		
		    		case CPU_TIME:
		    			writer.append("CPU Cycles," + currentResult.getValue() + "," + currentResult.getTally() + "\n");
		    			break;
		    			
		    		case IDLE_TIME:
		    			writer.append("Idle Time," + currentResult.getValue() + "," + currentResult.getTally() + "\n");
		    			break;
		    			
		    		case FIRST_JOB_DURATION:
		    			writer.append("First Job Duration," + currentResult.getValue() + "," + currentResult.getTally() + "\n");
		    			break;
		    		
		    		default:
		    			System.out.println("ERROR: Cannot determine result type when exporting CSV file.");
		    			
		    	}
			
			}
	 
		    //Once complete, save and close the new external file. 
		    writer.flush();
		    writer.close();
		}
		
		catch(IOException e) {
		     e.printStackTrace();
		} 
	}
	
	/**
	 * Resets the collection of test result data to allow the 
	 * next automated test to take place.
	 */
	public void resetTests() {
		
		results.clear();
		
	}

}

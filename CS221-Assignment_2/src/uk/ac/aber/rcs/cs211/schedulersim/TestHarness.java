package uk.ac.aber.rcs.cs211.schedulersim;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import uk.ac.aber.rcs.cs211.schedulersim.scheduler.*;

public class TestHarness {

	// SimulatorApplication sim = new SimulatorApplication();
	RoundRobin robin = new RoundRobin();
	ArrayList<Job> jobQueue;
	Simulator simulator;
	Scheduler scheduler;
	Job[] finishedList;
	ArrayList<ResultValue> results = new ArrayList<ResultValue>();

	public TestHarness() {

			try {
				runTest(10000, new LotterySchedulingWeighted(), "/home/connor/workspace/CS221-Assignment_2/src/Test.jobs");
				resetTests();
				
				runTest(10000, new ShortestTimeRemaining(), "/home/connor/workspace/CS221-Assignment_2/src/Test.jobs");
				resetTests();
				
				runTest(10000, new RoundRobin(), "/home/connor/workspace/CS221-Assignment_2/src/Test.jobs");
				resetTests();
				
				runTest(10000, new FirstComeFirstServed(), "/home/connor/workspace/CS221-Assignment_2/src/Test.jobs");
				resetTests();
			
			} catch (IOException e) {
				System.out.println("ERROR: Test cannot complete.");
				e.printStackTrace();
			}

	}

	public static void main(String[] args) {

		new TestHarness();

	}

	public void runTest(int noOfTests, Scheduler newScheduler, String fileName) throws FileNotFoundException,IOException {

		String csvName = newScheduler.getClass().getName() + " - " + new Date(System.currentTimeMillis()).toString() + ".csv";
		
		for (int i = 0; i < noOfTests; i++) {
			
			scheduler = newScheduler;
			
			jobQueue = SimulatorLoader.Load(fileName);

			simulator = new Simulator(scheduler, jobQueue);
			
			while (!simulator.hasfinished()) {
				
				simulator.singleStep();

		/*		try {
					
					Thread.sleep(50);
					
				} catch (InterruptedException e) {
					
				} */

			}

			finishedList = simulator.getFinishedQueue();

			tallyResults(finishedList);

		}
		
		//displayResults();
		exportCSV(csvName);
	}
	
	public void tallyResults(Job[] jobs) {
		
		int total = 0;
		int mean = 0;
		
		for (int i = 0; i < jobs.length; i++) {
			total += jobs[i].getElapsedDuration();
		}	
		
		mean = total / jobs.length;
		
		tallyResultData(mean, ResultType.MEAN_ELAPSED_TIME);
		tallyResultData(simulator.getCpuTicks(), ResultType.CPU_TIME);
		tallyResultData(simulator.getContextSwitchCount(), ResultType.CONTEXT_SWITCHES);
		tallyResultData(simulator.getIdleTimeCount(), ResultType.IDLE_TIME);
		
	}
	
	public void tallyResultData(int value, ResultType dataType) {
		
		boolean isFound = false;
		int loopValue = 0;
		
		ResultValue currentResult;
		
		while (!isFound && loopValue < results.size()) {
			
			currentResult = results.get(loopValue);
			
			if (currentResult.getValue() == value && currentResult.getType().equals(dataType)) {
				
				currentResult.incrementTally();
				isFound = true;
				
			} else {
				
				loopValue++;
				
			}
			
		}
		
		if (!isFound) {
			
			ResultValue newResult = new ResultValue(dataType, value);
			newResult.incrementTally();
			
			results.add(newResult);
			
		}
		
	}
	
	public void displayResults() {
		
		for (ResultValue currentResult : results) {
			
			System.out.println(currentResult.getType() + " / Value: " + currentResult.getValue() + " / Tally: " + currentResult.getTally());
		
		}
	}
	
	public void exportCSV(String fileName) {
		
		Collections.sort(results, new Comparator<ResultValue>(){
			  public int compare(ResultValue s1, ResultValue s2) {
				
				//Sort by result type enum.
				int sort = s1.getType().compareTo(s2.getType());  
				
				//Sort by value.
				return sort == 0 ? s1.getValue() - s2.getValue() : sort;
			  }
			});
		
		try {
			
		    FileWriter writer = new FileWriter(fileName);
		    
		    writer.append("Result,Value,Frequency\n");
		    
		    for (ResultValue currentResult : results) {
		    	
		    /*	if (currentResult.getType().equals(ResultType.MEAN_ELAPSED_TIME)) {
		    		
		    		writer.append("Mean Elapsed Time," + currentResult.getValue() + "," + currentResult.getTally() + "\n");
		    		
		    	} else if (currentResult.getType().equals(ResultType.CONTEXT_SWITCHES)) {
		    		
		    		writer.append("Context Switches," + currentResult.getValue() + "," + currentResult.getTally() + "\n");
		    		
		    	} else if (currentResult.getType().equals(ResultType.CPU_TIME)){
		    		
		    		writer.append("CPU Cycles," + currentResult.getValue() + "," + currentResult.getTally() + "\n");
		    		
		    	} else {
		    		
		    		writer.append("Idle Time," + currentResult.getValue() + "," + currentResult.getTally() + "\n");
		    	
		    	} */
		    	
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
		    		
		    		default:
		    			System.out.println("ERROR: Cannot determine result type when exporting CSV file.");
		    			
		    	}
			
			}
	 
		    writer.flush();
		    writer.close();
		}
		
		catch(IOException e) {
		     e.printStackTrace();
		} 
	}
	
	public void resetTests() {
		
		results.clear();
	}

}

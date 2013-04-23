package uk.ac.aber.rcs.cs211.schedulersim.scheduler;

import uk.ac.aber.rcs.cs211.schedulersim.AbstractScheduler;
import uk.ac.aber.rcs.cs211.schedulersim.Job;

public class ShortestTimeRemaining extends AbstractScheduler {
	
public void addNewJob(Job job) throws SchedulerException {
		
		boolean added = false;
		int test = 0;
		
		int newTimeRemaining = job.getLength() - job.getProgramCounter();
		
		int currentTimeRemaining = 0;
		
		if (this.queue.contains(job)) {
			throw new SchedulerException("Job already on Queue");
		}
		
		if (numberOfJobs == 0) {
			
			this.queue.add(this.numberOfJobs, job);
			this.numberOfJobs++;
			
		} else {
			
			while(!(added) && test < numberOfJobs) {
				
				//System.out.println(test);
				//Job current = (Job) queue.get(test);
				
				currentTimeRemaining = queue.get(test).getLength() - queue.get(test).getProgramCounter();
				
				if (newTimeRemaining < currentTimeRemaining) {
					
					//removeJob(current);
					
					queue.add(test, job);
					added = true;
					numberOfJobs++;
					//queue.add(numberOfJobs, current);
					//this.numberOfJobs++;
					
				} else {
					
					test++;
					
				}	
			}
			
			if (!added) {
				
				queue.add(numberOfJobs, job);
				this.numberOfJobs++;
				
			}
				
		}
		
		//printQueue();
	}
	
	public void printQueue() {
		
		System.out.println("******************\n");
		for (int i = 0; i < queue.size(); i++) {
			
			Job test = (Job) queue.get(i);
			
			System.out.println(test.getName() + " / " + test.getLength());
		}
		System.out.println("\n******************\n");
		
	}
	
	public void reEvaluateQueue(Job job) {
		
		int newTimeRemaining = job.getLength() - job.getProgramCounter();
		
		int currentTimeRemaining = 0;
		
		boolean added = false;
		int test = 0;
		
		if (numberOfJobs == 0) {
			
			this.queue.add(this.numberOfJobs, job);
			//this.numberOfJobs++;
			
		} else {
			queue.remove(job);
			this.numberOfJobs--;
			
			while(!(added) && test < numberOfJobs) {
				
				//System.out.println(test);
				//Job current = (Job) queue.get(test);
				
				currentTimeRemaining = queue.get(test).getLength() - queue.get(test).getProgramCounter();
				
				if (newTimeRemaining < currentTimeRemaining) {
					
					//removeJob(current);
					
					queue.add(test, job);
					added = true;
					numberOfJobs++;
					//queue.add(numberOfJobs, current);
					//this.numberOfJobs++;
					
				} else {
					
					test++;
					
				}	
			}
			
			if (!added) {
				
				queue.add(numberOfJobs, job);
				this.numberOfJobs++;
				
			}	
	}

	}
	
	public void returnJob(Job job) throws SchedulerException {
		if (!this.queue.contains(job)) throw new SchedulerException("Job not on Queue");
		reEvaluateQueue(job);
	}

}

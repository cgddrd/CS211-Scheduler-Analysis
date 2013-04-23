package uk.ac.aber.rcs.cs211.schedulersim.scheduler;

import java.util.ArrayList;

import uk.ac.aber.rcs.cs211.schedulersim.Job;
import uk.ac.aber.rcs.cs211.schedulersim.Scheduler;

public class ShortestJobNext implements Scheduler {
	
	protected ArrayList<Job> queue;
	private int numberOfJobs;
	
	public ShortestJobNext() {
		this.queue = new ArrayList<Job>();
		this.numberOfJobs=0;
	}

	@Override
	public Job getNextJob() throws SchedulerException {
		
		Job lastJobReturned;
		
		if (this.numberOfJobs<1) {
			throw new SchedulerException("Empty Queue");
		}
		
		lastJobReturned = (Job) this.queue.get(0);
		
		return lastJobReturned;
		
	}

	@Override
	public void addNewJob(Job job) throws SchedulerException {
		
		boolean added = false;
		int test = 0;
		
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
				
				if (job.getLength() < queue.get(test).getLength()) {
					
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
		
		printQueue();
	}
	
	public void printQueue() {
		
		System.out.println("******************\n");
		for (int i = 0; i < queue.size(); i++) {
			
			Job test = (Job) queue.get(i);
			
			System.out.println(test.getName() + " / " + test.getLength());
		}
		System.out.println("\n******************\n");
		
	}

	@Override
	public void returnJob(Job job) throws SchedulerException {
		
		if (!this.queue.contains(job)) {
			throw new SchedulerException("Job not on Queue");
		}
		
		//removeJob(job);
		
		//addNewJob(job);

	}

	@Override
	public void removeJob(Job job) throws SchedulerException {
		
		if (!this.queue.contains(job)) {
			throw new SchedulerException("Job not on Queue");
		}
		
		this.queue.remove(job);
		this.numberOfJobs--;
		
	}

	@Override
	public void reset() {
		
		this.queue.clear();
		this.numberOfJobs=0;
		
	}

	@Override
	public Job[] getJobList() {
		
		Job[] jobs = new Job[queue.size()];
		
		for (int i=0; i<queue.size(); i++) {
			jobs[i]=this.queue.get(i);
		}
		
		return jobs;
	}
}

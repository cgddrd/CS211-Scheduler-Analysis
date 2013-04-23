package uk.ac.aber.rcs.cs211.schedulersim.scheduler;

import java.util.ArrayList;
import java.util.Random;

import uk.ac.aber.rcs.cs211.schedulersim.Job;
import uk.ac.aber.rcs.cs211.schedulersim.Scheduler;

public class LotteryScheduling implements Scheduler {
	
	protected ArrayList<Job> queue;
	private int numberOfJobs;
	private Random randGen;
	
	public LotteryScheduling() {
		this.queue = new ArrayList<Job>();
		this.numberOfJobs=0;
		this.randGen = new Random();
	}
	
	public int selectNextTicket() {
		
		int Low = 0;
		int High = numberOfJobs;
		
		return randGen.nextInt(High-Low) + Low;
		
	}

	@Override
	public Job getNextJob() throws SchedulerException {
		
		Job lastJobReturned;
		
		if (this.numberOfJobs<1) {
			throw new SchedulerException("Empty Queue");
		}
		
		lastJobReturned = (Job) this.queue.get(0);
		
		//System.out.println("Job: " + lastJobReturned.getName() + " Time: " + lastJobReturned.getElapsedDuration());
		
		return lastJobReturned;
		
	}

	@Override
	public void addNewJob(Job job) throws SchedulerException {
		
		if (this.queue.contains(job)) {
			throw new SchedulerException("Job already on Queue");
		}
		
		this.queue.add(this.numberOfJobs, job);
		this.numberOfJobs++;
	}

	@Override
	public void returnJob(Job job) throws SchedulerException {
		
		if (!this.queue.contains(job)) {
			throw new SchedulerException("Job not on Queue");
		}
		
		removeJob(job);
		
		
		addNewJob(job);
		
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

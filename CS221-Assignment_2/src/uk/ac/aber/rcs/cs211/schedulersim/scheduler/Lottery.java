package uk.ac.aber.rcs.cs211.schedulersim.scheduler;

import java.util.Random;

import uk.ac.aber.rcs.cs211.schedulersim.AbstractScheduler;
import uk.ac.aber.rcs.cs211.schedulersim.Job;

public class Lottery extends AbstractScheduler {
	
	private Random randGen;
	
	public Lottery() {
		randGen = new Random();
	}
	
	private void runLottery() throws SchedulerException {
		
		int token = randGen.nextInt(this.numberOfJobs-0) + 0;
		
		Job tempJob = queue.get(token);
		
		removeJob(tempJob);
		
		queue.add(0, tempJob);
		numberOfJobs++;
			
	}
	
	public void addNewJob(Job job) throws SchedulerException {
		if (this.queue.contains(job)) throw new SchedulerException("Job already on Queue");
		
		this.queue.add(this.numberOfJobs, job);
		this.numberOfJobs++;
		runLottery();
	}
	
	public void returnJob(Job job) throws SchedulerException {
		if (!this.queue.contains(job)) throw new SchedulerException("Job not on Queue");
		runLottery();
	}

}

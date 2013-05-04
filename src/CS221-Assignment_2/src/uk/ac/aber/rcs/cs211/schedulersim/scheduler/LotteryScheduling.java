package uk.ac.aber.rcs.cs211.schedulersim.scheduler;

import java.util.Random;

import uk.ac.aber.rcs.cs211.schedulersim.AbstractScheduler;
import uk.ac.aber.rcs.cs211.schedulersim.Job;

/**
 * "Non-weighted probabilistic lottery" scheduling algorithm produced as part of the scheduler analysis investigation.
 * This non-deterministic algorithm selects a random job to be processed next by the CPU. Unlike the it's weighted 
 * equivalent, this algorithm selects any job completely at random regardless of the priority of a job.
 * 
 * @author Connor Goddard (clg11)
 * @see uk.ac.aber.rcs.cs211.schedulersim.AbstractScheduler
 */
public class LotteryScheduling extends AbstractScheduler {
	
	private Random randGen;
	
	public LotteryScheduling() {
		randGen = new Random();
	}
	
	/**
	 * Generates a new random number within the size of the job queue before selecting 
	 * the job at that index to be scheduled next. 
	 * 
	 * @throws uk.ac.aber.rcs.cs211.schedulersim.scheduler.SchedulerException
	 */
	private void runLottery() throws SchedulerException {
		
		/*
		 * Generate a random number that falls within the size of the 
		 * job queue.
		 */
		int token = randGen.nextInt(this.numberOfJobs);
		
		/*
		 * Select the job located at the index position
		 * of that generated number. 
		 */
		Job tempJob = queue.get(token);
		
		//Temporarily remove the job from the queue..
		removeJob(tempJob);
		
		/*
		 * ..and place it at the front of the queue,
		 * shifting all other jobs to the right. 
		 */
		queue.add(0, tempJob);
		numberOfJobs++;
			
	}
	
	/**
	 * Overrides the base "addNewJob" method in @link{uk.ac.aber.rcs.cs211.schedulersim.AbstractScheduler}
	 * to enable selection of a new random job to be moved to the front of the queue when a new job is added.
	 * 
	 * @param job The new process "job" to be added to the CPU job queue.
	 * @throws uk.ac.aber.rcs.cs211.schedulersim.scheduler.SchedulerException
	 */
	@Override
	public void addNewJob(Job job) throws SchedulerException {
		
		//Check that the new job does not currently exist in the queue.
		if (this.queue.contains(job)) throw new SchedulerException("Job already on Queue");
		
		//Add the new job to the back of the job queue.
		this.queue.add(this.numberOfJobs, job);
		this.numberOfJobs++;
		
		//Re-allocate the lottery tickets and select a new job to be scheduled next.
		runLottery();
	}
	
	/**
	 * Overrides the base "returnJob" method in @link{uk.ac.aber.rcs.cs211.schedulersim.AbstractScheduler}
	 * to enable a new random job to be moved to the front of the queue when a job is 
	 * returned to the queue.
	 * 
	 * @param job The existing process "job" to be returned to the CPU job queue.
	 * @throws uk.ac.aber.rcs.cs211.schedulersim.scheduler.SchedulerException
	 */
	@Override
	public void returnJob(Job job) throws SchedulerException {
		
		//Check that the job has not already been removed from the queue.
		if (!this.queue.contains(job)) throw new SchedulerException("Job not on Queue");
		
		//Re-allocate the lottery tickets and select a new job to be scheduled next.
		runLottery();
		
	}

}

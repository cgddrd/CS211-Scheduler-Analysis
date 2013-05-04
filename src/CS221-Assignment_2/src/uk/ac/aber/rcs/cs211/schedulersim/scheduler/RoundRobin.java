package uk.ac.aber.rcs.cs211.schedulersim.scheduler;

import uk.ac.aber.rcs.cs211.schedulersim.AbstractScheduler;
import uk.ac.aber.rcs.cs211.schedulersim.Job;


/**
 * "Round robin" scheduling algorithm.
 * This preemptive, deterministic algorithm processes jobs in the order their original order within the queue, with
 * each process being allocated a certain length of CPU Burst, before being moved to the back of the queue until it
 * is their turn once again, or they complete. 
 * 
 * PLEASE NOTE: THIS ALGORITHM WAS NOT CHOSEN TO BE TESTED AS PART OF THE INVESTIGATION, HOWEVER HAS REMAINED IMPLEMENTED
 * FOR FUTURE REFERENCE.
 * 
 * @author Connor Goddard (clg11)
 * @see uk.ac.aber.rcs.cs211.schedulersim.AbstractScheduler
 */
public class RoundRobin extends AbstractScheduler {
	

	public RoundRobin() {
	
	}
	
	/**
	 * Overrides the base "returnJob" method in @link{uk.ac.aber.rcs.cs211.schedulersim.AbstractScheduler}
	 * to move the returned job to the back of the queue in order to wait it's equal turn before it 
	 * it scheduled once again.
	 * 
	 * @param job The existing process "job" to be returned to the CPU job queue.
	 * @throws uk.ac.aber.rcs.cs211.schedulersim.scheduler.SchedulerException
	 */
	@Override
	public void returnJob(Job job) throws SchedulerException {
		
		//Check that the job has not been removed from the job queue. 
		if (!this.queue.contains(job)) {
			throw new SchedulerException("Job not on Queue");
		}
		
		//Temporarily remove the job from the queue.
		removeJob(job);
		
		//Re-position the same job to the back of the queue. 
		addNewJob(job);
		
	}
}

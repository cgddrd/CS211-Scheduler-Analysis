package uk.ac.aber.rcs.cs211.schedulersim.scheduler;

import java.util.ArrayList;
import java.util.Random;

import uk.ac.aber.rcs.cs211.schedulersim.AbstractScheduler;
import uk.ac.aber.rcs.cs211.schedulersim.Job;

/**
 * "Weighted probabilistic lottery" scheduling algorithm produced as part of the scheduler analysis investigation.
 * This non-deterministic algorithm generates and allocates a number of "lottery tickets" to each process
 * contained within the job queue. The number of tickets allocated per job is determined by the priority, resulting in jobs
 * with a higher priority having an increased probability of being chosen next. 
 * 
 * There is however no guarantee that will become apparent in the output, as the selection of jobs is still random. 
 * 
 * @author Connor Goddard (clg11)
 * @see uk.ac.aber.rcs.cs211.schedulersim.AbstractScheduler
 */
public class LotterySchedulingWeighted extends AbstractScheduler {
	
	private Random randGen;
	
	/** Collection of all the lottery tickets allocated to the waiting jobs. */
	private ArrayList<LotteryTicket> tickets = new ArrayList<LotteryTicket>(); 
	
	public LotterySchedulingWeighted() {
		randGen = new Random();
	}
	
	/**
	 * Allocates a number of tickets to all the the jobs in the queue (based on the priority of the job),
	 * before selecting one of these tickets at random that will represent 
	 * the job that is processed next.
	 * 
	 * @throws uk.ac.aber.rcs.cs211.schedulersim.scheduler.SchedulerException
	 */
	private void runLottery() throws SchedulerException {
		
		//Distribute new lottery tickets to all the jobs in the queue.
		allocateTickets();
		
		/*
		 * Generate a random number that falls within the size of the 
		 * lottery ticket data collection.
		 */
		int token = randGen.nextInt(tickets.size());
		
		
		//Obtain the job that ticket is held by.
		Job tempJob = tickets.get(token).getHolder();
		
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
	 * to enable the re-allocation of lottery tickets (taking into account the new job) and 
	 * allows a new random job to be moved to the front of the queue when a new job is added.
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
		if (!this.queue.contains(job)) throw new SchedulerException("Job: " + job + " not on Queue");
		
		//Re-allocate the lottery tickets and select a new job to be scheduled next.
		runLottery();
	}
	
	/**
	 * Distributes a quantity of "lottery tickets" to each job ready for a random ticket
	 * to then be selected. The number of tickets allocated to each job is dependent on the priority of the
	 * job. (i.e. A job with priority '3' will have 3 lottery tickets allocated, resulting in it having a higher 
	 * Probability of being selected. However there is no guarantee of this.) 
	 */
	private void allocateTickets() {
		
		//Remove all existing lottery tickets.
		tickets.clear();
		
		//Loop through all the jobs in the queue
		for (Job currentJob : queue) {
			
			/*
			 * Allocate the same number of lottery tickets to
			 * the job as the size of the it's priority. 
			 */
			for (int i = 0; i < currentJob.getPriority(); i++) {
				
				tickets.add(new LotteryTicket(tickets.size(), currentJob));
				
			}
		}
	}
	
}
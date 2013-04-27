package uk.ac.aber.rcs.cs211.schedulersim.scheduler;

import java.util.ArrayList;
import java.util.Random;

import uk.ac.aber.rcs.cs211.schedulersim.AbstractScheduler;
import uk.ac.aber.rcs.cs211.schedulersim.Job;

public class LotterySchedulingWeighted extends AbstractScheduler {
	
	private Random randGen;
	private ArrayList<LotteryTicket> tickets = new ArrayList<LotteryTicket>(); 
	
	public LotterySchedulingWeighted() {
		randGen = new Random();
	}
	
	private void runLottery() throws SchedulerException {
		
		allocateTickets();
		
		int token = randGen.nextInt(tickets.size());
		
		Job tempJob = tickets.get(token).getHolder();
		
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
		if (!this.queue.contains(job)) throw new SchedulerException("Job: " + job + " not on Queue");
		runLottery();
	}
	
	public void allocateTickets() {
		
		tickets.clear();
		
		for (Job currentJob : queue) {
			
			for (int i = 0; i < currentJob.getPriority(); i++) {
				
				tickets.add(new LotteryTicket(tickets.size(), currentJob));
				
			}
		}
	}
	
}
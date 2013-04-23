package uk.ac.aber.rcs.cs211.schedulersim.scheduler;

import uk.ac.aber.rcs.cs211.schedulersim.AbstractScheduler;
import uk.ac.aber.rcs.cs211.schedulersim.Job;

public class RoundRobin extends AbstractScheduler {
	
	@Override
	public void returnJob(Job job) throws SchedulerException {
		
		if (!this.queue.contains(job)) {
			throw new SchedulerException("Job not on Queue");
		}
		
		removeJob(job);
		addNewJob(job);
		
	}
}

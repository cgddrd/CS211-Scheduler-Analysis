package uk.ac.aber.rcs.cs211.schedulersim.scheduler;

import uk.ac.aber.rcs.cs211.schedulersim.AbstractScheduler;
import uk.ac.aber.rcs.cs211.schedulersim.Job;

/**
 * "Shortest remaining time" scheduling algorithm produced as part of the scheduler analysis investigation.
 * This algorithm always selects the job with the shortest remaining execution time
 * to be processed next. This is achieved through "sorting" of the array when a new job is added, and an
 * existing job returned to the array. 
 * 
 * @author Connor Goddard (clg11)
 * @see uk.ac.aber.rcs.cs211.schedulersim.AbstractScheduler
 */
public class ShortestTimeRemaining extends AbstractScheduler {
	
	public ShortestTimeRemaining() {
			
	}

	/**
	 * Overrides the base "addNewJob()" method in @link{uk.ac.aber.rcs.cs211.schedulersim.AbstractScheduler}
	 * to enable the job queue to be re-sorted taking into consideration the new job being added.
	 * @param job The new process "job" to be added to the CPU job queue.
	 * @throws uk.ac.aber.rcs.cs211.schedulersim.scheduler.SchedulerException
	 */
	@Override
	public void addNewJob(Job job) throws SchedulerException {
		
		//Check if the job already exists on the queue. (This would be a serious problem).
		if (this.queue.contains(job)) throw new SchedulerException("Job already on Queue");
		
		//Add the job and re-sort the job queue taking into consideration the new job.
		reEvaluateQueue(job, false);
	}
	
	/**
	 * Overrides the base "returnJob()" method in @link{uk.ac.aber.rcs.cs211.schedulersim.AbstractScheduler}
	 * to enable the job queue to be re-sorted and updated when a job is returned from a CPU burst.
	 * @param job The existing "job" to be returned to the CPU job queue.
	 * @throws uk.ac.aber.rcs.cs211.schedulersim.scheduler.SchedulerException
	 */
	@Override
	public void returnJob(Job job) throws SchedulerException {
		
		//Check if the job to be returned still actually exists.
		if (!this.queue.contains(job))
			throw new SchedulerException("Job not on Queue");
		
		//Re-sort the job queue taking into consideration the updated program counter of the job.
		reEvaluateQueue(job, true);
	}

	/**
	 * Updates and "sorts" the elements in the job queue based on the remaining execution time
	 * of the new job being added or returned to the queue. If the job being added to the queue, then
	 * it is simply inserted the correct position based on it's total execution time. 
	 * 
	 * If however an existing job is being returned to the queue, then the correct position 
	 * (taking into consideration the now reduced remaining execution time) is re-calculated, and if appropriate
	 * the job is removed and re-inserted into the job queue at the new position.
	 * @param job The CPU process "job" to be added/returned to the job queue.
	 */
	private void reEvaluateQueue(Job job, boolean returningJob) {

		//Calculate the remaining execution time required for the job.
		int newTimeRemaining = job.getLength() - job.getProgramCounter();

		int currentTimeRemaining = 0, index = 0;

		boolean queueUpdated = false;

		//If the job queue is empty..
		if (numberOfJobs <= 0) {

				
			//Simply add the job into the first position of the queue.
			queue.add(this.numberOfJobs, job);
			
			/*
			 * If the job is new (i.e. not returning from a CPU burst),
			 * then the total number of jobs in the queue need to be incremented. 
			 * (This is not the case is a job is being returned, as it is simply being moved around
			 * the queue, not being added/removed entirely).
			 */
			if (!returningJob) {
				this.numberOfJobs++;
			}

		//If the job queue is not empty..
		} else {

			/*
			 * If the job is returning, then it must first be 
			 * removed from the queue to allow it to be moved accordingly to it's
			 * potential new position. 
			 */
			if (returningJob) {
				queue.remove(job);
				this.numberOfJobs--;
			}

			//Loop through the job queue as long as the queue has not been updated.. 
			while (index < numberOfJobs && !(queueUpdated)) {

				//Calculate the remaining execution time of the current job being inspected.
				currentTimeRemaining = queue.get(index).getLength()
						- queue.get(index).getProgramCounter();

				//If the job being updated has a smaller remaining execution time than the current job
				if (newTimeRemaining < currentTimeRemaining) {

					/*
					 * Then it should be positioned at that index,
					 * and all other jobs shifted right, as this job should be run before
					 * any of the jobs after it's position.  
					 */
					queue.add(index, job);
					queueUpdated = true;
					numberOfJobs++;

				//Otherwise continue along the queue.
				} else {

					index++;

				}
			}

			/*
			 * If after comparing all the other jobs in the queue, 
			 * the job still has a greater remaining execution time, it should
			 * be placed at the back of the queue.
			 */
			if (!queueUpdated) {

				queue.add(numberOfJobs, job);
				this.numberOfJobs++;

			}
		}

	}
}

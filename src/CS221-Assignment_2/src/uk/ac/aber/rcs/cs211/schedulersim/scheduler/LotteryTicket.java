package uk.ac.aber.rcs.cs211.schedulersim.scheduler;

import uk.ac.aber.rcs.cs211.schedulersim.Job;

/**
 * Provides a "data model" representation of a single "lottery ticket" utilised by 
 * @link{uk.ac.aber.rcs.cs211.schedulersim.scheduler.LotterySchedulingWeighted}.
 *
 * @author Connor Goddard (clg11)
 */
public class LotteryTicket {
	
	/** Represents the process job that the ticket belongs to. */
	private Job holder;
	
	/** Represents the unique ID number for the ticket */
	private int ticketNo;
	
	
	/**
	 * Default constructor for a new lottery ticket.
	 */
	public LotteryTicket() {
		
	}
	
	/**
	 * Bespoke constructor used to instantiate and populate a new 
	 * lottery ticket. 
	 * @param newTicketNo The unique number for the new ticket.
	 * @param newHolder The job that the new ticket is allocated to.
	 */
	public LotteryTicket(int newTicketNo, Job newHolder) {
		
		this.holder = newHolder;
		this.ticketNo = newTicketNo;
		
	}

	/**
	 * Returns the job that the ticket is allocated to.
	 * @return The holder of the lottery ticket @see{uk.ac.aber.rcs.cs211.schedulersim.Job}
	 */
	public Job getHolder() {
		return holder;
	}
	
	/**
	 * Returns the unique number of the lottery ticket.
	 * @return The unique ID Number.
	 */
	public int getTicketNo() {
		return ticketNo;
	}

}


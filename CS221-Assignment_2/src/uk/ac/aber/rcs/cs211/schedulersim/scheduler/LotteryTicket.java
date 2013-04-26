package uk.ac.aber.rcs.cs211.schedulersim.scheduler;

import uk.ac.aber.rcs.cs211.schedulersim.Job;

public class LotteryTicket {
	
	private Job holder;
	
	private int ticketNo;
	
	public LotteryTicket(int newTicketNo, Job newHolder) {
		
		this.holder = newHolder;
		this.ticketNo = newTicketNo;
		
	}

	public Job getHolder() {
		return holder;
	}

	public int getTicketNo() {
		return ticketNo;
	}

}


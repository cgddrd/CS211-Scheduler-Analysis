package uk.ac.aber.rcs.cs211.schedulersim;

/**
 * Class used to represent an individual test result value processed
 * within @link{uk.ac.aber.rcs.cs211.schedulersim.AutomatedScheduler}.
 * 
 * @author Connor Goddard (clg11)
 */
public class ResultValue {
	
	/** The enum data type of the test result. */
	private ResultType type;
	
	/** The unique test result value. */
	private int value;
	
	/** Records the frequency of the same value being obtained from automated testing. */
	private int tally;
	
	/**
	 * Default constructor for a new lottery ticket.
	 */
	public ResultValue() {
			
	}
	
	/**
	 * Bespoke constructor used to instantiate and populate a new 
	 * result value. 
	 * @param newType The new result type.
	 * @param newHolder The new unique test result value.
	 */
	public ResultValue(ResultType newType, int newValue) {	
		type = newType;
		value = newValue;
		tally = 0;	
	}

	/**
	 * Increments the frequency tally for the result value by one.
	 */
	public void incrementTally() {	
		tally++;	
	}
	
	/**
	 * Returns the unique test result value. 
	 * @return The unique value of the test result. 
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Returns the result value type. 
	 * @return The value type of the test result.
	 */
	public ResultType getType() {
		return type;
	}
	
	/**
	 * Returns the result value frequency tally. 
	 * @return The frequency tally of the test result. 
	 */
	public int getTally() {
		return tally;
	}

}

package uk.ac.aber.rcs.cs211.schedulersim;

public class ResultValue {
	
	private ResultType type;
	private int value;
	private int tally;
	
	public ResultValue(ResultType newType, int newValue) {
		
		type = newType;
		value = newValue;
		tally = 0;
		
	}


	public void incrementTally() {	
		tally++;	
	}
	
	
	public int getValue() {
		return value;
	}

	public ResultType getType() {
		return type;
	}

	public int getTally() {
		return tally;
	}

}

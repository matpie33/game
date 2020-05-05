package enums;

public enum State {
	IN_PROGRESS, NOT_RUNNING, REQUESTED;

	public boolean inProgress (){
		return equals(IN_PROGRESS);
	}

	public boolean notRunning (){
		return equals(NOT_RUNNING);
	}

	public boolean requested(){
		return equals(REQUESTED);
	}

}

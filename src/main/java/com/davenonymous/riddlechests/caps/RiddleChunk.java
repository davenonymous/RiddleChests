package com.davenonymous.riddlechests.caps;

public class RiddleChunk implements IRiddleChunk {
	boolean isRiddled = false;

	@Override
	public boolean isRiddled() {
		return isRiddled;
	}

	@Override
	public void setRiddled() {
		isRiddled = true;
	}
}
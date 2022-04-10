package me.lorenzo.scoreboardapi.scoreboard;

import lombok.Getter;

@Getter
public enum Style {

	CLASSIC(false, 1), CUSTOM(false, 0);

	private boolean descending;
	private int startNumber;

	
	Style(boolean descending, int startNumber) {
		this.descending = descending;
		this.startNumber = startNumber;
	}

	public Style reverse() {
		return descending(!this.descending);
	}

	public Style descending(boolean descending) {
		this.descending = descending;
		return this;
	}

	public Style startNumber(int startNumber) {
		this.startNumber = startNumber;
		return this;
	}

}

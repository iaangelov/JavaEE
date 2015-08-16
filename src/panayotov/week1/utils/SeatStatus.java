package panayotov.week1.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SeatStatus {

	TAKEN("Taken"), RESERVED("Reserved"), AVAILABLE("Available");

	private String text;

	private SeatStatus(String text) {
		this.text = text;
	}

	@JsonCreator
	public static SeatStatus getType(String text) {
		if (text == null) {
			return null;
		}
		for (SeatStatus type : SeatStatus.values()) {
			if (text.equalsIgnoreCase(type.getText())) {
				return type;
			}
		}
		throw new IllegalArgumentException("No matching type for type " + text + " is the value");
	}

	@JsonValue
	public String getText() {
		return text;
	}
}

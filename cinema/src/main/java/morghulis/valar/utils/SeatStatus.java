package morghulis.valar.utils;

public enum SeatStatus {

	TAKEN("Taken"), RESERVED("Reserved"), AVAILABLE("Available");

	private String text;

	private SeatStatus(String text) {
		this.text = text;
	}

	public static SeatStatus getType(String text) {

		if (text == null) {
			return null;
		}

		for (SeatStatus type : SeatStatus.values()) {
			if (text.equals(type.getText())) {
				return type;
			}
		}
		throw new IllegalArgumentException("No matching type for type " + text);
	}

	public String getText() {
		return text;
	}
}

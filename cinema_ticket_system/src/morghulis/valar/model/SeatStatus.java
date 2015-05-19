package morghulis.valar.model;

public enum SeatStatus {

	TAKEN("Taken"), RESERVED("Reserved"), AVAILABLE("Available");
	
	private String text;
	
	private SeatStatus(String text){
		this.text = text;
	}
	
	@Override
	public String toString(){
		return this.text;
	}
}

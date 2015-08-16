package panayotov.week1.dao;

public final class QueryNames {
	public static final String Movie_FindByName = "findByName";
	public static final String Movie_GetAllMovies = "getAllMovies";
	public static final String Screening_GetAllScreenings = "allScreenings";
	public static final String Screening_GetAllScreeningsByHallID = "allScreeningsByHallId";
	public static final String Screening_GetAllScreeningsByScreeningID = "allScreeningsByScreeningId";
	public static final String Screening_GetAllScreeningsByMovieName = "allScreeningsByMovieName";
	public static final String User_GetAllAsAdmin = "getAllAdmin";
	public static final String User_GetAllAsUser = "getAllUser";
	public static final String User_FindByUsername = "findByUsername";
	public static final String User_ValidateCredentials = "validateCredentials";
	public static final String Hall_GetAllHalls = "getAllHalls";
	public static final String Ticket_GetAllTickets = "getAllTickets";
	public static final String Ticket_FindByUserID = "findByUserId";
	public static final String Ticket_FindByScreeningID = "findByScreeningId";
	public static final String Ticket_FindReservedByUserID = "findReservedByUserId";
	public static final String Ticket_FindByUserNames = "findByUserName";
	public static final String Ticket_FindByUser = "findByUser";
	
	
	private QueryNames(){}
}
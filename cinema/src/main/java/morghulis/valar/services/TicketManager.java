package morghulis.valar.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import morghulis.valar.dao.ScreeningDAO;
import morghulis.valar.dao.TicketDAO;
import morghulis.valar.model.Ticket;

@Stateless
@Path("ticket")
public class TicketManager {
 
        @EJB
        private TicketDAO ticketDao;
       
        @EJB
        private ScreeningDAO screeningDao;
        
        @Inject
        private UserContext UserContext;
       
        @GET
        @Path("all")
        @Produces("application/json")
        public Collection<Ticket> getAllTickets() {
            return ticketDao.getAllTickets();
        }
        
        @POST
    	@Path("add")
    	public void addTicket(Ticket newTicket) {
    		if (newTicket != null) {
    			ticketDao.add(newTicket);
    		}
    	}
        
        @POST
    	@Path("addAndBuy")
    	public Response addAndBuy(Ticket newTicket) {
    		if (newTicket != null) {
    			newTicket.setScreening(screeningDao.findById(newTicket.getScreening().getId()));
    			ticketDao.add(newTicket);
    		//	System.out.println("coning from post " + UserContext.getCurrentUser() == null);
    			ticketDao.buyTicket(newTicket, UserContext.getCurrentUser());
    			return Response.ok().build();
    		}
    		return Response.noContent().build();
    		
    	}
        
        @POST
    	@Path("addAndReserve")
    	public Response addAndReserve(Ticket newTicket) {
    		if (newTicket != null) {
    			newTicket.setScreening(screeningDao.findById(newTicket.getScreening().getId()));
    			ticketDao.add(newTicket);
    		//	System.out.println("coning from post " + UserContext.getCurrentUser() == null);
    			ticketDao.reserveTicket(newTicket, UserContext.getCurrentUser());
    			return Response.ok().build();
    		}
    		return Response.noContent().build();
    		
    	}
        
        @DELETE
    	@Path("remove")
    	@Consumes(MediaType.APPLICATION_JSON)
    	public Response deleteTicket(@QueryParam("ticketId") String ticketId) {
    		Ticket ticketToRemove = ticketDao.findById(Long.parseLong(ticketId));
    		if (ticketToRemove != null) {
    			ticketDao.deleteTicket(ticketToRemove.getId());
    		}
    		return Response.noContent().build();
    	}
        
        
        //poprincip tozi metod e za administratorite , no ako vse pak customer go izvika s kakuvto i da e parametur 6te my vurne mytickets
        @GET
        @Path("/byUserId/{userId}")
        @Produces("application/json")
        public Collection<Ticket> getTicketsByUserId(@PathParam("userId") Long id){
        	try{
            	Collection<Ticket> findTicketsByUserId = ticketDao.findTicketsByUserId();
            	} catch(NullPointerException ex){
            		//throw new NullPointerException();
            	}
                    return new ArrayList<Ticket>();
        }
       
        //tova e za customeri da si vidqt tehnite bileti.. 
        //adminite susto mogat da hodqt na kino i da si kupuvat bileti taka 4e i te mogat da si vidqt tehnite s tozi metod
        @GET
        @Path("/mytickets")
        @Produces("application/json")
        public Collection<Ticket> getMyTickets(){
        	//try{
        	Collection<Ticket> findTicketsByUserId = ticketDao.findTicketsByUserId();
        	//} catch(NullPointerException ex){
        		//throw new NullPointerException();
        	//}
                return findTicketsByUserId;
        }
        
        @GET
        @Path("/byScreeningId/{screeningId}")
        @Produces("application/json")
        public Collection<Ticket> getTicketsByScreeningId(@PathParam("screeningId") Long id){
                return ticketDao.findTicketsByScreeningId(id);
        }
        
        @PUT
        @Path("/buyTicket")
        public Response buyTicket(@QueryParam("ticketId") String ticketId){
            Ticket ticketToBuy = ticketDao.findById(Long.parseLong(ticketId));
            if(ticketToBuy != null){
                ticketDao.buyTicket(ticketToBuy, UserContext.getCurrentUser());
            }
            return Response.noContent().build();
        }
        
        @PUT
        @Path("/reserveTicket")
        public Response reserveTicket(@QueryParam("ticketId") String ticketId){
            Ticket ticketToReserve = ticketDao.findById(Long.parseLong(ticketId));
            if(ticketToReserve != null){
            	ticketDao.reserveTicket(ticketToReserve, UserContext.getCurrentUser());
            	return Response.ok().build();
            }
            return Response.noContent().build();
        }
        
        @POST
        @Path("/confirmReservation")
        public Response confirmReservation(){
        	int confirmReservation = ticketDao.confirmReservation();
        	if(confirmReservation == -1){
        		return Response.status(304).build();
        	}
        	return Response.status(200).build();
        }
        
        @DELETE
        @Produces("application/json")	
        @Path("/cancelMyReservations")
        public Collection<Ticket> cancelMyReservations(){
        	Collection<Ticket> cancelMyReservations = ticketDao.cancelMyReservations();
        	
        	return cancelMyReservations;
        }
        
        @GET
        @Path("/test/{ticketId}")
        @Produces("application/json")
        public Ticket getById(@PathParam("ticketId") Long id){
        	//Ticket findById = null;
        
               // findById = ticketDao.findById(id);
        	//return findById;
                return (Ticket) ticketDao.getAllTickets().toArray()[0];
        }
       
}
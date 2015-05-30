package morghulis.valar.services;

import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import morghulis.valar.dao.TicketDAO;
import morghulis.valar.model.Movie;
import morghulis.valar.model.Ticket;

@Stateless
@Path("ticket")
public class TicketManager {
 
        @EJB
        private TicketDAO ticketDao;
       
        @Inject
        private UserContext UserContext;
       
        @GET
        @Path("all")
        @Produces("application/json")
        public List<Ticket> getAllTickets() {
            System.out.println(ticketDao.getAllTickets());
            return ticketDao.getAllTickets();
        }
        
        @PUT
    	@Path("add")
    	@Consumes(MediaType.APPLICATION_JSON)
    	public void addMovie(Ticket newTicket) {
    		if (newTicket != null) {
    			ticketDao.addTicket(newTicket);
    		}
    	}
        
        @DELETE
    	@Path("remove")
    	@Consumes(MediaType.APPLICATION_JSON)
    	public Response deleteMovie(@QueryParam("ticketId") String ticketId) {
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
                return ticketDao.findTicketsByUserId(id);
        }
       
        //tova e za customeri da si vidqt tehnite bileti.. 
        //adminite susto mogat da hodqt na kino i da si kupuvat bileti taka 4e i te mogat da si vidqt tehnite s tozi metod
        @GET
        @Path("/mytickets")
        @Produces("application/json")
        public Collection<Ticket> getMyTickets(){
                return ticketDao.findTicketsByUserId();
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
                ticketDao.buyTicket(ticketToReserve, UserContext.getCurrentUser());
            }
            return Response.noContent().build();
        }
        
        
        
        
       
       
}
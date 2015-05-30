package morghulis.valar.dao;

import java.util.Collection;
import java.util.List;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import morghulis.valar.model.Ticket;
import morghulis.valar.model.User;
import morghulis.valar.services.UserContext;
import morghulis.valar.utils.SeatStatus;
import morghulis.valar.utils.UserType;

@Singleton
public class TicketDAO {
       
        @PersistenceContext
        private EntityManager em;
             
        @Inject
        private UserContext userContext;       
       
        public List<Ticket> getAllTickets(){
            String query = "SELECT t FROM Ticket t";
            
            return em.createQuery(query, Ticket.class).getResultList();
        }
        
        public void addTicket(Ticket ticket) {
        	em.persist(ticket);
        }
       
        public void deleteTicket(long id) {
                Ticket t = em.find(Ticket.class, id);
                if (t != null) {
                        em.remove(t);
                }
        }
       
        public Ticket findById(long key) {
                return em.find(Ticket.class, key);
        }
       
        public  Collection<Ticket> findTicketsByUserId(long... userId){
                List<Ticket> tickets = null;
                String textQuery = "select t from Ticket t where t.user.id =: userId";
               
                if(userContext.getCurrentUser().getUserType().equals(UserType.ADMINISTRATOR) && userId.length != 0){
                        TypedQuery<Ticket> query = em.createQuery(textQuery, Ticket.class).setParameter("userId", userId[0]);
                        tickets = query.getResultList();
                }
               
                else {
                        TypedQuery<Ticket> query = em.createQuery(textQuery, Ticket.class).setParameter("userId", userContext.getCurrentUser().getId());
                        tickets = query.getResultList();
                }
                return tickets;
        }
       
        public Collection<Ticket> findTicketsByScreeningId(long screeningId){
                String textQuery = "select t from Ticket t where t.screening.id =: screeningId";
                TypedQuery<Ticket> query = em.createQuery(textQuery, Ticket.class).setParameter("screeningId", screeningId);
                List<Ticket> tickets = query.getResultList();
                return tickets;
        }
//       
//        public Ticket findTicketByScreeingAndSeatNumber(long screeningId, int seatNum){
//                String textQuery = "select t from Ticket t where t.screening.id =: screeningId and t.seatNumber =: seatNum";
//                TypedQuery<Ticket> query = em.createQuery(textQuery, Ticket.class);
//                query.setParameter("screeningId", screeningId);
//                query.setParameter("seatNum", seatNum);
//                return queryTicket(query);
//        }
//       
        public void buyTicket(Ticket ticket, User userWhoBuysTicket){
               
                Ticket ticketToBuy = findById(ticket.getId());
               
                if(ticket.getStatus().equals(SeatStatus.AVAILABLE)){
                        ticketToBuy.setStatus(SeatStatus.TAKEN);
                        ticketToBuy.setUser(userWhoBuysTicket);
                        userWhoBuysTicket.getTickets().add(ticketToBuy);
                }
               
                else if(ticket.getStatus().equals(SeatStatus.RESERVED)){
                        ticketToBuy.setStatus(SeatStatus.TAKEN);
                }
               
        }
       
        public void reserveTicket(Ticket ticket, User user){
                //User userFromDB = userDao.findUserById(user.getId());
                if(ticket.getStatus().equals(SeatStatus.AVAILABLE)){
                        Ticket ticketToReserve = findById(ticket.getId());
                        ticketToReserve.setStatus(SeatStatus.RESERVED);
                        ticketToReserve.setUser(user);
                        user.getTickets().add(ticketToReserve);
                }
               
        }
       
        public void cancelReservation(long ticketId){
//                Ticket ticket = findById(ticketId);
//                if(ticket.getStatus().equals(SeatStatus.RESERVED)){
//                        ticket.setStatus(new SeatStatus(SeatStatus.AVAILABLE));
//                        User userFromDB = userDao.findByUsername((ticket.getUser().getUsername()));
//                        userFromDB.getTickets().remove(ticket);
//                }
//               
        }
        public void createNewTicket(Ticket ticket) {
                em.persist(ticket);
        }
       
        public void editTicket(Ticket ticket){
                em.merge(ticket);
        }
       
        private Ticket queryTicket(TypedQuery<Ticket> query) {
                try {
                        return query.getSingleResult();
                } catch (Exception e) {
                        return null;
                }
        }
       
}

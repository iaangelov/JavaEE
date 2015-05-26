package slbedu.library.services;


import java.net.HttpURLConnection;
import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import slbedu.library.dao.UserDAO;
import slbedu.library.model.User;
import slbedu.library.utils.UserType;

@Stateless
@Path("user")
public class UserManager {

    private static final Response RESPONSE_OK = Response.ok().build();
    
    @EJB
    private UserDAO userDAO;

    @Inject
    private UserContext userContext;

    @GET
    @Path("all")
    @Produces("application/json")
    public Collection<User> getAllUsers() {
        return this.userDAO.getAllUsers();
    }

    @GET
    @Path("type")
    @Produces("application/json")
    public UserType getCurrentUserType() {
        return userDAO.findByUsername(
                userContext.getCurrentUser().getUserName()).getUserType();
    }
    
    @Path("current")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    public String getUser() {
        if (userContext.getCurrentUser() == null) {
            return null;
        }
        return userContext.getCurrentUser().getUserName();
    }
    
    @Path("authenticated")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response isAuthenticated() {
        if (userContext.getCurrentUser() == null) {
            return Response.status(HttpURLConnection.HTTP_NOT_FOUND).build();
        }
        return RESPONSE_OK;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(User newUser) {
        System.out.println(newUser);
        if(userDAO.usernameTaken(newUser.getUserName())){
            return Response.status(HttpURLConnection.HTTP_CONFLICT).build();
        }
        newUser.setUserStatus(new UserType(UserType.CUSTOMER));
        userDAO.addUser(newUser);
        userContext.setCurrentUser(newUser);
        return RESPONSE_OK;
    }
    
   
    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response loginUser(User user) {
        boolean isUserValid = userDAO.validateUserCredentials(user.getUserName(), user.getPassword());
        if (!isUserValid) {
            return Response.status(HttpURLConnection.HTTP_UNAUTHORIZED).build();
        }
        user = userDAO.findByUsername(user.getUserName());
        userContext.setCurrentUser(user);
        return RESPONSE_OK;
    }
}

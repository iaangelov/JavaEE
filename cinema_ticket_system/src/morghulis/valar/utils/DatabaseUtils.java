package morghulis.valar.utils;

import javax.ejb.Stateless;

import morghulis.valar.model.User;

@Stateless
public class DatabaseUtils {

    private static User[] USERS = {
        new User("admin", "admin", "first.user@somemail.com", new UserType(UserType.ADMINISTRATOR)),
        new User("Second User", "Test1234", "second.user@somemail.com",
        		new UserType(UserType.CUSTOMER)),
        new User("Third User", "98411TA", "third.user@somemail.com",
        		new UserType(UserType.CUSTOMER))};
    
}

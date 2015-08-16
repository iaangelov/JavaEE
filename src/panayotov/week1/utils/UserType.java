package panayotov.week1.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserType {


	CUSTOMER("Customer"), ADMINISTRATOR("Administrator");
	
	private String text;
	
	private UserType(String text){
		this.text = text;
	}
	
	@JsonCreator
    public static UserType getType(String text) {
        
        if (text == null) {
            return null;
        }
 
        for (UserType type: UserType.values()) {
            if (text.equals(type.getText())) {
                return type;
            }
        }
        throw new IllegalArgumentException("No matching type for type " + text);
    }
 
	@JsonValue
    public String getText() {
        return text;
    }
}

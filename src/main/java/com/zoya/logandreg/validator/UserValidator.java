package com.zoya.logandreg.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import com.zoya.logandreg.models.User;

@Component
public class UserValidator implements Validator {
	
	//supports(Class<?>): Specifies that a instance of the User Domain Model can be validated with this custom validator.
	@Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }
    
	//validate(Object, Errors): Creating custom validation. We can add errors via .rejectValue(String, String).
    @Override
    public void validate(Object object, Errors errors) {
        User user = (User) object;
        
        if (!user.getPasswordConfirmation().equals(user.getPassword())) {
            
            errors.rejectValue("passwordConfirmation", "Match");
        }         
    }
}

package com.arraigntech.utility;

import java.util.Arrays;

import org.passay.DigitCharacterRule;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.UppercaseCharacterRule;
import org.passay.WhitespaceRule;
import org.springframework.stereotype.Component;


@Component
public class PasswordConstraintValidator  {

	
	public boolean isValid(final String password) {
        final PasswordValidator validator = new PasswordValidator(Arrays.asList(
            new LengthRule(8, 30), 
            new UppercaseCharacterRule(1), 
            new DigitCharacterRule(1), 
//            new SpecialCharacterRule(1), 
//            new NumericalSequenceRule(3,false),
//            new AlphabeticalSequenceRule(3,false),
//            new QwertySequenceRule(3,false),
            new WhitespaceRule()));
        final RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }
  
        return false;
    }

}

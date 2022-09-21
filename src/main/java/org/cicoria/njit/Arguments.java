package org.cicoria.njit;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class Arguments implements IParameterValidator {
    @Override
    public void validate(String name, String value) throws ParameterException {
        if (!value.equalsIgnoreCase("initiator") && !value.equalsIgnoreCase("worker")) {
            throw new ParameterException("Parameter " + name + " should be either 'initiator' or 'worker' (found " + value + ")");
        }
    }   
}

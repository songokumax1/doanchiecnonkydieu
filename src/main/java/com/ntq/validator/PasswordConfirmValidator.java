/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntq.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author songo
 */
@FacesValidator("PasswordConfirmValidator")
public class PasswordConfirmValidator implements Validator{

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object value) throws ValidatorException {
            // Retrieve the value passed to this method
        String confirmPassword = (String) value;
        // Retrieve the temporary value from the password field
        UIInput passwordInput = (UIInput) uic.findComponent("txtPass");
        String password = (String) passwordInput.getLocalValue();
        if (password == null || confirmPassword == null || !password.equals(confirmPassword)) {
            FacesMessage mgs = new FacesMessage("Mật khẩu xác minh không đúng");
            throw new ValidatorException(mgs);
        }
    }
    
}

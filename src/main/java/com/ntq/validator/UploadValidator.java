/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntq.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.Part;

/**
 *
 * @author songo
 */
@FacesValidator("UploadValidator")
public class UploadValidator implements Validator{

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object value) throws ValidatorException {
        Part p = (Part) value;
        
        if(!p.getContentType().equals("image/png") && !p.getContentType().equals("image/jpeg")
                && !p.getContentType().equals("image/pjpeg"))
        {
            FacesMessage mgs = new FacesMessage("Cần file jpg, png");
            throw new ValidatorException(mgs);
        }
        if(p.getSize() > 2097152)
        {
            FacesMessage mgs = new FacesMessage("Kích thước tối đa 2MB");
            throw new ValidatorException(mgs);
        }
    }
    
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntq.filter;

import com.ntq.pojo.User;
import com.ntq.service.UserService;
import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author songo
 */
@WebFilter(urlPatterns = {"/faces/login.xhtml","/faces/reg.xhtml"})
public class UserFilterLogin implements Filter{
    private HttpServletRequest httpRequest;
    private static UserService userSerice = new UserService();
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        httpRequest =(HttpServletRequest) request;
        
        HttpSession session = httpRequest.getSession();
        User u = (User) session.getAttribute("user");
        if (u != null){
           User usernew = userSerice.getUserById(u.getId());
            if (usernew.getDiem() != u.getDiem() || !usernew.getRole().equals(u.getRole()) || usernew.getBand() != u.getBand())
               session.setAttribute("user", usernew);
            if(usernew.getBand()==1)
               {
                    String home = "/faces/viewband.xhtml?faces-redirect=true";
                    RequestDispatcher dispatcher = httpRequest.getRequestDispatcher(home);
                    dispatcher.forward(request, response);
               }
            String home = "/faces/index.xhtml?faces-redirect=true";
            RequestDispatcher dispatcher = httpRequest.getRequestDispatcher(home);
            dispatcher.forward(request, response);
        }
        else{
                chain.doFilter(request, response);
            }
    }

    @Override
    public void destroy() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

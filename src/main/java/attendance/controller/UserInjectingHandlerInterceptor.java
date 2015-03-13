package attendance.controller;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class UserInjectingHandlerInterceptor extends HandlerInterceptorAdapter {

    @Override
    public void postHandle(
    		final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler,
            final ModelAndView modelAndView) throws Exception {

        if (modelAndView != null) {
        	
        	Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>)
        			  SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        	boolean isManager = false;
        	boolean isAdmin = false;
        	
			for (GrantedAuthority authority : authorities) {
			    isManager |= authority.getAuthority().equals("ROLE_MANAGER");
			    isAdmin |= authority.getAuthority().equals("ROLE_ADMIN");
			}
			
			ModelMap modelMap = modelAndView.getModelMap();
			
			modelMap.addAttribute("isManager", isManager);
			modelMap.addAttribute("isAdmin", isAdmin);
        }
    }

}
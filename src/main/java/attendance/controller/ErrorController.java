package attendance.controller;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping( "/" )
public class ErrorController {
	
	@RequestMapping( "/error" )
	public String error(HttpServletRequest request, HttpServletResponse response, Model model) {
		
		Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
		
		if( throwable != null )
		{
			Throwable rootThrowable = getRoot( throwable );
			StringWriter sw = new StringWriter();
			rootThrowable.printStackTrace(new PrintWriter( sw ));
			model.addAttribute("stackTrace", sw.toString());
		}
		else {
			model.addAttribute("stackTrace", "No exception");
		}
		
		return "error";
	}
	
	private static Throwable getRoot( Throwable t ) {
		if( t.getCause() != null ) {
			return getRoot( t.getCause());
		}
		else {
			return t;
		}
	}
}

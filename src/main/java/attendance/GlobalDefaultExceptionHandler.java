package attendance;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * Global exception handling.
 * 
 * @see http://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc
 * 
 */
@ControllerAdvice
class GlobalDefaultExceptionHandler {
    public static final String DEFAULT_ERROR_VIEW = "error";

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception exception) throws Exception {
        // If the exception is annotated with @ResponseStatus rethrow it and let
        // the framework handle it - like the OrderNotFoundException example
        // at the start of this post.
        // AnnotationUtils is a Spring Framework utility class.
        if (AnnotationUtils.findAnnotation(exception.getClass(), ResponseStatus.class) != null)
            throw exception;
        
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("springError");
		
		String stackTrace = null;
		if( exception != null )
		{
			Throwable rootThrowable = getRoot( exception );
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			rootThrowable.printStackTrace(pw);
			pw.flush();
			stackTrace = sw.toString();
			modelAndView.addObject("throwable", exception);
		}
		
		modelAndView.addObject("stackTrace", stackTrace);
		
		modelAndView.addObject(StandardModelKeys.USER_ERROR_MESSAGE, "Oops! An error occurred");
		
		return modelAndView;
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
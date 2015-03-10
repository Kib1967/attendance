package attendance.controller;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import attendance.model.AttendanceItem;

@Component
public class AttendanceItemValidator implements Validator {
	 
    @Override
    public boolean supports(Class<?> paramClass) {
        return AttendanceItem.class.equals(paramClass);
    }
 
    @Override
    public void validate(Object obj, Errors errors) {
        AttendanceItem item = (AttendanceItem) obj;
        
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "startDate", "Errors.AttendanceItem.StartDateRequired");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "endDate", "Errors.AttendanceItem.EndDateRequired");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "attendanceItemType", "Errors.AttendanceItem.AttendanceItemTypeRequired");
        
        Date startDate = item.getStartDate();
		Date endDate = item.getEndDate();
		
		if(startDate != null && endDate != null && startDate.after(endDate)) {
        	errors.reject("Errors.AttendanceItem.StartDateCannotBeAfterEndDate", "Start date cannot be after end date you numpty");
        }
    }
}
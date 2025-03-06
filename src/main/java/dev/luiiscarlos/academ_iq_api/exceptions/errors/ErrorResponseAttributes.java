package dev.luiiscarlos.academ_iq_api.exceptions.errors;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ErrorResponseAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) { // <- Get the error attributes
        Map<String, Object> allErrorAttributes = super.getErrorAttributes(webRequest, options);

        Map<String, Object> errorAttributes = new HashMap<>();
        int statusCode = (int) allErrorAttributes.get("status");
		errorAttributes.put("status", HttpStatus.valueOf(statusCode));
		errorAttributes.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

		String message = "";

		Throwable throwable = getError(webRequest);

		if (throwable instanceof ResponseStatusException) {
			ResponseStatusException responseStatusException = (ResponseStatusException) throwable;
			message = responseStatusException.getReason() == null ? "" : responseStatusException.getReason();
		} else {
			if (throwable.getCause() != null)
                message = throwable.getCause().getMessage() == null ? throwable.getCause().toString() : throwable.getCause().getMessage();
			else
                message = throwable.toString();
		}

		errorAttributes.put("message", message);

		return errorAttributes;
	}

}

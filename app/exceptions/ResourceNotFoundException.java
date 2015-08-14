package exceptions;

public class ResourceNotFoundException extends Exception {

	public String getMessage() {
		return "Resource not found.";
	}
}

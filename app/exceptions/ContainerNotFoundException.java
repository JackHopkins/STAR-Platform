package exceptions;

public class ContainerNotFoundException extends Exception {

	public String getMessage() {
		return "Resource not found.";
	}
}

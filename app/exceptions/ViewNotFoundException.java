package exceptions;

public class ViewNotFoundException extends Exception{

	public String getMessage() {
		return "View not found.";
	}
}


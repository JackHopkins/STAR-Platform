package utils;

public class Res {

	private static Res instance;
	
	protected Res() {
		
	}
	public Res getInstance() {
		if (instance == null) instance = new Res();
		return instance;
	}
}

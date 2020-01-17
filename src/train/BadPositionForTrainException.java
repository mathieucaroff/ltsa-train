package train;

public class BadPositionForTrainException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public BadPositionForTrainException(String name){
		super(name);
	}
	
	public String getMessage(){
		return super.getMessage();
	}
}

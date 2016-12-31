package pippin;

public class IllegalInstructionException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IllegalInstructionException(){
		super();
	}
	
	public IllegalInstructionException(String string){
		super(string);
	}
}

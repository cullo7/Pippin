package pippin;

public class CodeAccessException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public CodeAccessException(){
		super();
	}
	
	public CodeAccessException(String string){
		super(string);
	}
}

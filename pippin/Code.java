package pippin;

import java.util.Arrays;

public class Code {
	
	public static int CODE_MAX = 256;
	private long [] code = new long[CODE_MAX];
	private int nextCodeIndex = 0;

	public static long join (int opPart, int arg) {
		long longOp = opPart;
		long longArg = arg;
		// move the opcode to the upper 32 bits
		longOp = longOp << 32;
		// if arg was negative, longArg will have 32 leading 1s,
		// remove them:
		longArg = longArg & 0x00000000FFFFFFFFL;
		//join the upper 32 bits and the lower 32 bits
		return longOp | longArg;		
	}
	
	public void setCode(int op, int arg) throws CodeAccessException{
		if (nextCodeIndex == CODE_MAX){
			throw new CodeAccessException("Attempt to access code outside its bounds");
		}
		code[nextCodeIndex++] = join(op, arg);
	}
 
	public int getProgramSize(){
		return nextCodeIndex;
	}
 
 
	public int getOpPart(int i) throws CodeAccessException{
		if (i < 0 || i >= nextCodeIndex){
			throw new CodeAccessException("Attempt to access code outside its bounds");
		}
		return (int)(code[i] >> 32);
	}
	public int getArg(int i) throws CodeAccessException{
		if (i < 0 || i >= nextCodeIndex){
			throw new CodeAccessException("Attempt to access code outside its bounds");
		}
		return (int)code[i]; 
	}
 
	public void clear(){
		nextCodeIndex = 0;
		for(int i = 0;i < code.length; i++){
			code[i] = 0;
		}
	}	
	
	public String getText(int i) {
	    StringBuilder builder = new StringBuilder();
	    if(i < nextCodeIndex) {
	        builder.append(InstructionMap.mnemonics.get(getOpPart(i)/8));
	        builder.append(' ');
	        int k = getOpPart(i)%8;
	        switch(k) {
	        case 7: case 6: builder.append("&"); break;
	        case 5: case 4: builder.append("@"); break;
	        case 3: case 2: builder.append("#");
	        }
	        builder.append(getArg(i));
	    }
	    return builder.toString();
	}
}
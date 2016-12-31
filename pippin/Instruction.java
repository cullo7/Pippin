package pippin;

public interface Instruction {

	 void execute(int arg, int flags);
	 
	 static int numOnes(int k){
		 /*int count = 0;
		 String binary = Integer.toUnsignedString(k,2);
		 for(int i = 0; i < binary.length(); i++){
			 if(binary.charAt(i) == '1'){
				 count++;
			 }
		 }
		 return count;*/
		 
		 k = k - ((k >>> 1) & 0x55555555);
		 k = (k & 0x33333333) + ((k >>> 2) & 0x33333333);
		 return (((k + (k >>> 4)) & 0x0F0F0F0F) * 0x01010101) >>> 24;
	 }
	 
	 static void checkParity(int num){
		 //System.out.println(numOnes(num));
		 if(numOnes(num) % 2 == 1){
			 throw new ParityCheckException("The intstruction is corrupted");
		 }
	 }
}
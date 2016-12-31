package pippin;

public class Factorial {
		
		public static void main(String [] args){
			MachineModel test = new MachineModel();
			test.setData(0,8);
			test.setRunning(true);
			// LOD using direct addressing (flags 0) plus the parity bit
			test.setCode(1*8 + 1, 0);
			// STO, using direct addressing (parity bit is 1); arq is 1
			test.setCode(2*8 + 1, 1);
			// LOD, using direct addressing (parity bit is 1); arq is 0
			test.setCode(1*8 + 1, 0);
			// SUB, using immediate addressing (flags 2, parity bit is 1); arq is 1
			test.setCode(6*8 + 1 + 2, 1);
			// STO, using direct addressing (parity bit is 1); arq is 0
			test.setCode(2*8 + 1, 0);
			// CMPZ, flags and parity bit 0; arg is 0
			test.setCode(0xC*8, 0);
			// SUB, using immediate addressing (flags 2, parity bit is 1); arq is 1
			test.setCode(6*8 + 1 + 2, 1);
			// JMPZ using flags 0 (relative conditional jump), parity bit is 1, arg is 4
			test.setCode(4*8 + 1, 4);
			// LOD, using direct addressing (parity bit is 1); arq is 0
			test.setCode(1*8 + 1, 0);
			// MUL, using direct addressing (parity bit is 1); arq is 1
			test.setCode(7*8 + 1, 1);
			// JUMP, using flags 2 and parity bit 1; arg is 1
			test.setCode(3*8 + 1 + 2, 1);
			// HALT, using flags 0, parity bit 0 and arg = 0.
			test.setCode(0xF*8, 0);
			int result = 0;
			while(test.isRunning()) {
				if(result != test.getData(1)){
					result = test.getData(1);
					System.out.println("0 => " + test.getData(0) + 
							"; 1 => " + result);
				}
				test.step();
			}
		}
}

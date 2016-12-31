package pippin;

import java.util.Observable;

public class MachineModel extends Observable{
	 private class CPU {
		 private int accum;
		 private int pc;
	 }
	 
	 public final Instruction [] INSTRUCTIONS = new Instruction[0x10];
	 
	 private CPU cpu = new CPU();
	 private Memory memory = new Memory();
	 private boolean withGUI;
	 private Code code = new Code();
	 private boolean running = false; 
	 
	 void halt(){
		 if (!withGUI){
			 System.exit(0);
		 }
		 else{
			 running = false;
		 }
	 }
	 
	 public MachineModel(boolean bool){
		 withGUI = bool;
		 
		 INSTRUCTIONS[0x0] = (arg, flags) -> {//NOP
			 flags = flags & 0x6; // remove parity bit that will have been verified
			 if(flags != 0) {
				 String fString = "(" + (flags%8 > 3?"1":"0") + 
						 (flags%4 > 1?"1":"0") + ")";
				 throw new IllegalInstructionException(
						 "Illegal flags for this instruction: " + fString);
			 }
			 cpu.pc++;			
		 };
		 		 		 
		 INSTRUCTIONS[0x1] = (arg, flags) -> {//LOD
			 flags = flags & 0x6; // remove parity bit that will have been verified
			 if(flags == 0) { // direct addressing
				 cpu.accum = memory.getData(arg);
			 } else if(flags == 2) { // immediate addressing
				 cpu.accum = arg;
			 } else if(flags == 4) { // indirect addressing
				 cpu.accum = memory.getData(memory.getData(arg));				
			 } else { // here the illegal case is "11"
				 String fString = "(" + (flags%8 > 3?"1":"0") 
						 + (flags%4 > 1?"1":"0") + ")";
				 throw new IllegalInstructionException(
						 "Illegal flags for this instruction: " + fString);
			 }	
			 cpu.pc++;
		 };
		 		 
		 INSTRUCTIONS[0x2] = (arg, flags) -> {//STO
			 flags = flags & 0x6; // remove parity bit that will have been verified
			 if(flags == 0) { // direct addressing
				 memory.setData(arg, cpu.accum);
			 } else if(flags == 4) { // indirect addressing
				 memory.setData(memory.getData(arg), cpu.accum);				
			 } else { // here the illegal case is "11"
				 String fString = "(" + (flags%8 > 3?"1":"0") 
						 + (flags%4 > 1?"1":"0") + ")";
				 throw new IllegalInstructionException(
						 "Illegal flags for this instruction: " + fString);
			 }
			 cpu.pc++;
		 };
		 		 
		 INSTRUCTIONS[0x3] = (arg, flags) -> {//JUMP
			 flags = flags & 0x6; // remove parity bit that will have been verified
			 if(flags == 0) { // direct addressing
				 cpu.pc += arg;
			 } else if(flags == 2) { // immediate addressing
				 cpu.pc = arg;
			 } else if(flags == 4) { // indirect addressing
				 cpu.pc += memory.getData(arg);				
			 } else { // here the illegal case is "11"
				 cpu.pc = memory.getData(arg);
			 }			
		 };
		 		 
		 INSTRUCTIONS[0x4] = (arg, flags) -> {//JMPZ
			 if(cpu.accum == 0){
				 flags = flags & 0x6; // remove parity bit that will have been verified
				 if(flags == 0) { // direct addressing
					 cpu.pc += arg;
				 } else if(flags == 2) { // immediate addressing
					 cpu.pc = arg;
				 } else if(flags == 4) { // indirect addressing
					 cpu.pc += memory.getData(arg);				
				 } else { // here the illegal case is "11"
					 cpu.pc = memory.getData(arg);
				 }
			 }
			 else{
				 cpu.pc++;	
			 }
		 };
		 
		 INSTRUCTIONS[0x5] = (arg, flags) -> {//ADD
			 flags = flags & 0x6; // remove parity bit that will have been verified
			 if(flags == 0) { // direct addressing
				 cpu.accum += memory.getData(arg);
			 } else if(flags == 2) { // immediate addressing
				 cpu.accum += arg;
			 } else if(flags == 4) { // indirect addressing
				 cpu.accum += memory.getData(memory.getData(arg));				
			 } else { // here the illegal case is "11"
				 String fString = "(" + (flags%8 > 3?"1":"0") 
						 + (flags%4 > 1?"1":"0") + ")";
				 throw new IllegalInstructionException(
						 "Illegal flags for this instruction: " + fString);
			 }
			 cpu.pc++;			
		 };
		 
		 INSTRUCTIONS[0x6] = (arg, flags) -> {//SUB
			 flags = flags & 0x6; // remove parity bit that will have been verified
			 if(flags == 0) { // direct addressing
				 cpu.accum -= memory.getData(arg);
			 } else if(flags == 2) { // immediate addressing
				 cpu.accum -= arg;
			 } else if(flags == 4) { // indirect addressing
				 cpu.accum -= memory.getData(memory.getData(arg));				
			 } else { // here the illegal case is "11"
				 String fString = "(" + (flags%8 > 3?"1":"0") 
						 + (flags%4 > 1?"1":"0") + ")";
				 throw new IllegalInstructionException(
						 "Illegal flags for this instruction: " + fString);
			 }
			 cpu.pc++;			
		 };
		 
		 INSTRUCTIONS[0x7] = (arg, flags) -> {//MUL
			 flags = flags & 0x6; // remove parity bit that will have been verified
			 if(flags == 0) { // direct addressing
				 cpu.accum *= memory.getData(arg);
			 } else if(flags == 2) { // immediate addressing
				 cpu.accum *= arg;
			 } else if(flags == 4) { // indirect addressing
				 cpu.accum *= memory.getData(memory.getData(arg));				
			 } else { // here the illegal case is "11"
				 String fString = "(" + (flags%8 > 3?"1":"0") 
						 + (flags%4 > 1?"1":"0") + ")";
				 throw new IllegalInstructionException(
						 "Illegal flags for this instruction: " + fString);
			 }
			 cpu.pc++;			
		 };
		  		 
		 INSTRUCTIONS[0x8] = (arg, flags) -> {//DIV
			 flags = flags & 0x6; // remove parity bit that will have been verified
			 if(flags == 0) { // direct addressing
				 if(memory.getData(arg) == 0){
					 throw new DivideByZeroException();
				 }
				 cpu.accum /= memory.getData(arg);
			 } else if(flags == 2) { // immediate addressing
				 if(arg == 0){
					 throw new DivideByZeroException();
				 }
				 cpu.accum /= arg;
			 } else if(flags == 4) { // indirect addressing
				 if(memory.getData(memory.getData(arg)) == 0){
					 throw new DivideByZeroException();
				 }
				 cpu.accum /= memory.getData(memory.getData(arg));				
			 } else { // here the illegal case is "11"
				 String fString = "(" + (flags%8 > 3?"1":"0") 
						 + (flags%4 > 1?"1":"0") + ")";
				 throw new IllegalInstructionException(
						 "Illegal flags for this instruction: " + fString);
			 }
			 cpu.pc++;			
		 };
		 
		 INSTRUCTIONS[0x9] = (arg, flags) -> {//AND
			 flags = flags & 0x6; // remove parity bit that will have been verified
			 if(flags == 0) { // direct addressing
				 if(cpu.accum != 0 && memory.getData(arg) != 0){
					 cpu.accum = 1;
				 }
				 else{
					 cpu.accum = 0;
				 }
			 } else if(flags == 2) { // immediate addressing
				 if(cpu.accum != 0 && arg != 0){
					 cpu.accum = 1;
				 }
				 else{
					 cpu.accum = 0;
				 }
			 } else if(flags == 4) { // indirect addressing
				 throw new IllegalInstructionException();				
			 } else { // here the illegal case is "11"
				 String fString = "(" + (flags%8 > 3?"1":"0") 
						 + (flags%4 > 1?"1":"0") + ")";
				 throw new IllegalInstructionException(
						 "Illegal flags for this instruction: " + fString);
			 }
			 cpu.pc++;			
		 };
		 		 
		 INSTRUCTIONS[0xA] = (arg, flags) -> {//NOT
			 flags = flags & 0x6; // remove parity bit that will have been verified
			 if(flags != 0) {
				 String fString = "(" + (flags%8 > 3?"1":"0") + 
						 (flags%4 > 1?"1":"0") + ")";
				 throw new IllegalInstructionException(
						 "Illegal flags for this instruction: " + fString);
			 }
			 if(cpu.accum == 0){
				 cpu.accum = 1;
			 }
			 else{
				 cpu.accum = 0;
			 }
			 cpu.pc++;			
		 };

		 INSTRUCTIONS[0xB] = (arg, flags) -> {//CMPL
			 flags = flags & 0x6; // remove parity bit that will have been verified
			 if(flags != 0) {
				 String fString = "(" + (flags%8 > 3?"1":"0") + 
						 (flags%4 > 1?"1":"0") + ")";
				 throw new IllegalInstructionException(
						 "Illegal flags for this instruction: " + fString);
			 }
			 if(memory.getData(arg) < 0){
				 cpu.accum = 1;
			 }
			 else{
				 cpu.accum = 0;
			 }
			 cpu.pc++;		
		 };

		 INSTRUCTIONS[0xC] = (arg, flags) -> {//CMPZ
			 flags = flags & 0x6; // remove parity bit that will have been verified
			 if(flags != 0) {
				 String fString = "(" + (flags%8 > 3?"1":"0") + 
						 (flags%4 > 1?"1":"0") + ")";
				 throw new IllegalInstructionException(
						 "Illegal flags for this instruction: " + fString);
			 }
			 if(memory.getData(arg) == 0){
				 cpu.accum = 1;
			 }
			 else{
				 cpu.accum = 0;
			 }
			 cpu.pc++;		
		 };
		 
		 INSTRUCTIONS[0xD] = (arg, flags) -> {//FOR
			 flags = flags & 0x6; // remove parity bit that will have been verified
			 if(flags == 0) { // direct addressing
				 arg = getData(arg);
			 } else if(flags == 2) { // immediate addressing
				 //use arg				
			 } else { // here the illegal case is "11"
				 String fString = "(" + (flags%8 > 3?"1":"0") 
						 + (flags%4 > 1?"1":"0") + ")";
				 throw new IllegalInstructionException(
						 "Illegal flags for this instruction: " + fString);
			 }
			 int iterations = arg%0x1000;
			 int instructions = arg/0x1000;
			 int pc = cpu.pc;
			 if(iterations > 0 || instructions > 0){
				 for(int i = 0; i < iterations;i++){
					 cpu.pc = pc+1;
					 for(int k = 0; k < instructions;k++){		 
						 step();
					 }
				 }
			 }
		 };
		 		 
		 INSTRUCTIONS[0xF] = (arg, flags) -> {//HALT
			 flags = flags & 0x6; // remove parity bit that will have been verified
			 if(flags != 0) {
				 String fString = "(" + (flags%8 > 3?"1":"0") + 
						 (flags%4 > 1?"1":"0") + ")";
				 throw new IllegalInstructionException(
						 "Illegal flags for this instruction: " + fString);
			 }
			 halt();	
		 };
	 }
	 
	 public void setPC(int pc){
		 cpu.pc = pc;
	 }
	 
	 public MachineModel(){
		 this(false);
	 }
	 
	 public void setData(int i, int j) {
		 memory.setData(i, j);		
	 }
	 public Instruction get(int i) {
		 return INSTRUCTIONS[i];
	 }
	 public int[] getData() {
		 return memory.getData();
	 }
	 public int getData(int i){
		 return memory.getData(i);
	 }
	 public int getPC() {
		 return cpu.pc;
	 }
	 public int getAccum() {
		 return cpu.accum;
	 }
	 public void setAccum(int i) {
		 cpu.accum = i;
	 }
	 
	 public void setRunning(boolean bool){
		 running = bool;
	 }
	 
	 public boolean isRunning(){
		 return running;
	 }
	  
	 public void setCode(int op, int arg){
		 code.setCode(op, arg);
	 }
	  
	 public void clear(){
		 memory.clear();
		 code.clear();
		 cpu.accum = 0;
		 cpu.pc = 0;
	 }
	 
	 public void step(){
		 try{
			 int opPart = code.getOpPart(cpu.pc);
			 int arg = code.getArg(cpu.pc);
			 Instruction.checkParity(opPart);
			 INSTRUCTIONS[opPart/8].execute(arg, opPart%8);
		 }
		 catch (Exception e){
			 halt();
			 throw e; 
		 }
	 }
	 
	 public Code getCode(){
		 return code;
	 }
	 
	 public int getChangedIndex(){
		 return memory.getChangedIndex();
	 }
}

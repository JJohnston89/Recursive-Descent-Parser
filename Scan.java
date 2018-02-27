/*
 * This Scan Class is an implementation of an lexical analyzer for c- language.
 * The primary method is getTokens() which reads a source file line by line forming tokens, and outputs the tokens in a file named tokens.
 * 
 * Name: Joshua Johnston
 * Date: 8/27/2017
 */
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
	
  class Scan {
	
	
	private FileReader fr;
	private FileWriter fw;
	private BufferedReader br;
	private PrintWriter pw;
	private TokenRecord tokenRec;
	
	private String[] listOfKeywords = {"else", "if", "int", "float", "return", "void", "while"};
	
	
//-----------------------------------------------------------------------------------------------------------	
	public Scan(String file_name) throws IOException{             //constructor
		
		 this.fr = new FileReader(file_name);                     //created FileReader object with input source file		 
		 this.br = new BufferedReader(fr);                        //created BufferedReader object to read from FileReader
		 this.fw = new FileWriter("tokens.txt");                  //created FileWriter object to create a output file named tokens		 
		 this.pw = new PrintWriter(fw);		                      //created PrintWriter object to write the tokens to a file named tokens
		
	}
//-----------------------------------------------------------------------------------------------------------	
	
	public void getTokens() throws IOException{                  //getTokens() is the primary method of the Scan Class
		
		StringBuilder sb = new StringBuilder();                  //StringBuilder object to append token values        
		
		String currentToken = null;                          
		String tokenValue = null;
		String currentString = null;		
		
		int countDC = 0;                                         //count for multi-line comments
		int countP = 0;                                          //count for periods for float numbers
		boolean inComment = false;                               //flag for multi-line comments
		boolean stillInNum = false;
		
		
		
		while((currentString = br.readLine()) != null){          //while loop to read a line from the input source file
			
			//System.out.println("Input: " + currentString);       //echo the line
			
			
			
			for(int i = 0; i < currentString.length(); i++){	 //inner for loop to process each char	
				
				/* checking to see if it's a white space.
				 * if it is then do nothing and continue.				 * 
				 */
				 if (currentString.charAt(i) == '\n' || currentString.charAt(i) == '\t' || currentString.charAt(i) == ' ' )				
						
					continue;			
				
				/* checking the back slash '/' for three cases:
				 * case 1: the look ahead is another '/' for single line comments
				 * case 2: the look ahead is '*' for multi-line comments
				 * case 3: the inComment flag is false for token type divide
				 */
				 else if(currentString.charAt(i) == '/'){                                                                      
					if(i != currentString.length()-1 && currentString.charAt(i + 1) == '/' && inComment != true)         
						break;                                                                                           
					
					else if(i != currentString.length()-1 && currentString.charAt(i + 1) == '*'){                        
						inComment = true;             
						countDC++;
						i++;                                                       //processed the next char since look ahead has seen it
					}
					else if(inComment != true){
						currentToken = "DIVIDE";
						tokenValue = "/";
						//tokenRec = new TokenRecord(currentToken, tokenValue);       //created a TokenRecord object to hold currentToken and tokenValue
						
						//System.out.println(tokenRec.toString());                    //echo the line
						pw.println("/");                            //writing the line to output file tokens
						currentToken = null;                                        //resetting the current token
						tokenValue = null;						                    //resetting the token value
					}
				}
				
				/* checking the '*' for two cases:
				 * case 1: checking to see if it is still in a multi-line comment
				 * case 2: checking to see if the look ahead is a '/' and the count is not zero				 * 
				 */
				else if(currentString.charAt(i) == '*' && inComment == true){
					if(i != currentString.length()-1 && currentString.charAt(i + 1) == '/' && countDC != 0){						
						countDC--;                    
					    i++;                             //processed the next char since look ahead has seen it
						} 
					if(countDC == 0)                     //if the count is zero then it's out of the multi-line comment
						inComment = false;	
				}					
				
				else if(inComment == false){
					
					/* token type is ID
					 * case 1: checking if it is a letter
					 * case 2: checking if the look ahead is not a letter
					 * case 3: checking to see if the index is at the end of the line
					 */
					if(Character.isLetter(currentString.charAt(i)) && stillInNum != true){
						currentToken = "ID";                            			//if it is a letter then current token is ID
						sb.append(currentString.charAt(i));             			//append the char
						
						if(i != currentString.length() - 1 && !(Character.isLetter(currentString.charAt(i + 1)))){	 						
							
							tokenValue = sb.toString();                              	//getting the final string for token value
							sb.delete(0, sb.length());                               	//deleting everything in the StringBuilder
							
							if(isKeyword(tokenValue)){                                	//checking to see if the token value is a keyword
								currentToken = "keyword";
								pw.println(tokenValue);
							}
							else{
								pw.println(currentToken);
							}
								
							
							//tokenRec = new TokenRecord(currentToken, tokenValue);    	//created a TokenRecord object to hold currentToken and tokenValue
							
							//System.out.println(tokenRec.toString());                 	//echo the line
							//pw.println(currentToken);                         	    //writing the line to output file tokens
							currentToken = null;                                     	//resetting the current token
							tokenValue = null;                                       	//resetting the token value
							
							} 	
						
						else if(i == currentString.length() - 1 && currentToken == "ID"){     	//if it is at the end of the line
							 tokenValue = sb.toString();                                      	//get the final string for token value
							 sb.delete(0, sb.length());                                         //clear StringBuilder
							 
							 if(isKeyword(tokenValue)){                                	//checking to see if the token value is a keyword
									currentToken = "keyword";
									pw.println(tokenValue);
								}
								else{
									pw.println(currentToken);
								}
							 
							 //tokenRec = new TokenRecord(currentToken, tokenValue);              //created a TokenRecord object to hold currentToken and tokenValue
								
							 //System.out.println(tokenRec.toString());                        	//echo the line
							 //pw.println(currentToken);                                	    //writing the line to output file tokens
							 currentToken = null;                                            	//resetting the current token
							 tokenValue = null;                                              	//resetting the token value						 
						 	}
						}
					
					/* token type NUM
					 * case 1: is it a digit?
					 * case 2: is it at the end of the current string?
					 * case 3: is it a float?
					 * case 4: checking to see if the look ahead is not a digit
					 */
					else if(Character.isDigit(currentString.charAt(i))){                 //if it is a digit then current token is NUM
						currentToken = "NUM";                                            //append the digit
						sb.append(currentString.charAt(i));
						
						if(i == currentString.length() - 1 && currentToken == "NUM"){       //if the next char is end of line and the current token is NUM
							 tokenValue = sb.toString();                                    //print the token NUM
							 sb.delete(0, sb.length());
							 //tokenRec = new TokenRecord(currentToken, tokenValue);
								
							// System.out.println(tokenRec.toString());
							 pw.println(currentToken);
							 currentToken = null;
							 tokenValue = null;
							 countP = 0;
							 stillInNum = false;
							 
						 	}
						
						else if(i != currentString.length() - 1  && currentString.charAt(i + 1) == '.'              //if the next three char are in format: digit.digit
								                           		 && i != currentString.length() - 2                 //and the count is zero
								                                 && Character.isDigit(currentString.charAt(i + 2))  //then it is still token NUM
								                                 && countP == 0){								                           
							
							sb.append(currentString.charAt(i + 1));                                                 //append a period
							countP++;                                                                               //count the period
							i++;							                                                     
						 }
						
						else if(i != currentString.length() - 1 && currentString.charAt(i + 1) == 'E'){         //if we see an 'E' next after a digit then we have a float
							sb.append(currentString.charAt(i + 1));
							stillInNum = true;
							i++;
							
							if(i != currentString.length() - 1 && currentString.charAt(i + 1) != '+'                      //if the next char is not a '+', '-', or a digit
															   && currentString.charAt(i + 1) != '-'                      //then the current token is ERROR
								                               && !(Character.isDigit(currentString.charAt(i + 1)))){
								
								currentToken = "ERROR";
								sb.append(currentString.charAt(i + 1)); 
								tokenValue = sb.toString();
								sb.delete(0, sb.length());
								//tokenRec = new TokenRecord(currentToken, tokenValue);
									
								 //System.out.println(tokenRec.toString());
								 pw.println(currentToken);
								 currentToken = null;
								 tokenValue = null;
								 countP = 0;
								 stillInNum = false;
								 i++;
							}
							else if (i == currentString.length() - 1 && stillInNum == true){    //if the next char after an 'E' is end of line and the current token is NUM
								                                                                //then the current token is ERROR
								currentToken = "ERROR";
								tokenValue = sb.toString();
								sb.delete(0, sb.length());
								//tokenRec = new TokenRecord(currentToken, tokenValue);
									
								 //System.out.println(tokenRec.toString());
								 pw.println(currentToken);
								 currentToken = null;
								 tokenValue = null;
								 countP = 0;
								 stillInNum = false;				
								
							}
							
							
						}						
						
						else if(i != currentString.length() - 1 && !(Character.isDigit(currentString.charAt(i + 1)))){
							tokenValue = sb.toString();
							sb.delete(0, sb.length());
							countP = 0;
							//tokenRec = new TokenRecord(currentToken, tokenValue);
							
							//System.out.println(tokenRec.toString());
							pw.println(currentToken);
							currentToken = null;
							tokenValue = null;
							stillInNum = false;
							
						}
					}					
					
					/* token type is +
					 * 
					 * case 1: if stillInNum flag is true and the look ahead is a digit then the current token is NUM					 
					 * case 2: if stillInNum flag is false then the current token is +
					 * 
					 * stored in a TokenRecord object
					 * then echo to screen and output file tokens
					 */
					else if(currentString.charAt(i) == '+'){
						
						if(stillInNum){                                                                               
							if(i != currentString.length() - 1 && !(Character.isDigit(currentString.charAt(i + 1)))){
								sb.append(currentString.charAt(i));
								sb.append(currentString.charAt(i + 1));
								i++;
								
								currentToken = "ERROR";								
								tokenValue = sb.toString();
								sb.delete(0, sb.length());
								
								//tokenRec = new TokenRecord(currentToken, tokenValue);
								//System.out.println(tokenRec.toString());
								pw.println(currentToken);
								
								currentToken = null;
								tokenValue = null;								
								stillInNum = false;
								countP = 0;
								
								
								
							}
							else if(i == currentString.length() - 1){	
								
								sb.append(currentString.charAt(i));
								currentToken = "ERROR";								
								tokenValue = sb.toString();
								sb.delete(0, sb.length());
								
								//tokenRec = new TokenRecord(currentToken, tokenValue);
								//System.out.println(tokenRec.toString());
								pw.println(currentToken);
								
								currentToken = null;
								tokenValue = null;								
								stillInNum = false;
								countP = 0;
								
							}
							else
								sb.append(currentString.charAt(i));
								
							
						}
						else{
							
							currentToken = "PLUS";
							tokenValue = "+";
							//tokenRec = new TokenRecord(currentToken, tokenValue);
							
							//System.out.println(tokenRec.toString());
							pw.println("+");
							currentToken = null;
							tokenValue = null;		
							
						}
									
					}
					
					/* token type is -
					 * 
					 * case 1: if stillInNum flag is true and the look ahead is a digit then the current token is NUM					 
					 * case 2: if stillInNum flag is false then the current token is -
					 * 
					 * stored in a TokenRecord object
					 * then echo to screen and output file tokens
					 */					
					else if(currentString.charAt(i) == '-'){
						
						if(stillInNum){                                                                               
							if(i != currentString.length() - 1 && !(Character.isDigit(currentString.charAt(i + 1)))){
								sb.append(currentString.charAt(i));
								sb.append(currentString.charAt(i + 1));
								i++;
								
								currentToken = "ERROR";								
								tokenValue = sb.toString();
								sb.delete(0, sb.length());
								
								//tokenRec = new TokenRecord(currentToken, tokenValue);
								//System.out.println(tokenRec.toString());
								pw.println(currentToken);
								
								currentToken = null;
								tokenValue = null;								
								stillInNum = false;
								countP = 0;
								
								
								
							}
							else if(i == currentString.length() - 1){	
								
								sb.append(currentString.charAt(i));
								currentToken = "ERROR";								
								tokenValue = sb.toString();
								sb.delete(0, sb.length());
								
								//tokenRec = new TokenRecord(currentToken, tokenValue);
								//System.out.println(tokenRec.toString());
								pw.println(currentToken);
								
								currentToken = null;
								tokenValue = null;								
								stillInNum = false;
								countP = 0;
								
							}
							else
								sb.append(currentString.charAt(i));
						}
						else{
							currentToken = "MINUS";
							tokenValue = "-";
							//tokenRec = new TokenRecord(currentToken, tokenValue);
						
							//System.out.println(tokenRec.toString());
							pw.println("-");
							currentToken = null;
							tokenValue = null;
						}
					}
					
					/* token type is *
					 * stored in a TokenRecord object
					 * then echo to screen and output file tokens
					 */					
					else if(currentString.charAt(i) == '*'){
						currentToken = "MULTIPLY";
						tokenValue = "*";
						//tokenRec = new TokenRecord(currentToken, tokenValue);
						
						//System.out.println(tokenRec.toString());
						pw.println("*");
						currentToken = null;
						tokenValue = null;						
					}
					
					/* token type is <
					 * case 1: if the look ahead is '=' then token type is <=
					 * 
					 * stored in a TokenRecord object
					 * then echo to screen and output file tokens 
					 */
					
					else if(currentString.charAt(i) == '<'){
						
						if(i != currentString.length() - 1 && currentString.charAt(i + 1) == '='){
							currentToken = "LTEQ";
							tokenValue = "<=";							
							//tokenRec = new TokenRecord(currentToken, tokenValue);							
							//System.out.println(tokenRec.toString());
							pw.println("<=");
							currentToken = null;
							tokenValue = null;
							i++;
							}
						else{
							currentToken = "LT";
							tokenValue = "<";
							//tokenRec = new TokenRecord(currentToken, tokenValue);						
							//System.out.println(tokenRec.toString());
							pw.println("<");
							currentToken = null;
							tokenValue = null;
							}
					}
					
					/* token type is >
					 * case 1: if the look ahead is '=' then token type is >=
					 * 
					 * stored in a TokenRecord object
					 * then echo to screen and output file tokens 
					 */
					
					else if(currentString.charAt(i) == '>'){
						
						if(i != currentString.length() - 1 && currentString.charAt(i + 1) == '='){
							currentToken = "GTEQ";
							tokenValue = ">=";							
							//tokenRec = new TokenRecord(currentToken, tokenValue);							
							//System.out.println(tokenRec.toString());
							pw.println(">=");
							currentToken = null;
							tokenValue = null;
							i++;
							}
						else{
							currentToken = "GT";
							tokenValue = ">";
							//tokenRec = new TokenRecord(currentToken, tokenValue);						
							//System.out.println(tokenRec.toString());
							pw.println(">");
							currentToken = null;
							tokenValue = null;
							}
					}
					
					/* token type is =
					 * case 1: if the look ahead is '=' then token type is ==
					 * 
					 * stored in a TokenRecord object
					 * then echo to screen and output file tokens 
					 */
					
					else if(currentString.charAt(i) == '='){
						
						if(i != currentString.length() - 1 && currentString.charAt(i + 1) == '='){
							currentToken = "EQUALITY";
							tokenValue = "==";							
							//tokenRec = new TokenRecord(currentToken, tokenValue);							
							//System.out.println(tokenRec.toString());
							pw.println("==");
							currentToken = null;
							tokenValue = null;
							i++;
							}
						else{
							currentToken = "ASSIGNMENT";
							tokenValue = "=";
							//tokenRec = new TokenRecord(currentToken, tokenValue);						
							//System.out.println(tokenRec.toString());
							pw.println("=");
							currentToken = null;
							tokenValue = null;	
							}
					}
					
					/* token type is !=
					 * case 1: if the look ahead is '=' else the token type is error
					 * 
					 * stored in a TokenRecord object
					 * then echo to screen and output file tokens 
					 */
					
					else if(currentString.charAt(i) == '!'){
						
						if(i != currentString.length() - 1 && currentString.charAt(i + 1) == '='){
							currentToken = "NOTEQUALITY";
							tokenValue = "!=";							
							//tokenRec = new TokenRecord(currentToken, tokenValue);							
							//System.out.println(tokenRec.toString());
							pw.println("!=");
							currentToken = null;
							tokenValue = null;
							i++;
							}
						else{
							currentToken = "ERROR";						
							tokenValue = "!";
						
							//tokenRec = new TokenRecord(currentToken, tokenValue);						
							//System.out.println(tokenRec.toString());
							pw.println(currentToken);
							currentToken = null;
							tokenValue = null;
							}
					}
					
					/* token type is ; 
					 * stored in a TokenRecord object
					 * echo to screen and output file tokens
					 * reset currentToken and tokenValue
					 */					
					else if(currentString.charAt(i) == ';'){
						currentToken = "SEMICOLON";
						tokenValue = ";";
						//tokenRec = new TokenRecord(currentToken, tokenValue);
						
						//System.out.println(tokenRec.toString());
						pw.println(";");
						currentToken = null;
						tokenValue = null;						
					}
					
					/* token type is , 
					 * stored in a TokenRecord object
					 * echo to screen and output file tokens
					 * reset currentToken and tokenValue
					 */						
					else if(currentString.charAt(i) == ','){
						currentToken = "COMMA";
						tokenValue = ",";
						//tokenRec = new TokenRecord(currentToken, tokenValue);
						
						//System.out.println(tokenRec.toString());
						pw.println(",");
						currentToken = null;
						tokenValue = null;						
					}
					
					/* token type is ( 
					 * stored in a TokenRecord object
					 * echo to screen and output file tokens
					 * reset currentToken and tokenValue
					 */						
					else if(currentString.charAt(i) == '('){
						currentToken = "LP";
						tokenValue = "(";
						//tokenRec = new TokenRecord(currentToken, tokenValue);
						
						//System.out.println(tokenRec.toString());
						pw.println("(");
						currentToken = null;
						tokenValue = null;						
					}
					
					/* token type is ) 
					 * stored in a TokenRecord object
					 * echo to screen and output file tokens
					 * reset currentToken and tokenValue
					 */						
					else if(currentString.charAt(i) == ')'){
						currentToken = "RP";
						tokenValue = ")";
						//tokenRec = new TokenRecord(currentToken, tokenValue);
						
						//System.out.println(tokenRec.toString());
						pw.println(")");
						currentToken = null;
						tokenValue = null;						
					}
					
					/* token type is [ 
					 * stored in a TokenRecord object
					 * echo to screen and output file tokens
					 * reset currentToken and tokenValue
					 */						
					else if(currentString.charAt(i) == '['){
						currentToken = "LB";
						tokenValue = "[";
						//tokenRec = new TokenRecord(currentToken, tokenValue);
						
						//System.out.println(tokenRec.toString());
						pw.println("[");
						currentToken = null;
						tokenValue = null;						
					}
					
					/* token type is ] 
					 * stored in a TokenRecord object
					 * echo to screen and output file tokens
					 * reset currentToken and tokenValue
					 */						
					else if(currentString.charAt(i) == ']'){
						currentToken = "RB";
						tokenValue = "]";
						//tokenRec = new TokenRecord(currentToken, tokenValue);
						
						//System.out.println(tokenRec.toString());
						pw.println("]");
						currentToken = null;
						tokenValue = null;						
					}
					
					/* token type is { 
					 * stored in a TokenRecord object
					 * echo to screen and output file tokens
					 * reset currentToken and tokenValue
					 */						
					else if(currentString.charAt(i) == '{'){
						currentToken = "LCB";
						tokenValue = "{";
						//tokenRec = new TokenRecord(currentToken, tokenValue);
						
						//System.out.println(tokenRec.toString());
						pw.println("{");
						currentToken = null;
						tokenValue = null;						
					}
					
					/* token type is } 
					 * stored in a TokenRecord object
					 * echo to screen and output file tokens
					 * reset currentToken and tokenValue
					 */	
					
					else if(currentString.charAt(i) == '}'){
						currentToken = "RCB";
						tokenValue = "}";
						//tokenRec = new TokenRecord(currentToken, tokenValue);
						
						//System.out.println(tokenRec.toString());
						pw.println("}");
						currentToken = null;
						tokenValue = null;						
					}
					
					/* token type is error 
					 * stored in a TokenRecord object
					 * echo to screen and output file tokens
					 * reset StringBuilder, currentToken, and tokenValue
					 */	
					
					else{
						
						if(sb.length() != 0){
							tokenValue = sb.toString();
							sb.delete(0, sb.length());
							//tokenRec = new TokenRecord(currentToken, tokenValue);					
							//System.out.println(tokenRec.toString());
							pw.println(currentToken);
							currentToken = null;
							tokenValue = null;
							countP = 0;
							stillInNum = false;
						}
						else{
							currentToken = "ERROR";							
							tokenValue = Character.toString(currentString.charAt(i));						
						
							//tokenRec = new TokenRecord(currentToken, tokenValue);					
							//System.out.println(tokenRec.toString());
							pw.println(currentToken);
							currentToken = null;
							tokenValue = null;
							countP = 0;
							stillInNum = false;
						}
					}
				}//if for not in comment				
			}//end of for loop
		}//end of while loop
		
		currentToken = "EOF";    //end of file token
		//System.out.println(currentToken);
		pw.println(currentToken);
		
		pw.close();           //closing PrintWriter
		fw.close();           //closing FileWriter
		fr.close();           //closing FileReader
		br.close();           //closing BufferedReader
		sb = null;            //setting StringBuilder to null for JVM
		tokenRec = null;      //setting TokenRecord to null for JVM
		
		return;
	}// end of getTokens()
//-----------------------------------------------------------------------------------------------------------	
	private boolean isKeyword(String input){
		
		for(int i = 0; i < listOfKeywords.length; i++){
			if(listOfKeywords[i].equals(input))           //checking to see if the input is in the array of keywords
				return true;                              //if it is then return true else return false
		}
		return false;
	} //end of isKeyword()
//-----------------------------------------------------------------------------------------------------------
	
}//end of Scan Class
/////////////////////////////////////////////////////////////////////////////////////////////////////////////

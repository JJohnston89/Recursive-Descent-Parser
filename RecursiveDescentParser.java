/*
 * This RecursiveDescentParser Class is an implementation of an parser for c- language.
 * When the constructor is called it takes in a file of tokens,
 * the bufferedReader reads in the file, and the variable token gets initialize to the first token.
 * When parse() is called it calls different methods recursively following the structure of the grammar for c- language.
 * When the token reaches a leaf, and does not match the expected token then print reject else accept the token, and get the next token.
 * When the token is EOF and reaches parse() then print accept. 
 * 
 * Name: Joshua Johnston
 * Date: 9/24/2017
 */
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

	class RecursiveDescentParser {
	
	private String token;	
	private FileReader fr; 
	private BufferedReader br;	
//-----------------------------------------------------------------------------------------------------------	
	public RecursiveDescentParser() throws IOException{   //constructor
		this.fr = new FileReader("tokens.txt");           //created FileReader object with input source file
		this.br = new BufferedReader(fr);		          //created BufferedReader object to read from FileReader
		this.token = readNextToken();                     //initialize to the first token
	    
	}
//-----------------------------------------------------------------------------------------------------------	
	private String readNextToken() throws IOException{		
		
		token = br.readLine();              //reads a token from the buffer.
		
		if(token.equals("ERROR")){          //if the token is ERROR then print REJECT and exit
			System.out.println("REJECT");
			System.exit(0);
			}
		
		return token;		
	}//end of readNextToken()
//-----------------------------------------------------------------------------------------------------------	
	private void match(String expectedToken) throws IOException{
		if(token.equals(expectedToken))          //matching token to the expected token
			 token = readNextToken();            //if true then get the next token
		else{
			System.out.println("REJECT");        //else print REJECT and exit
			System.exit(0);
		}
			
	} //end of match()
//-----------------------------------------------------------------------------------------------------------	
	public void parse() throws IOException{       //beginning of the grammar
		declaration_list();                       //call the method
    
                if(!(token.equals("EOF"))){
			
			System.out.println("REJECT");
			System.exit(0);
		}
                else
		  System.out.println("ACCEPT");             //if the method returns then print accept		
		
                br.close();                               //closeing BufferedReader
		fr.close();		                          //closing the file
	} //end of parse()
//-----------------------------------------------------------------------------------------------------------	
	private void declaration_list() throws IOException{
		
		declaration();                
		declaration_list1();
		
	} // end of declaration_list()
//-----------------------------------------------------------------------------------------------------------	
	private void declaration_list1() throws IOException{
		
		if(token.equals("int") || token.equals("float") || token.equals("void")){      //if the token is a type call the method
			declaration();                                                             //else return to the previous call
			declaration_list1();
		}
		
	} //end of declaration_list1()
//-----------------------------------------------------------------------------------------------------------	
	private void declaration() throws IOException{
		
		if(token.equals("int") || token.equals("float") || token.equals("void")){	 //if the token is a type then call type-specifier		
			
			type_specifier();
			match("ID");
			declaration1();
		}
		else{
			System.out.println("REJECT");                                           //else print REJECT and exit
			System.exit(0);
		}		
	} //end of declaration()
//-----------------------------------------------------------------------------------------------------------	
	private void declaration1() throws IOException{
		
		switch(token){                    //if the token is ;, [, ( match it else print REJECT and exit
		
		case ";":
			match(";");
			break;
			
		case "[":
			match("[");
			match("NUM");
			match("]");
			match(";");
			break;
			
		case "(":
			match("(");
			params();
			match(")");
			compound_stmt();
			break;
			
		default:
			System.out.println("REJECT");
			System.exit(0);
		}
	} //declaration1()
//-----------------------------------------------------------------------------------------------------------
	private void var_declaration() throws IOException{
				
		if(token.equals("int") || token.equals("float") || token.equals("void")){   //if token is a type then call type specifier else print REJECT and exit
			type_specifier();
			match("ID");
			var_declaration1();
			
		}
		else{
			System.out.println("REJECT");
			System.exit(0);
		}
		
	} //var_declaration()
//-----------------------------------------------------------------------------------------------------------	
	private void var_declaration1() throws IOException{
		
		switch(token){               //if the token is ; or [ match it else print REJECT and exit
		
			case ";": 
					match(";");
					break;
		
			case "[": 
				 	match("[");
				 	match("NUM");
				 	match("]");
				 	match(";");
				 	break;
				 	
			default:
				System.out.println("REJECT");
				System.exit(0);
		}			
	} //var_declaration1()
//-----------------------------------------------------------------------------------------------------------	
	private void type_specifier() throws IOException{
		
		switch(token){              //if the token is int, float, or void match it
			
			case "int":
					  match("int");
					  break;
			
			case "void":
					   match("void");
				       break;
			
			case "float":
				        match("float");
				        break;
				        
			default:
				System.out.println("REJECT");              //else print REJECT and exit
				System.exit(0);
		}
	} //end of type_specifier()
//-----------------------------------------------------------------------------------------------------------	
	private void params() throws IOException{
		
		if(token.equals("void")){                     //if the type is void, match it
			match("void");
		}
		else if( token.equals("int") || token.equals("float")){    //if the type is int or float call param_list()
			param_list();
		}
		else{
			System.out.println("REJECT");                      //else print reject and exit
			System.exit(0);
		}
	} // end of params() 
//-----------------------------------------------------------------------------------------------------------	
	private void param_list() throws IOException{
		param();
		param_list1();
	} //param_list()
//-----------------------------------------------------------------------------------------------------------	
	private void param_list1() throws IOException{
		
		if(token.equals(",")){
			match(",");
			param();
			param_list1();
		}
		
	} //param_list1()
//-----------------------------------------------------------------------------------------------------------	
	private void param() throws IOException{
		
		if(token.equals("int") || token.equals("float") || token.equals("void")){   //if the token is a type then call type_specifier()
			type_specifier();
			match("ID");
			param1();
		}
		else{
			System.out.println("REJECT");                                           //else print reject and exit
			System.exit(0);
		}
		
	} //param()
//-----------------------------------------------------------------------------------------------------------	
	private void param1() throws IOException{
		
		if(token.equals("[")){     //if token is [ match it else return
			match("[");
			match("]");			
		}		
	} //end of param1()
//-----------------------------------------------------------------------------------------------------------	
	private void compound_stmt() throws IOException{
		
		match("{");              
		local_declarations();        //both local and statement can return empty
		statement_list();            
		match("}");
	} //end of compound_stmt()
//-----------------------------------------------------------------------------------------------------------	
	private void local_declarations() throws IOException{
		
		local_declarations1();
	} // end of local_declarations()
//-----------------------------------------------------------------------------------------------------------	
	private void local_declarations1() throws IOException{
		
		if(token.equals("int") || token.equals("float") || token.equals("void")){  //if token is a type call method else return
			var_declaration();
			local_declarations1();
		}
	} //end of local_declarations1()
//-----------------------------------------------------------------------------------------------------------	
	private void statement_list() throws IOException{
		
		statement_list1();
	} //end of statement_list()
//-----------------------------------------------------------------------------------------------------------	
	private void statement_list1() throws IOException{
		
		if(token.equals("ID") || token.equals(";") || token.equals("{") 
				              || token.equals("if") || token.equals("while") 
				              || token.equals("return") || token.equals("(") || token.equals("NUM")){
			statement();
			statement_list1();
		}
		
	} // end of statement_list1()
//-----------------------------------------------------------------------------------------------------------
	private void statement() throws IOException{
		
		if(token.equals("ID") || token.equals(";") || token.equals("(") || token.equals("NUM"))
			expression_stmt();
		else if(token.equals("{"))		
			compound_stmt();
		else if(token.equals("if"))
			selection_stmt();
		else if(token.equals("while"))
			iteration_stmt();
		else if(token.equals("return"))
			return_stmt();
		else{
			System.out.println("REJECT");
			System.exit(0);
		}
	} //end of statement()
//-----------------------------------------------------------------------------------------------------------	
	private void expression_stmt() throws IOException{
		
		if(token.equals(";")){			
			match(";");
		}
		else{
			expression();
			match(";");
		}
	} //end of expression_stmt()
//-----------------------------------------------------------------------------------------------------------	
	private void selection_stmt() throws IOException{
		
		match("if");
		match("(");
		expression();
		match(")");
		statement();
		selection_stmt1();
	} // end of selection_stmt() 
//-----------------------------------------------------------------------------------------------------------	
	private void selection_stmt1() throws IOException{
		
		if(token.equals("else")){
			match("else");
			statement();
		}
		
	} //end of selection_stmt1()
//-----------------------------------------------------------------------------------------------------------	
	private void iteration_stmt() throws IOException{
		match("while");
		match("(");
		expression();
		match(")");
		statement();
	} // end of iteration_stmt()
//-----------------------------------------------------------------------------------------------------------	
	private void return_stmt() throws IOException{
		
		match("return");
		return_stmt1();
	} //end of return_stmt()
//-----------------------------------------------------------------------------------------------------------	
	private void return_stmt1() throws IOException{
		
		if(token.equals(";"))
			match(";");
		else{
			expression();
			match(";");
		}
	} // end of return_stmt1()
//-----------------------------------------------------------------------------------------------------------	
	private void expression() throws IOException{		
		
		if(token.equals("ID") || token.equals("NUM") || token.equals("(")){
			
			if(token.equals("ID")){            //if the token is ID then accept it and match the next token =, [, ( or empty
				match("ID");
				
				switch(token){
					
					case "=":
						match("=");
						expression();
						break;
						
					case "[":                     //case [ can be [ expression ] or [ expression ] =
						match("[");
						expression();
						match("]");
						
						if(token.equals("=")){
							match("=");
							expression();
						}
						else {
							term1();
							additive_expression1();
							simple_expression1();
						}
						break;
					
					case "(":                      //case ( can be ( args )
						match("(");
						args();
						match(")");
						term1();
						additive_expression1();
						simple_expression1();
						break;
						
					default:                            //all three methods can return nothing
						term1();
						additive_expression1();
						simple_expression1();
				}
				
			}
			else if (token.equals("(")){               //else the token is not ID but ( then we know ( expression )
				match("(");
				expression();
				match(")");
				term1();
				additive_expression1();
				simple_expression1();
			}
			
			else if(token.equals("NUM")){               //else the token is NUM
				match("NUM");
				term1();
				additive_expression1();
				simple_expression1();
				
			}
		}		
	} // end of expression()
//-----------------------------------------------------------------------------------------------------------	
	private void var1() throws IOException{
		
		if(token.equals("[")){
			match("[");
			expression();
			match("]");
		}
	} // end of var1()
//-----------------------------------------------------------------------------------------------------------	
	private void simple_expression1() throws IOException{
		
		if(token.equals("<=") || token.equals("<") || token.equals(">") || token.equals(">=") || token.equals("==") || token.equals("!=")){
			relop();
			additive_expression();
		}
	} // end of simple_expression1()
//-----------------------------------------------------------------------------------------------------------	
	private void relop() throws IOException{
		
		switch(token){
		
			case "<=":
				match("<=");
				break;
				
			case "<":
				match("<");
				break;
				
			case ">":
				match(">");
				break;
			
			case ">=":
				match(">=");
				break;
				
			case "==":
				match("==");
				break;
				
			case "!=":
				match("!=");
				break;
				
			default:
			System.out.println("REJECT");
			System.exit(0);
		}
	} //end of relop()
//-----------------------------------------------------------------------------------------------------------	
	private void additive_expression() throws IOException{
		
		term();
		additive_expression1();
	} // end of additive_expression()
//-----------------------------------------------------------------------------------------------------------	
	private void additive_expression1() throws IOException{
		
		switch(token){
		
			case "+":
				match("+");
				term();
				additive_expression1();
				break;
				
			case "-":
				match("-");
				term();
				additive_expression1();
				break;
				
			default:
				break;
		}
		
	} //end of additive_expression1()
//-----------------------------------------------------------------------------------------------------------	
	private void term() throws IOException{
		
		factor();
		term1();
	} // end of term()
//-----------------------------------------------------------------------------------------------------------	
	private void term1() throws IOException{
		
		switch(token){
		
			case "*":
				match("*");
				factor();
				term1();
				break;
			
			case "/":
				match("/");
				factor();
				term1();
				break;
				
			default:
				break;
			}
	} // end of term1()
//-----------------------------------------------------------------------------------------------------------	
	private void factor() throws IOException{
		
		switch(token){
			case "(":
				match("(");
				expression();				
				match(")");
				break;				
			
			case "NUM":
				match("NUM");
				break;
				
			case "ID":
				match("ID");
				factor1();
				break;					
		}
		
	} // end of factor()
//-----------------------------------------------------------------------------------------------------------	
	private void factor1() throws IOException{
		
		if(token.equals("(")){
			match("(");
			args();
			match(")");
		}
		else{
			var1();
		}
	} // end of factor1()
//-----------------------------------------------------------------------------------------------------------	
	private void args() throws IOException{
		
		if(token.equals("ID") || token.equals("(") || token.equals("NUM")){
			arg_list();
		}
	} //end of args()
//-----------------------------------------------------------------------------------------------------------
	
	private void arg_list() throws IOException{
		expression();
		arg_list1();
	} //end of arg_list()
//-----------------------------------------------------------------------------------------------------------	
	private void arg_list1() throws IOException{
		
		if(token.equals(",")){
			match(",");
			expression();
			arg_list1();
		}
		
	} //end of arg_list1()
//-----------------------------------------------------------------------------------------------------------
	
} // end of RecursiveDescentParser Class
/////////////////////////////////////////////////////////////////////////////////////////////////////////////

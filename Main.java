/*
 * This Main Class is the driver of the compiler project.
 * A source file is taken by the command line and passed to the Scan object to get tokens.
 * 
 * Name: Joshua Johnston
 * Date: 8/27/2017
 * 
 *  Update 9/24/2017
 *  Parser was added to main()
 */


import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;


public class Main {

//-----------------------------------------------------------------------------------------------------------	
	public static void main(String[] args) throws IOException{		
		
		if(args.length == 0){                               //checking if a file was passed from the command line
			System.out.println("No file was entered.");     //if not then exit program
			System.out.println("Exiting the program.");
			System.exit(0);			
		}
		
		File testFile = new File(args[0]);                                     //checking to see if the file exists or not		
		if(testFile.exists() == false){                                        //if not then exit the program
			System.out.println("The file: " + args[0] + " does not exist.");
			System.out.println("Exiting the program.");
			System.exit(0);
		}
		Scan lexical = new Scan(args[0]);		
	
		lexical.getTokens();
		RecursiveDescentParser paser = new RecursiveDescentParser();
		paser.parse();
		
		
		
		
			
	return;	
	} //end of main()
//-----------------------------------------------------------------------------------------------------------
} //end of Main Class
/////////////////////////////////////////////////////////////////////////////////////////////////////////////

/*
 * This TokenRecord class holds the token type and it's attribute as a string.
 * 
 * Name: Joshua Johnston
 * Date: 8/27/2017
 */

	class TokenRecord {         
	
	private String tokenType;
	private String tokenValue;
//-----------------------------------------------------------------------------------------------------------
	public TokenRecord(String tokenType, String tokenValue){       //constructor 
		this.tokenType = tokenType;
		this.tokenValue = tokenValue;
	}
//-----------------------------------------------------------------------------------------------------------
	
	public String getTokenType(){              
		return tokenType;
	} //end of getTokenType()
//-----------------------------------------------------------------------------------------------------------
	
	public String getTokenValue(){
		return tokenValue;
	} //end of getTokenValue()
//-----------------------------------------------------------------------------------------------------------
	@Override
	public String toString(){
		
		return (tokenType + ": " + tokenValue);		
	} //end of toString()
//-----------------------------------------------------------------------------------------------------------
	
}//end of TokenRecord Class
/////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.example.bonjour;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utility {

	private BufferedReader in = null;

	public String getContentFromResponse(InputStream responseStream) throws Exception
	{
		try{
			in = new BufferedReader
		            (new InputStreamReader(responseStream));
		            StringBuffer sb = new StringBuffer("");
		            String line = "";
		            String NL = System.getProperty("line.separator");
		            while ((line = in.readLine()) != null) {
		                sb.append(line + NL);
		            }
		            in.close();
		            return sb.toString();
		}catch(Exception ex){
			throw ex;
		}
	}
    public enum Radar
    {
        NEARBY_PEOPLE_AREA,
        NEARBY_EMERGENCY_AREA,
        NEARBY_FRIENDS_AREA
    }
}



import java.awt.Color;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.*;

public class MessageBuilder
{
	public synchronized int ExtractStandardTwitterInt(String elementName, String from)
	{
		int returnValue = -1;
		
		//working pattern for text
		Pattern statusPattern = Pattern.compile("\"" + elementName + "\":(.*?),");
		
		Matcher matcher = statusPattern.matcher(from);
		if (matcher.find())
		{
			try
			{
				returnValue = Integer.parseInt(from.substring(matcher.start() + elementName.length() + 3, matcher.end() - 1));
			}
			catch (Exception ex)
			{
				//Null element found or integer could not be parsed
			}
		}
		
		return returnValue;
	}
	
	public synchronized String ExtractStandardTwitterString(String elementName, String from)
	{
		String returnValue = "";
		
		//working pattern for text
		Pattern statusPattern = Pattern.compile("\"" + elementName + "\":\"(.*?)\"");
		
		Matcher matcher = statusPattern.matcher(from);
		if (matcher.find())
		{
			returnValue = from.substring(matcher.start() + elementName.length() + 4, matcher.end() - 1);
		}
		
		return returnValue;
	}
	
	public synchronized String ExtractUserTimezone(String text)
	{
		return ExtractStandardTwitterString("timezone", text);
	}
	
	public synchronized String ExtractUserLocation(String text)
	{
		return ExtractStandardTwitterString("location", text);
	}
	
	public synchronized String ExtractGeo(String text)
	{
		return ExtractStandardTwitterString("geo", text);
	}
	
	public synchronized String ExtractSource(String text)
	{
		return ExtractStandardTwitterString("source", text);
	}
	
	public synchronized String ExtractCoordinates(String text)
	{
		return ExtractStandardTwitterString("coordinates", text);
	}
	
	public synchronized String ExtractPlace(String text)
	{
		return ExtractStandardTwitterString("place", text);
	}
	
	public synchronized int ExtractRetweetedCount(String text)
	{
		return ExtractStandardTwitterInt("retweeted_count", text);
	}
	
	public synchronized String ExtractUserProfileImageUrl(String text)
	{
		return ExtractStandardTwitterString("profile_image_url", text);
	}
	
	public synchronized String ExtractUserScreenName(String text)
	{
		String returnValue = "";
		
		//working pattern for text
		Pattern statusPattern = Pattern.compile("\"screen_name\":\"(.*?)\"");
		
		Matcher matcher = statusPattern.matcher(text);
		if (matcher.find())
		{
			returnValue = text.substring(matcher.start() + 15, matcher.end() - 1);
		}
		
		return returnValue;
	}
	
	public synchronized  boolean ExtractRetweeted(String text)
	{
		String returnValue = "";
		
		//working pattern for text
		Pattern statusPattern = Pattern.compile("\"retweeted\":(.*?),");
		
		Matcher matcher = statusPattern.matcher(text);
		if (matcher.find())
		{
			returnValue = text.substring(matcher.start() + 12, matcher.end() - 1);
		}
		
		return Boolean.parseBoolean(returnValue);
	}
	
	public synchronized String ExtractLanguage(String text)
	{
		String returnValue = "";
		
		//working pattern for text
		Pattern statusPattern = Pattern.compile("\"lang\":\"(.*?)\"");
		
		Matcher matcher = statusPattern.matcher(text);
		if (matcher.find())
		{
			returnValue = text.substring(matcher.start() + 8, matcher.end() - 1);
		}
		
		return returnValue;
	}
	
	public synchronized  String ExtractStatus(String text)
	{
		String returnValue = "";
		
		//working pattern for text
		Pattern statusPattern = Pattern.compile("\"text\":\"(.*?)\"");
		
		Matcher matcher = statusPattern.matcher(text);
		if (matcher.find())
		{
			returnValue = text.substring(matcher.start() + 8, matcher.end() - 1);
		}
		
		return returnValue;
	}
	
	public synchronized  ArrayList<String> ExtractHashTags(String text)
	{	
		ArrayList<String> returnValue = new ArrayList<String>();
		
		//Working pattern for hashtags element
		Pattern hashTagPattern = Pattern.compile("\"hashtags\":\\[\\{(.*?)\\}\\]");
		
		//working pattern for text
		Pattern textPattern = Pattern.compile("\"text\":\"(.*?)\"");
		
		//Get the hashtag element from the raw message string
		Matcher matcher = hashTagPattern.matcher(text);
		
		if (matcher.find())
		{
			String htElem = text.substring(matcher.start(), matcher.end());
			//System.out.println(htElem);
			
			//Extract individual hastags
			Matcher textMatcher = textPattern.matcher(htElem);
			while (textMatcher.find())
			{
				//System.out.println("Found Hashtag: " + htElem.substring(textMatcher.start() + 8, textMatcher.end() - 1)); 
				returnValue.add(htElem.substring(textMatcher.start() + 8, textMatcher.end() - 1));
			}
		}
		
		return returnValue;
	}
	
	public synchronized  Message parseMesage(String msg)
	{
		//System.out.println(msg);
		Message returnValue = new Message();
		
		returnValue.setText(ExtractStatus(msg));
		returnValue.setHashTags(ExtractHashTags(msg));
		returnValue.setUserLanguage(ExtractLanguage(msg));
		returnValue.setUserScreenName(ExtractUserScreenName(msg));
		returnValue.setRetweeted(ExtractRetweeted(msg));
		returnValue.setUserProfileImageUrl(ExtractUserProfileImageUrl(msg));
		returnValue.setRetweetedCount(ExtractRetweetedCount(msg));
		returnValue.setPlace(ExtractPlace(msg));
		returnValue.setGeo(ExtractGeo(msg));
		returnValue.setSource(ExtractSource(msg));
		returnValue.setCoordinates(ExtractCoordinates(msg));
		returnValue.setUserLocation(ExtractUserLocation(msg));
		returnValue.setUserTimezone(ExtractUserTimezone(msg));
		 
		return returnValue;		
	}
}
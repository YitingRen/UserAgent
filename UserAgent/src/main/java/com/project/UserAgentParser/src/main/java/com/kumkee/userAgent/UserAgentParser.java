package com.project.UserAgentParser.src.main.java.com.kumkee.userAgent;

import com.kumkee.userAgent.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserAgentParser
{

	public UserAgent parse(String userAgentString)
	{
		UserAgent userAgent = new UserAgent();
		userAgent.setBrowser(this.browser(userAgentString));
		userAgent.setVersion(this.browserVersion(userAgentString, userAgent.getBrowser()));
		userAgent.setEngine(this.engine(userAgentString));
		userAgent.setEngineVersion(engineVersion(userAgentString));
		userAgent.setOs(this.OS(userAgentString));
		userAgent.setPlatform(this.platform(userAgentString));
		userAgent.setMobile(com.kumkee.userAgent.Platform.mobilePlatforms.contains(userAgent.getPlatform()) || userAgent.getBrowser().equalsIgnoreCase(com.kumkee.userAgent.Browser.PSP));
		return userAgent;
	}

	public String engineVersion(String userAgentString)
	{
		String regexp = engine(userAgentString)+"[\\/\\- ]([\\d\\w\\.\\-]+)";
		// System.out.println("Engine Version: "+regexp);
		Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE); 
		Matcher matcher = pattern.matcher(userAgentString);
		//System.out.println(matcher.groupCount());
		
		if(matcher.find())
		{
			String version = matcher.group(1);
			// System.out.println("Verison: "+version);
			return version;
		}
		
		return null;
	}
	
	public String engine(String userAgentString)
	{
		if(matches(com.kumkee.userAgent.Engine.WebkitPattern, userAgentString))
			return com.kumkee.userAgent.Engine.Webkit;
		if(matches(com.kumkee.userAgent.Engine.KHTMLPattern, userAgentString))
			return com.kumkee.userAgent.Engine.KHTML;
		if(matches(com.kumkee.userAgent.Engine.KonquerorPattern, userAgentString))
			return com.kumkee.userAgent.Engine.Konqeror;
		if(matches(com.kumkee.userAgent.Engine.ChromePattern, userAgentString))
			return com.kumkee.userAgent.Engine.Chrome;
		if(matches(com.kumkee.userAgent.Engine.PrestoPattern, userAgentString))
			return com.kumkee.userAgent.Engine.Presto;
		if(matches(com.kumkee.userAgent.Engine.GeckoPattern, userAgentString))
			return com.kumkee.userAgent.Engine.Gecko;
		if(matches(com.kumkee.userAgent.Engine.OperaPattern, userAgentString))
			return com.kumkee.userAgent.Engine.Unknown;
		if(matches(com.kumkee.userAgent.Engine.MSIEPattern, userAgentString))
			return com.kumkee.userAgent.Engine.MSIE;
		if(matches(com.kumkee.userAgent.Engine.MIDPPattern, userAgentString))
			return com.kumkee.userAgent.Engine.MIDP;
		
		return Engine.Unknown;
	}
	
	public String platform(String userAgentString)
	{
		if(matches(com.kumkee.userAgent.Platform.WindowsPhonePattern, userAgentString))
			return "Windows Phone";
		if(matches(com.kumkee.userAgent.Platform.WindowsPattern, userAgentString))
			return "Windows";
		if(matches(com.kumkee.userAgent.Platform.MacPattern, userAgentString))
			return "Mac";
		if(matches(com.kumkee.userAgent.Platform.AndroidPattern, userAgentString))
			return "Android";
		if(matches(com.kumkee.userAgent.Platform.BlackberryPattern, userAgentString))
			return "Blackberry";
		if(matches(com.kumkee.userAgent.Platform.LinuxPattern, userAgentString))
			return "Linux";
		if(matches(com.kumkee.userAgent.Platform.WiiPattern, userAgentString))
			return "Wii";
		if(matches(com.kumkee.userAgent.Platform.PlaystationPattern, userAgentString))
			return "Playstation";
		if(matches(com.kumkee.userAgent.Platform.iPadPattern, userAgentString))
			return "iPad";
		if(matches(com.kumkee.userAgent.Platform.iPodPattern, userAgentString))
			return "iPod";
		if(matches(com.kumkee.userAgent.Platform.iPhonePattern, userAgentString))
			return "iPhone";
		if(matches(com.kumkee.userAgent.Platform.SymbianPattern, userAgentString))
			return "Symbian";
		if(matches(Platform.JavaPattern, userAgentString))
			return "Java";
		
		return "Unknown";
	}
	
	public String browser(String userAgentString)
	{
		if(matches(com.kumkee.userAgent.Browser.KonquerorPattern, userAgentString))
			return com.kumkee.userAgent.Browser.Konqueror;
		else if(matches(com.kumkee.userAgent.Browser.ChromePattern, userAgentString))
			return com.kumkee.userAgent.Browser.Chrome;
		else if(matches(com.kumkee.userAgent.Browser.SafariPattern, userAgentString))
			return com.kumkee.userAgent.Browser.Safari;
		else if(matches(com.kumkee.userAgent.Browser.OperaPattern, userAgentString))
			return com.kumkee.userAgent.Browser.Opera;
		else if(matches(com.kumkee.userAgent.Browser.PS3Pattern, userAgentString))
			return com.kumkee.userAgent.Browser.PS3;
		else if(matches(com.kumkee.userAgent.Browser.PSPPattern, userAgentString))
			return com.kumkee.userAgent.Browser.PSP;
		else if(matches(com.kumkee.userAgent.Browser.FirefoxPattern, userAgentString))
			return com.kumkee.userAgent.Browser.Firefox;
		else if(matches(com.kumkee.userAgent.Browser.LotusPattern, userAgentString))
			return com.kumkee.userAgent.Browser.Lotus;
		else if(matches(com.kumkee.userAgent.Browser.NetscapePattern, userAgentString))
			return com.kumkee.userAgent.Browser.Netscape;
		else if(matches(com.kumkee.userAgent.Browser.SeamonkeyPattern, userAgentString))
			return com.kumkee.userAgent.Browser.Seamonkey;
		else if(matches(com.kumkee.userAgent.Browser.ThumderbirdPattern, userAgentString))
			return com.kumkee.userAgent.Browser.Thunderbird;
		else if(matches(com.kumkee.userAgent.Browser.OutlookPattern, userAgentString))
			return com.kumkee.userAgent.Browser.Outlook;
		else if(matches(com.kumkee.userAgent.Browser.EvolutionPattern, userAgentString))
			return com.kumkee.userAgent.Browser.Evolution;
		else if(matches(com.kumkee.userAgent.Browser.MSIEMobilePattern, userAgentString))
			return com.kumkee.userAgent.Browser.MSIEMobile;
		else if(matches(com.kumkee.userAgent.Browser.MSIEPattern, userAgentString))
			return com.kumkee.userAgent.Browser.MSIE;
		else if(matches(com.kumkee.userAgent.Browser.BlackberryPattern, userAgentString))
			return com.kumkee.userAgent.Browser.Blackberry;
		else if(matches(com.kumkee.userAgent.Browser.GabblePattern, userAgentString))
			return com.kumkee.userAgent.Browser.Gabble;
		else if(matches(com.kumkee.userAgent.Browser.YammerDesktopPattern, userAgentString))
			return com.kumkee.userAgent.Browser.YammerDesktop;
		else if(matches(com.kumkee.userAgent.Browser.YammerMobilePattern, userAgentString))
			return com.kumkee.userAgent.Browser.YammerMobile;
		else if(matches(com.kumkee.userAgent.Browser.ApacheHTTPClientPattern, userAgentString))
			return com.kumkee.userAgent.Browser.ApacheHTTPClient;
		else
			return com.kumkee.userAgent.Browser.Unknown;
	}

	public String browserVersion(String userAgentString, String browser)
	{
		Pattern pattern; 
		
		if(browser.equalsIgnoreCase(com.kumkee.userAgent.Browser.Chrome))
		{
			pattern = BrowserVersion.ChromePattern;
		}
		else if(browser.equalsIgnoreCase(com.kumkee.userAgent.Browser.Safari))
		{
			pattern = BrowserVersion.SafariPattern;
		}
		else if(browser.equalsIgnoreCase(com.kumkee.userAgent.Browser.PS3))
		{
			pattern = BrowserVersion.PS3Pattern;
		}
		else if(browser.equalsIgnoreCase(com.kumkee.userAgent.Browser.PSP))
		{
			pattern = BrowserVersion.PSPPattern;
		}
		else if(browser.equalsIgnoreCase(com.kumkee.userAgent.Browser.Lotus))
		{
			pattern = BrowserVersion.LotusPattern;
		}
		else if(browser.equalsIgnoreCase(com.kumkee.userAgent.Browser.Blackberry))
		{
			pattern = BrowserVersion.BlackberryPattern;
		}
		else if(browser.equalsIgnoreCase(com.kumkee.userAgent.Browser.YammerDesktop))
		{
			pattern = BrowserVersion.YammerDesktopPattern;
		}
		else if(browser.equalsIgnoreCase(com.kumkee.userAgent.Browser.YammerMobile))
		{
			pattern = BrowserVersion.YammerMobilePattern;
		}
		else if(browser.equalsIgnoreCase(Browser.ApacheHTTPClient))
		{
			pattern = BrowserVersion.ApacheDesktopClientPattern;
		}
		else
		{
			// Append the Browsers name to the start of the generic "Other" regexp 
			pattern = Pattern.compile(browser + BrowserVersion.Other, Pattern.CASE_INSENSITIVE); 
		}

		Matcher matcher = pattern.matcher(userAgentString);
		if(matcher.find())
		{
			return matcher.group(1);
		}
		else
		{
			return null;
		}
	}

	public String OS(String userAgentString)
	{
		if(matches(com.kumkee.userAgent.OS.AdobeAirPattern, userAgentString))
			return replaceTokens(com.kumkee.userAgent.OS.AdobeAirPattern, userAgentString, "Adobe Air #{$1}");
		if(matches(com.kumkee.userAgent.OS.WindowsPhonePattern, userAgentString))
			return "Windows Phone";
		if(matches(com.kumkee.userAgent.OS.WindowsVistaPattern, userAgentString))
			return "Windows Vista";
		if(matches(com.kumkee.userAgent.OS.Windows7Pattern, userAgentString))
			return "Windows 7";
		if(matches(com.kumkee.userAgent.OS.Windows2003Pattern, userAgentString))
			return "Windows 2003";
		if(matches(com.kumkee.userAgent.OS.WindowsXPPattern, userAgentString))
			return "Windows XP";
		if(matches(com.kumkee.userAgent.OS.Windows2000Pattern, userAgentString))
			return "Windows 2000";
		if(matches(com.kumkee.userAgent.OS.WindowsPattern, userAgentString))
			return "Windows";
		if(matches(com.kumkee.userAgent.OS.OSXPattern, userAgentString))
			return replaceTokens(com.kumkee.userAgent.OS.OSXPattern, userAgentString, "OS X #{$1}.#{$2}");
		if(matches(com.kumkee.userAgent.OS.LinuxPattern, userAgentString))
			return "Linux";
		if(matches(com.kumkee.userAgent.OS.WiiPattern, userAgentString))
			return "Wii";
		if(matches(com.kumkee.userAgent.OS.PS3Pattern, userAgentString))
			return "Playstation 3";
		if(matches(com.kumkee.userAgent.OS.PSPPattern, userAgentString))
			return "Playstation Portable";
		if(matches(com.kumkee.userAgent.OS.YpodPattern, userAgentString))
			return replaceTokens(com.kumkee.userAgent.OS.YpodPattern, userAgentString, "iPhone OS #{$1}.#{$2}");
		if(matches(com.kumkee.userAgent.OS.YpadPattern, userAgentString))
			return replaceTokens(com.kumkee.userAgent.OS.YpadPattern, userAgentString, "iPhone OS #{$1}.#{$2}");
		if(matches(com.kumkee.userAgent.OS.YphonePattern, userAgentString))
			return replaceTokens(com.kumkee.userAgent.OS.YphonePattern, userAgentString, "iPhone OS #{$1}.#{$2}");
		if(matches(com.kumkee.userAgent.OS.IpadPattern, userAgentString))
			return replaceTokens(com.kumkee.userAgent.OS.IpadPattern, userAgentString, "iPad OS #{$1}.#{$2}");
		if(matches(com.kumkee.userAgent.OS.IphonePattern, userAgentString))
			return replaceTokens(com.kumkee.userAgent.OS.IphonePattern, userAgentString, "iPhone OS #{$1}.#{$2}");
		if(matches(com.kumkee.userAgent.OS.DarwinPattern, userAgentString))
			return "Darwin";
		if(matches(com.kumkee.userAgent.OS.JavaPattern, userAgentString))
			return replaceTokens(com.kumkee.userAgent.OS.JavaPattern, userAgentString, "Java #{$1}");
		if(matches(com.kumkee.userAgent.OS.SymbianPattern, userAgentString))
			return "Symbian OS";
		if(matches(OS.BlackBerryPattern, userAgentString))
			return "BlackBerry OS";
		
			//return "OSX";

		return "Unknown";
	}

	public boolean matches(Pattern pattern, String userAgentStr)
	{
		return pattern.matcher(userAgentStr).find();
	}

	/**
	 * Replaces the tokens within the format String with the content of the groups found within the userAgentString. 
	 * <p>
	 * Tokens within the format string are formatted as #{$i} where i is the index of the group in the pattern. 
	 * <p>
	 * TODO: I would expect that there is an easier way to do this. 
	 * 
	 * @param pattern The regexp pattern to search for groups within the userAgentString
	 * @param userAgentString
	 * @param format The string to replace the tokens within
	 * @return The format string with the tokens replaced with the groups found in the userAgentString
	 */
	public String replaceTokens(Pattern pattern, String userAgentString, String format)
	{
		// System.out.println("UserAgent: " + userAgentString);
		// System.out.println("Regexp: " + pattern.toString());
		// System.out.println("Replacement: " + format);

		Matcher m = pattern.matcher(userAgentString);	

		// Move the group content into an array
		List<String> groupContent = new ArrayList<String>();
		// System.out.println("Group Count: "+m.groupCount());

		m.find();
		for(int i=0; i<=m.groupCount(); i++)
		{
			String s = m.group(i);
			// System.out.println("--"+s);
			groupContent.add(s);
		}
		
		// Replace the tokens in the pattern 
		for(int i=0; i<groupContent.size(); i++)
		{
			String token = "#\\{\\$"+i+"\\}";
			// System.out.println("Replacing ["+token+"] with ["+groupContent.get(i)+"]");
			format = format.replaceAll(token, groupContent.get(i));
		}
		
		// System.out.println("Response: "+format);
		return format;
	}
}

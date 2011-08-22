package net.nczonline.web.cssembed;

import java.util.regex.Matcher;

import org.junit.Test;
import static junit.framework.Assert.*;

public class BackgroundImageRegexTest {

	@Test
	public void testRegex() {
		RegexTest[] positive_matches = {
				new RegexTest("No quotes", "background-image:url(image.png)", "image.png"),
				new RegexTest("Single quotes", "background-image:url('image.png')", "image.png"),
				new RegexTest("Double quotes", "background-image:url(\"image.png\")", "image.png"),
				
				new RegexTest("With semi-colon at end", "background-image:url(\"image.png\") ;", "image.png"),
				
				new RegexTest("With spaces", "background-image: url( \"image.png\" )", "image.png"),
				new RegexTest("With tabs", "background-image\t:\turl(\"image.png\")", "image.png"),
				new RegexTest("Multiple lines", "background-image:\nurl(\n\t'image.png')", "image.png"),
				
				new RegexTest("Mixed case", "Background-Image:Url('image.png')", "image.png"),
				new RegexTest("Case preserved in url", "Background-Image:Url('ImagE.png')", "ImagE.png"),
				
				new RegexTest("Forward slashes", "background-image:url(/path/to/image.png)", "/path/to/image.png"),
				new RegexTest("Back slashes", "background-image:url(\\path\\to\\image.png)", "\\path\\to\\image.png"),
				
				new RegexTest("Absolute URL", "background-image:url(http://some.server.com/path/to/image.png)", 
						"http://some.server.com/path/to/image.png"),
				
				new RegexTest("URL with port number", "background-image:url(http://some.server.com:8080/path/to/image.png)", 
						"http://some.server.com:8080/path/to/image.png"),
				
				new RegexTest("URL with query parameters", "background-image:url(http://some.server.com/path/to/image.png?key=value&key2=value2)", 
						"http://some.server.com/path/to/image.png?key=value&key2=value2"),
				
				new RegexTest("Absolute URL in double quotes", "background-image:url(\"http://some.server.com/path/to/image.png?key=value&key2=value2\")", 
						"http://some.server.com/path/to/image.png?key=value&key2=value2"),
				
				new RegexTest("Absolute URL in single quotes", "background-image:url('http://some.server.com/path/to/image.png?key=value&key2=value2')", 
						"http://some.server.com/path/to/image.png?key=value&key2=value2"),
				
				new RegexTest("Absolute URL with % encoding", "background-image:url('http://some.server.com/image.png?key=value%20with%20space')", 
						"http://some.server.com/image.png?key=value%20with%20space"),
				
		};
		
		String negative_matches[][] = {
				{"Mismatched quotes", "background-image:url(\"image.png')"},
				{"Path with spaces", "background-image:url(\"my images/image.png')"},
		};
		
		for(RegexTest test : positive_matches) {
			Matcher matcher = CssTransformer.BACKGROUND_IMAGE.matcher(test.css);
			assertTrue(test.description, matcher.matches());
			assertEquals(test.description + " - URL does not match", test.expectedUrl, matcher.group(CssTransformer.URL_GROUP));
		}
		
		for(int i=0; i<negative_matches.length; i++) {
			String message = negative_matches[i][0];
			String css = negative_matches[i][1];
			assertFalse(message, CssTransformer.BACKGROUND_IMAGE.matcher(css).matches());
		}
	}
	
	static class RegexTest {
		String description;
		String css;
		String expectedUrl;
		
		RegexTest(String description, String css, String expectedUrl) {
			this.description = description;
			this.css = css;
			this.expectedUrl = expectedUrl;
		}
	}
}

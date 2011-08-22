package net.nczonline.web.cssembed;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

public class CssTransformer {

	private static final String URL_PATTERN_REGEX = "([\\\\/\\.a-zA-Z0-9%\\?\\-_:=&]+)";
	private static final String WHITE_SPACE = "\\s*";
	private static final String OPTIONAL_QUOTE = "(['\"]{0,1})";
	private static final String MATCH_OPENING_QUOTE = "\\1";
	private static final String CASE_INSENSITIVE = "(?i)";
	private static final String OPTIONAL_SEMI_COLON = "[;]{0,1}";
	
	static final int URL_GROUP = 2;
	static final Pattern BACKGROUND_IMAGE = getBackgroundImagePattern();
	

	static final Pattern getBackgroundImagePattern() {
		StringBuilder regex = new StringBuilder();
		regex
			.append(CASE_INSENSITIVE)
			.append(rightWhiteSpace("background-image"))
			.append(rightWhiteSpace(":"))
			.append(rightWhiteSpace("url"))
			.append(rightWhiteSpace("\\("))
				.append(OPTIONAL_QUOTE)
				.append(URL_PATTERN_REGEX)
				.append(MATCH_OPENING_QUOTE)
			.append(leftWhiteSpace("\\)"))
			.append(leftWhiteSpace(OPTIONAL_SEMI_COLON));
		
		return Pattern.compile(regex.toString());
	}

	private static String leftWhiteSpace(String literal) {
		return WHITE_SPACE + literal;
	}

	private static String rightWhiteSpace(String literal) {
		return literal + WHITE_SPACE;
	}
	
	/**
	 * Reads a CSS file from input, transforms it according to transformation, and then writes the transformed css to output
	 * 
	 * This method parses the input files into 'Tokens'. For each token, the appropriate transform() method 
	 * is called on the transformation. The output of the transformation is written to the output <em>instead of</em> the token
	 * 
	 * 
	 * @param input the source css file
	 * @param t the transformation to apply on the CSS
	 * @param output the output css file
	 * @throws IOException If the input can't be read, or the output can't be written for some reason
	 */
	public void transform(Reader input, Transform t, Writer output) throws IOException {
		String source = IOUtils.toString(input);
		
		source = t.preTransform(source);
		
		/*
		 * At present, we only generate background image tokens
		 */
		Matcher m = BACKGROUND_IMAGE.matcher(source);
		StringBuffer sb = new StringBuffer();
		while(m.find()) {
			
			String url = m.group(URL_GROUP);
			BackgroundImage image = new BackgroundImage(url);
			String replacement = t.transform(image);
			m.appendReplacement(sb, replacement);
		}
		m.appendTail(sb);
		
		String finalOutput = t.postTransform(sb.toString());
		IOUtils.write(finalOutput, output);
	}
}

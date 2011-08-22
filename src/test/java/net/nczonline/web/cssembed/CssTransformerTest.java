package net.nczonline.web.cssembed;

import static junit.framework.Assert.*;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Test;

public class CssTransformerTest {

	private CssTransformer transformer = new CssTransformer();
	
	@Test
	public void testTransformationCallbacksWithoutBackgroundImages() throws IOException {
		final String css = ".someclass { font-size:small;}";
		final String expectedOutput = ".someotherclass { font-size:large;}";
		
		Transform t = new Transform() {
			
			public String transform(CssToken token) {
				throw new IllegalStateException("transform should not be called");
			}
			
			public String preTransform(String source) {
				return source.replaceAll("someclass", "someotherclass");
			}
			
			public String postTransform(String source) {
				return source.replaceAll("small", "large");
			}
		};
		
		String output = transform(css, t);
		assertEquals("Pre and Post transformation not called", expectedOutput, output);
	}
	
	@Test
	public void testMultipleBackgroundImages() throws IOException {
		final String css = ".firstclass {background-image:url('first.png')} .secondclass {background-image:url('second.png'); .thirdclass{}}";
		final String expectedOutput = ".firstclass {background-image:url('first.png?version=12');} " +
				".secondclass {background-image:url('second.png?version=12'); .thirdclass{}}";

		Transform t = new DefaultTransform() {
			@Override
			public String transform(CssToken token) {
				if(token instanceof BackgroundImage) {
					BackgroundImage image = (BackgroundImage)token;
					return "background-image:url('" + image.getUrl() + "?version=12');";
				}
				throw new IllegalStateException("Unknown token " + token);
			}
		};
		
		String output = transform(css, t);
		assertEquals(expectedOutput, output);
	}
	
	private String transform(String css, Transform t) throws IOException {
		StringWriter writer = new StringWriter();
		transformer.transform(new StringReader(css), t, writer);
		return writer.toString();
	}
	
}

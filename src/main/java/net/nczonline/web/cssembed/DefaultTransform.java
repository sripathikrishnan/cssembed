package net.nczonline.web.cssembed;

public class DefaultTransform implements Transform {

	public String preTransform(String source) {
		return source;
	}

	public String transform(CssToken token) {
		if(token instanceof BackgroundImage) {
			transform((BackgroundImage)token);
		}
		throw new IllegalArgumentException("Unknown token type - " + token.getClass());
	}
	
	protected String transform(BackgroundImage image) {
		return image.toCss();
	}

	public String postTransform(String source) {
		return source;
	}
}

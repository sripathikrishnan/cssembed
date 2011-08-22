package net.nczonline.web.cssembed;

public interface Transform {

	String preTransform(String source);
	
	String transform(CssToken token);	

	String postTransform(String source);

}

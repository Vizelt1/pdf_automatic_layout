package fr.rappa.object;

import java.awt.Font;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDPageContentStream;

import fr.rappa.properties.Constants;

public class Text {
	public int fontSize = Constants.FONT_SIZE;;
	public String content;
	public boolean bold = false;
	public boolean italic = false;
	public float x = 0;
	public float y = 0;
	
	public Text(String text) {
		this.content = text;
	}
	
	public Text(String text, boolean bold, boolean italic) {
		this.content = text;
		this.bold = bold;
		this.italic = italic;
		this.fontSize = Constants.FONT_SIZE;
	}
	
	public void setPos(float cursorX, float cursorY) {
		this.x = cursorX;
		this.y = cursorY;
	}
	
	public void renderText(PDPageContentStream contentStream) throws IOException {
		contentStream.beginText();
		contentStream.setFont(Constants.FONT_TYPE, fontSize);
		if (bold) contentStream.setFont(Constants.FONT_TYPE_BOLD, fontSize);
		if (italic) contentStream.setFont(Constants.FONT_TYPE_ITALIC, fontSize);
		if (bold && italic) contentStream.setFont(Constants.FONT_TYPE_BOLD_ITALIC, fontSize);
		contentStream.newLineAtOffset(x, y);
		contentStream.showText(content);
		contentStream.endText();
	}

	public int getCurrentFontStyle() {
		if (bold && italic)
			return Font.BOLD;
		if (bold)
			return Font.BOLD;
		if (italic)
			return Font.ITALIC;
		
		return Font.PLAIN;
	}
}

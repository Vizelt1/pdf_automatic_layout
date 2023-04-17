package fr.rappa.object;


import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDPageContentStream;

import fr.rappa.properties.Constants;

public class Title extends Text {

	public Title(String text) {
		super(text, true, false);
		fontSize = Math.round(Constants.FONT_SIZE*Constants.FONT_TITLE_MULT);
	}
	
	@Override
	public void renderText(PDPageContentStream contentStream) throws IOException {
		contentStream.beginText();
		contentStream.setFont(Constants.FONT_TYPE_BOLD, fontSize);
		contentStream.newLineAtOffset(x, y);
		contentStream.showText(content);
		contentStream.endText();

	}
}

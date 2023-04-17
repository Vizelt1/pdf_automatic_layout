package fr.rappa.properties;

import java.awt.Font;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class Constants {

	/**
	public static PDFont FONT_TYPE = PDType1Font.HELVETICA;
	public static final PDFont FONT_TYPE_BOLD = PDType1Font.HELVETICA_BOLD;
	public static final PDFont FONT_TYPE_ITALIC = PDType1Font.HELVETICA_OBLIQUE;
	public static final PDFont FONT_TYPE_BOLD_ITALIC = PDType1Font.HELVETICA_BOLD_OBLIQUE;
	**/
	
	/**
	public static PDFont FONT_TYPE = PDType1Font.HELVETICA;
	public static final PDFont FONT_TYPE_BOLD = PDType1Font.HELVETICA_BOLD;
	public static final PDFont FONT_TYPE_ITALIC = PDType1Font.HELVETICA_OBLIQUE;
	public static final PDFont FONT_TYPE_BOLD_ITALIC = PDType1Font.HELVETICA_BOLD_OBLIQUE;
	**/
	
	public static int FONT_SIZE = 12;
	
	public static PDFont FONT_TYPE = PDType1Font.TIMES_ROMAN;
	public static PDFont FONT_TYPE_BOLD = PDType1Font.TIMES_BOLD;
	public static PDFont FONT_TYPE_ITALIC = PDType1Font.TIMES_ITALIC;
	public static PDFont FONT_TYPE_BOLD_ITALIC = PDType1Font.TIMES_BOLD_ITALIC;
	
	public static Font TTF_FONT_TYPE = new Font(Constants.FONT_TYPE.getName().replaceAll("Times-Roman", "Times New Roman"), Font.PLAIN, Constants.FONT_SIZE);
	public static Font TTF_FONT_TYPE_BOLD = new Font(Constants.FONT_TYPE.getName().replaceAll("Times-Roman", "Times New Roman"), Font.BOLD, Constants.FONT_SIZE);
	public static Font TTF_FONT_TYPE_ITALIC = new Font(Constants.FONT_TYPE.getName().replaceAll("Times-Roman", "Times New Roman"), Font.ITALIC, Constants.FONT_SIZE);
	public static Font TTF_FONT_TYPE_BOLD_ITALIC = new Font(Constants.FONT_TYPE.getName().replaceAll("Times-Roman", "Times New Roman"), Font.BOLD, Constants.FONT_SIZE);
	
	
	public static final float FONT_TITLE_MULT = 1.5f;

	public static float FONT_SPACEMENT_DIV = 3f;
	public static float MARGIN_WIDTH = 75;
	public static float MARGIN_HEIGHT = 75;
	public static float PADDING = 3;
	public static float WIDTH = 595;
	public static float HEIGHT = 842;

	public static PDFont FONT_SYMBOL = PDType1Font.SYMBOL;
}

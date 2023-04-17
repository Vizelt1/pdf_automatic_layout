package fr.rappa;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class Test {


	public static void main(String[] args) throws IOException {
		int i = 0;
		
		File input = null;
		File output = null;
		while (i < args.length) {
			if (args[i].equalsIgnoreCase("-o")) {
				i++;
				output = new File(args[i]);
			} else if (args[i].equalsIgnoreCase("-help")){
				Log.info("-o <file> : output");
				Log.info("<file> : input");
				Log.info("-help : show commands");
			} else {
				input = new File(args[i]);
			}
			i++;
		}

		new PDFLinit(input, output);
	}
	
}

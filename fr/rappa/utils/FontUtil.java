package fr.rappa.utils;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

public class FontUtil {

    public static PDFont loadFont(String location) {
        PDFont font;
        try {
            PDDocument documentMock = new PDDocument();
            font = PDType0Font.load(documentMock, new File(location));
        }
        catch (IOException e) {
            throw new RuntimeException("IO exception");
        }
        return font;
    }
}

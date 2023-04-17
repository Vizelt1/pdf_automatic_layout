package fr.rappa;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import fr.rappa.object.Text;
import fr.rappa.object.Title;
import fr.rappa.properties.Constants;
import fr.rappa.utils.FontUtil;
import fr.rappa.utils.TextUtil;

public class PDFLinit {
	
	protected File input;
	protected File output;
	protected int TitleNumber = 0;
	protected int PageNumber = 0;
	protected int CurrentFontStyle = Font.PLAIN;
	protected boolean CustomTextStyleMode = false;
	private PDDocument document;
	private PDFont oldFontType;
	
	
	public PDFLinit(File input, File output) {
		this.input = input;
		this.output = output;
		try {
			readFile();
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}
	}
	
	public void exportFile() throws IOException {
		document.save(output);
		document.close();
		Log.info("done.");
	}
	
	public void readFile() throws IOException, FontFormatException {
		List<String> lines = FileUtils.readLines(input);
		int line_index = 0;
		
		document = new PDDocument();
		PDPage page = new PDPage();

		page.setMediaBox(PDRectangle.A5);
		
		Constants.WIDTH = page.getMediaBox().getWidth();
		Constants.HEIGHT = page.getMediaBox().getHeight();
		document.addPage(page);
		float cursorX = 0;
		float cursorY = 0;
		PDPageContentStream contentStream = new PDPageContentStream(document, page);
		
		
		cursorX = Constants.MARGIN_WIDTH;
		cursorY = Constants.HEIGHT - Constants.MARGIN_HEIGHT;
		
		
		AffineTransform affinetransform = new AffineTransform();     
		FontRenderContext frc = new FontRenderContext(affinetransform,true,true);     
		Font font = new Font(Constants.FONT_TYPE.getName().replaceAll("Times-Roman", "Times New Roman"), Font.PLAIN, Constants.FONT_SIZE);
		Log.info(Constants.FONT_TYPE.getName());
		PageNumber++; 
		for (String line : lines) {
			line_index++;
		
			if (line.startsWith("_")) {
				String cmd = "";
				String args_str = "";
				String[] args = new String[0];
				if (line.contains("(")) {
					cmd = line.substring(1).split("\\(")[0].toLowerCase();
					args_str = line.split("\\(")[1];
					args_str = args_str.substring(0, args_str.length()-1);
					args = args_str.split(",");
				} else {
					cmd = line.substring(1);
				}
				Log.info("COMMAND: " + cmd);
				
				switch(cmd.toLowerCase()) {
				case "margin":
					if (args.length != 2) {
						Log.err("_margin takes one argument -> _margin(width,height) [in centimeter]", line_index);
						return;
					}
					float w = Float.parseFloat(args[0])*28.3465f;
					float h = Float.parseFloat(args[1])*28.3465f;
					Constants.MARGIN_WIDTH = w;
					Constants.MARGIN_HEIGHT = h;
					
					cursorX = Constants.MARGIN_WIDTH;
					cursorY = Constants.HEIGHT - Constants.MARGIN_HEIGHT;
					
					break;
				
				case "setpagebox":
					if (args.length != 2) {
						Log.err("_setpagebox takes two arguments -> _setpagebox(width,height) [in centimeter]", line_index);
						return;
					}
					float width = Float.parseFloat(args[0])*28.3465f;
					float height = Float.parseFloat(args[1])*28.3465f;
					page.setMediaBox(new PDRectangle(0, 0, width, height));
					Constants.WIDTH = page.getMediaBox().getWidth();
					Constants.HEIGHT = page.getMediaBox().getHeight();

					cursorX = Constants.MARGIN_WIDTH;
					cursorY = Constants.HEIGHT - Constants.MARGIN_HEIGHT;
					
					break;
					
				
				case "fontsize":
					if (args.length < 1) {
						Log.err("_fontsize takes one argument -> _fontsize(integer)", line_index);
						return;
					}
					Constants.FONT_SIZE = Integer.parseInt(args[0]);
					break;
				
				case "setfont":
					if (args.length != 2) {
						Log.err("_setfont takes two arguments -> _setfont(file, type)", line_index);
						return;
					}
					
					String file = args[0].trim();
					String type = args[1].trim();
					
					switch(type.toLowerCase()) {
					case "plain":
						Constants.FONT_TYPE = FontUtil.loadFont(file);
						Constants.TTF_FONT_TYPE = Font.createFont(Font.TRUETYPE_FONT, new File(file));
						break;
					case "bold":
						Constants.FONT_TYPE_BOLD = FontUtil.loadFont(file);
						Constants.TTF_FONT_TYPE_BOLD = Font.createFont(Font.TRUETYPE_FONT, new File(file));
						break;
					case "italic":
						Constants.FONT_TYPE_ITALIC = FontUtil.loadFont(file);
						Constants.TTF_FONT_TYPE_ITALIC = Font.createFont(Font.TRUETYPE_FONT, new File(file));
						break;
					case "bolditalic":
						Constants.FONT_TYPE_BOLD_ITALIC = FontUtil.loadFont(file);
						Constants.TTF_FONT_TYPE_BOLD_ITALIC = Font.createFont(Font.TRUETYPE_FONT, new File(file));
						break;
					default:
						break;
					}
					break;
				
					
				case "title":
					if (args.length != 1) {
						Log.err("_title takes one argument -> _title(string)", line_index);
						return;
					} else {
						
						if (cursorX > 0) {
							if (cursorY - Constants.FONT_TITLE_MULT*Constants.FONT_SIZE+2*Constants.PADDING <= Constants.MARGIN_HEIGHT) {
								PDPage page1 = new PDPage(page.getMediaBox());
								document.addPage(page1);
								contentStream.close();
								contentStream = new PDPageContentStream(document, page1);
								
								float numberwidth = (float) ((font.getStringBounds(""+PageNumber, frc).getMaxX()));
								contentStream.beginText();
								contentStream.setFont(Constants.FONT_TYPE, 10);
								contentStream.newLineAtOffset(Constants.WIDTH-Constants.MARGIN_WIDTH-numberwidth, Constants.HEIGHT-Constants.MARGIN_HEIGHT/2 - 5);
								contentStream.showText("" + PageNumber);
								contentStream.endText();
								PageNumber++;
								
								cursorX = Constants.MARGIN_WIDTH;
								cursorY = Constants.HEIGHT - Constants.MARGIN_HEIGHT;
							} else {
								cursorY -= Constants.FONT_TITLE_MULT*Constants.FONT_SIZE+2*Constants.PADDING;
								cursorX = Constants.MARGIN_WIDTH;		
							}
			
						}
						
						String[] _textSplit = args[0].split(" ");
						
						for (int i = 0; i < _textSplit.length; i++) {
							
							String s = _textSplit[i];
							
							if (s.length() == 0) continue;
							s = TextUtil.replaceSpecialChar(s);

							
							Title tmp_text = new Title(s);
							font = Constants.TTF_FONT_TYPE_BOLD.deriveFont((float) tmp_text.fontSize);
							Log.info("FontSize: " + font.getSize());
							float textwidth = (float) ((font.getStringBounds(tmp_text.content, frc).getMaxX()));
							if (cursorX+textwidth+(tmp_text.fontSize/Constants.FONT_SPACEMENT_DIV) >= -Constants.MARGIN_WIDTH+Constants.WIDTH) {
								if (cursorY - Constants.FONT_TITLE_MULT*Constants.FONT_SIZE+2*Constants.PADDING <= Constants.MARGIN_HEIGHT) {
									PDPage page1 = new PDPage(page.getMediaBox());
									document.addPage(page1);
									
									contentStream.close();
									contentStream = new PDPageContentStream(document, page1);
									
									float numberwidth = (float) ((font.getStringBounds(""+PageNumber, frc).getMaxX()));
									contentStream.beginText();
									contentStream.setFont(Constants.FONT_TYPE, 10);
									contentStream.newLineAtOffset(Constants.WIDTH-Constants.MARGIN_WIDTH-numberwidth, Constants.HEIGHT-Constants.MARGIN_HEIGHT/2 - 5);
									contentStream.showText("" + PageNumber);
									contentStream.endText();
									PageNumber++;
									
									cursorX = Constants.MARGIN_WIDTH;
									cursorY = Constants.HEIGHT - Constants.MARGIN_HEIGHT;
								} else {
									cursorY -= tmp_text.fontSize+Constants.PADDING;
									cursorX = Constants.MARGIN_WIDTH;
								}
							}
							
							tmp_text.setPos(cursorX, cursorY);
							Log.info("textX:" + tmp_text.x + "; cursorX: " + cursorX);
							Log.info("textY:" + tmp_text.y + "; cursorY: " + cursorY);
							Log.info("content: " + tmp_text.content);
							tmp_text.renderText(contentStream);
							
							cursorX += textwidth+(tmp_text.fontSize/Constants.FONT_SPACEMENT_DIV);
						}						
						
					}
					
					cursorY -= Constants.FONT_TITLE_MULT*Constants.FONT_SIZE+2*Constants.PADDING;
					cursorX = Constants.MARGIN_WIDTH;

					break;
				case "lb":
					cursorY -= (Constants.FONT_SIZE+Constants.PADDING);
					cursorX = Constants.MARGIN_WIDTH;

					break;
					
				case "ntitle":
					if (args.length != 1) {
						Log.err("_ntitle takes one argument -> _ntitle(string)", line_index);
						return;
					} else {
						
						if (cursorX > 0) {
							if (cursorY - Constants.FONT_TITLE_MULT*Constants.FONT_SIZE+2*Constants.PADDING <= Constants.MARGIN_HEIGHT) {
								PDPage page1 = new PDPage(page.getMediaBox());
								document.addPage(page1);
								contentStream.close();
								contentStream = new PDPageContentStream(document, page1);
								
								float numberwidth = (float) ((font.getStringBounds(""+PageNumber, frc).getMaxX()));
								contentStream.beginText();
								contentStream.setFont(Constants.FONT_TYPE, 10);
								contentStream.newLineAtOffset(Constants.WIDTH-Constants.MARGIN_WIDTH-numberwidth, Constants.HEIGHT-Constants.MARGIN_HEIGHT/2 - 5);
								contentStream.showText("" + PageNumber);
								contentStream.endText();
								PageNumber++;
								
								cursorX = Constants.MARGIN_WIDTH;
								cursorY = Constants.HEIGHT - Constants.MARGIN_HEIGHT;
							} else {
								cursorY -= Constants.FONT_TITLE_MULT*Constants.FONT_SIZE+2*Constants.PADDING;
								cursorX = Constants.MARGIN_WIDTH;
							}

						
						}
						TitleNumber++;
						
						String _completeTitle = TitleNumber+"- " + args[0];
						String[] _textSplit = _completeTitle.split(" ");
						boolean firstChar = true;
						for (String s : _textSplit) {
							if (s.length() == 0) continue;
							s = TextUtil.replaceSpecialChar(s);
							
							Title tmp_text = new Title(s);
							font = Constants.TTF_FONT_TYPE_BOLD.deriveFont((float) tmp_text.fontSize);
							Log.info("FontSize: " + font.getSize());
							float textwidth = (float) ((font.getStringBounds(tmp_text.content, frc).getMaxX()));
							if (cursorX+textwidth+(tmp_text.fontSize/Constants.FONT_SPACEMENT_DIV) >= -Constants.MARGIN_WIDTH+Constants.WIDTH) {
								if (cursorY - Constants.FONT_TITLE_MULT*Constants.FONT_SIZE+2*Constants.PADDING <= Constants.MARGIN_HEIGHT) {
									PDPage page1 = new PDPage(page.getMediaBox());
									document.addPage(page1);
									contentStream.close();
									contentStream = new PDPageContentStream(document, page1);
									
									float numberwidth = (float) ((font.getStringBounds(""+PageNumber, frc).getMaxX()));
									contentStream.beginText();
									contentStream.setFont(Constants.FONT_TYPE, 10);
									contentStream.newLineAtOffset(Constants.WIDTH-Constants.MARGIN_WIDTH-numberwidth, Constants.HEIGHT-Constants.MARGIN_HEIGHT/2 - 5);
									contentStream.showText("" + PageNumber);
									contentStream.endText();
									PageNumber++;
									
									Log.info("NEW PAGE");
									cursorX = Constants.MARGIN_WIDTH;
									cursorY = Constants.HEIGHT - Constants.MARGIN_HEIGHT;
								} else {
									cursorY -= tmp_text.fontSize+Constants.PADDING;
									cursorX = Constants.MARGIN_WIDTH;
								}

							}
							
							tmp_text.setPos(cursorX, cursorY);
							Log.info("textX:" + tmp_text.x + "; cursorX: " + cursorX);
							Log.info("textY:" + tmp_text.y + "; cursorY: " + cursorY);
							Log.info("content: " + tmp_text.content);
							tmp_text.renderText(contentStream);
							
							cursorX += textwidth+(tmp_text.fontSize/Constants.FONT_SPACEMENT_DIV);
							if (firstChar) {
								cursorX+=2*(tmp_text.fontSize/Constants.FONT_SPACEMENT_DIV);
								firstChar = false;
							}
							
						}						
						
					}
					
					cursorY -= Constants.FONT_TITLE_MULT*Constants.FONT_SIZE+2*Constants.PADDING;
					cursorX = Constants.MARGIN_WIDTH;	
					
					break;
					
				default:
					Log.err(cmd + " not found.");
					break;
				}
			} else if (line.startsWith("$")) {
				continue;
			} else {
				String[] _textSplit = line.split(" ");
				for (int i = 0; i < _textSplit.length; i++) {
					
					String s = _textSplit[i];
					
					if (s.length() == 0) continue;
					if (s.startsWith("/")) s = s.substring(1);
					s = TextUtil.replaceSpecialChar(s);
					
					if (s.startsWith("i(")) {
						CustomTextStyleMode = true;
						CurrentFontStyle = Font.ITALIC;
						s = s.substring(2);
					}
					
					if (s.startsWith("b(")) {
						CustomTextStyleMode = true;
						CurrentFontStyle = Font.BOLD;
						s = s.substring(2);
					}
					
					if (s.startsWith("sym(")) {
						CustomTextStyleMode = true;
						CurrentFontStyle = 89;
						s = s.substring(5);
					}
					
					
					if (s.startsWith("bi(")) {
						CustomTextStyleMode = true;
						CurrentFontStyle = Font.ITALIC+Font.BOLD;
						s = s.substring(3);
					}
					
					boolean removeCustomTextStyleMode = false;
					if (s.contains(")") && CustomTextStyleMode) {
						s = s.substring(0, s.lastIndexOf(")"))+s.substring(s.lastIndexOf(")")+1);
						removeCustomTextStyleMode = true;
					}
					
					Text tmp_text = new Text(s);
					font = Constants.TTF_FONT_TYPE.deriveFont((float) tmp_text.fontSize);
					
					if (CurrentFontStyle == Font.ITALIC) {
						tmp_text.italic = true;
						font = Constants.TTF_FONT_TYPE_ITALIC.deriveFont((float) tmp_text.fontSize);
						oldFontType = Constants.FONT_TYPE;
					}
					if (CurrentFontStyle == Font.BOLD) {
						tmp_text.bold = true;
						font = Constants.TTF_FONT_TYPE_BOLD.deriveFont((float) tmp_text.fontSize);
						oldFontType = Constants.FONT_TYPE;
					}
					
					if (CurrentFontStyle == Font.BOLD+Font.ITALIC) {
						tmp_text.bold = true;
						tmp_text.italic = true;
						font = Constants.TTF_FONT_TYPE_BOLD_ITALIC.deriveFont((float) tmp_text.fontSize);
						oldFontType = Constants.FONT_TYPE;
					}
					if (CurrentFontStyle == 89) {
						oldFontType = Constants.FONT_TYPE;
						Constants.FONT_TYPE = PDType1Font.ZAPF_DINGBATS;
						
					}

					
					if (s.length() == 0) continue;
					s = TextUtil.replaceSpecialChar(s);

					
					
					float textwidth = (float) ((font.getStringBounds(tmp_text.content, frc).getMaxX()));
					if (cursorX+textwidth+(tmp_text.fontSize/Constants.FONT_SPACEMENT_DIV) >= -Constants.MARGIN_WIDTH+Constants.WIDTH) {
						if (cursorY - Constants.FONT_TITLE_MULT*Constants.FONT_SIZE+Constants.PADDING <= Constants.MARGIN_HEIGHT) {
							page = new PDPage(page.getMediaBox());
							document.addPage(page);
							contentStream.close();
							contentStream = new PDPageContentStream(document, page);
							
							float numberwidth = (float) ((font.getStringBounds(""+PageNumber, frc).getMaxX()));
							contentStream.beginText();
							contentStream.setFont(Constants.FONT_TYPE, 10);
							contentStream.newLineAtOffset(Constants.WIDTH-Constants.MARGIN_WIDTH-numberwidth, Constants.HEIGHT-Constants.MARGIN_HEIGHT/2 - 5);
							contentStream.showText("" + PageNumber);
							contentStream.endText();
							PageNumber++;
							
							cursorX = Constants.MARGIN_WIDTH;
							cursorY = Constants.HEIGHT - Constants.MARGIN_HEIGHT;
						} else {
							cursorY -= tmp_text.fontSize+Constants.PADDING;
							cursorX = Constants.MARGIN_WIDTH;
						}
					}
					
					tmp_text.setPos(cursorX, cursorY);
					Log.info("textX:" + tmp_text.x + "; cursorX: " + cursorX);
					Log.info("textY:" + tmp_text.y + "; cursorY: " + cursorY);
					Log.info("content: " + tmp_text.content);
					tmp_text.renderText(contentStream);
					
					cursorX += textwidth+(tmp_text.fontSize/Constants.FONT_SPACEMENT_DIV);
					
					
					
					
					if (removeCustomTextStyleMode) {
						CustomTextStyleMode = false;
						CurrentFontStyle = Font.PLAIN;
						Constants.FONT_TYPE = oldFontType;
					}

				}
				


			}
		}
		
		contentStream.close();
		exportFile();
		
	}
	
}

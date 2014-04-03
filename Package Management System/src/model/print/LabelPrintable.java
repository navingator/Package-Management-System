package model.print;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class LabelPrintable implements Printable {

	private String labelFileName;
	
	public LabelPrintable(String labelFileName) {
		this.labelFileName = labelFileName;
	}
	
	@Override
    public int print(Graphics graphics, PageFormat pageFormat, 
        int pageIndex) throws PrinterException {  
		
        System.out.println(pageIndex);
        
        int result = NO_SUCH_PAGE;
        
        if (pageIndex == 0) {
            // translate to avoid clipping
            graphics.translate((int) pageFormat.getImageableX(), 
                (int) pageFormat.getImageableY());   
            
			try {
				BufferedImage read = ImageIO.read(new File(labelFileName));
				
				double width = pageFormat.getImageableWidth();
				double height = pageFormat.getImageableHeight();
	            graphics.drawImage(read, 0, 0, (int) width, (int) height, null);
			} catch (IOException e) {
				e.printStackTrace();
				return result;
			}
            
            result = PAGE_EXISTS;    
        }    
        return result;    
    }
}

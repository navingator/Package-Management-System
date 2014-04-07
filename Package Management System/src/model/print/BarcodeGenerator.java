package model.print;

/*
 * Copyright 2010 Jeremias Maerki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* $Id: BarcodeGenerator.java,v 1.1 2010/10/05 08:56:04 jmaerki Exp $ */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.output.bitmap.BitmapEncoder;
import org.krysalis.barcode4j.output.bitmap.BitmapEncoderRegistry;
import org.krysalis.barcode4j.tools.UnitConv;

/**
 * Class that generates the barcode for use in printing
 * Adapted from the example code from Barcode4j
 *
 */
public class BarcodeGenerator {

    public BufferedImage getBarcode(String msg, String fullName, int dpi, FileOutputStream out) 
    		throws IOException {

        //create the barcode bean
        Code128Bean bean = new Code128Bean();

        //configure the barcode generator
        bean.setModuleWidth(UnitConv.in2mm(8.0f / dpi)); //makes a dot/module exactly eight pixels
        bean.setBarHeight(20);
        bean.doQuietZone(false);
		bean.setFontSize(4);
        bean.setPattern("____-__-__ __:__:__");

        boolean antiAlias = false;
        int orientation = 0;
        //get up the canvas provider to create a monochrome bitmap
        BitmapCanvasProvider canvas = new BitmapCanvasProvider(
                dpi, BufferedImage.TYPE_BYTE_BINARY, antiAlias, orientation);

        //generate the barcode
        bean.generateBarcode(canvas, msg);

        //signal end of generation
        canvas.finish();

        //get generated bitmap
        BufferedImage symbol = canvas.getBufferedImage();

        
        //make room for text
        int nameSize = (int) (96.0/300*dpi); //pixels
        int width = symbol.getWidth();
        int height = symbol.getHeight();
        int lineHeight = (int)(nameSize * 1.2);
        if (fullName.equals("")) {
        	lineHeight = 0;
        }
        Font font = new Font("Times", Font.PLAIN, nameSize);
        height += lineHeight;

        //add padding
        int namePadding = 40;
        height += namePadding; //pad between name and barcode

        //create bitmap
        BufferedImage bitmap = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g2d = (Graphics2D)bitmap.getGraphics();
        g2d.setBackground(Color.white);
        g2d.setColor(Color.black);
        g2d.clearRect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        g2d.setFont(font);

        //add text lines
        int y = lineHeight;
        g2d.drawString(fullName, 0, y);
        
        //place the barcode symbol
        AffineTransform symbolPlacement = new AffineTransform();
        symbolPlacement.translate(0, lineHeight + namePadding);
        g2d.drawRenderedImage(symbol, symbolPlacement);
        g2d.dispose();

        //encode bitmap as file
        String mime = "image/png";
        try {
            final BitmapEncoder encoder = BitmapEncoderRegistry.getInstance(mime);
            encoder.encode(bitmap, out, mime, dpi);
        } finally {
            out.close();
        }
        
        return bitmap;
        
    }

    /**
     * Command-line program.
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
    	
    	File outputFile = new File("testFiles/barcode.png");
    	
        try {
        	FileOutputStream out = new FileOutputStream(outputFile);
            BarcodeGenerator app = new BarcodeGenerator();
            int dpi = 300;
            app.getBarcode("--Navin Pathak--","Navin Pathak",dpi,out);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

}

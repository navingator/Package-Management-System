package model.print;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.print.PrintService;





public class LabelPrinter {

	private BarcodeGenerator bcgen;
	private PrintService service;
	
	public LabelPrinter() {
		this.bcgen = new BarcodeGenerator();
		
	}
	
	// function to print the label
	public void printLabel(String packageID, String ownerName) {
		
		String tempBarcodeFileName = "testFiles/barcode.png";
		int dpi = 300;
		
		try {
			File tempFile = new File(tempBarcodeFileName);
			FileOutputStream outStream = new FileOutputStream(tempFile);
			bcgen.getBarcode(packageID, ownerName, dpi, outStream);//, printableArea);
			sendToPrinter(tempBarcodeFileName);
			tempFile.delete();
			
		} catch (IOException e) {
			// TODO Add other stuff
			System.out.println("Failed to generate barcode.");
			e.printStackTrace();
		}
		
	}
	
	private void sendToPrinter(String tempBarcodeFileName) {
		PrinterJob pj = PrinterJob.getPrinterJob();
        if (service == null) {
        	return;
        }
    	try {
			pj.setPrintService(service);
		} catch (PrinterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        PageFormat pf = pj.defaultPage();
        Paper paper = pf.getPaper();    
        double width = mm2Pixels(54);
        double height = mm2Pixels(101);    
        paper.setSize(width, height);
        paper.setImageableArea(
                        mm2Pixels(2.5), 
                        mm2Pixels(10), 
                        width - mm2Pixels(5), 
                        height - mm2Pixels(20));                
        System.out.println("Before- " + dump(paper));    
        pf.setOrientation(PageFormat.LANDSCAPE);
        pf.setPaper(paper);    
        System.out.println("After- " + dump(paper));
        System.out.println("After- " + dump(pf));                
        dump(pf);    
        PageFormat validatePage = pj.validatePage(pf);
        System.out.println("Valid- " + dump(validatePage));                

        pj.setPrintable(new LabelPrintable(tempBarcodeFileName), pf);
        try {
            pj.print();
        } catch (PrinterException ex) {
            ex.printStackTrace();
        }  
	}
	
	protected static String dump(PageFormat pf) {    
        Paper paper = pf.getPaper();            
        return dump(paper);    
    }
	
	protected static String dump(Paper paper) {            
        StringBuilder sb = new StringBuilder(64);
        sb.append(paper.getWidth()).append("x").append(paper.getHeight())
           .append("/").append(paper.getImageableX()).append("x").
           append(paper.getImageableY()).append(" - ").append(paper
       .getImageableWidth()).append("x").append(paper.getImageableHeight());            
        return sb.toString();            
    }
	
	private double mm2Pixels(double mm) {
		return in2Pixels(mm*0.0393700787);
	}
	
	private double in2Pixels(double inch) {
		return inch * 72d;
	}


	private void setDefaultPrinter(PrintService service) {
		this.service = service;
	}
	
	private void changePrinter() {
		PrintService[] services = PrinterJob.lookupPrintServices();
		
		ArrayList<String> serviceNames = new ArrayList<String>();
		for (PrintService pservice: services) {
			System.out.println(pservice);
			serviceNames.add(pservice.getName());
		}
		//TODO get printer from view
		if(services.length != 0) {
			service = services[2];
		}
//		if(serviceNames.size() > 0) {
//			int sIndex = viewAdaptor.getChoiceFromList(serviceNames);
//			service = services[sIndex]);
//		}
	}
	
	public void start() {
		
	}
	//TODO
	
	public static void main(String[] args) {
		LabelPrinter lp = new LabelPrinter();
		lp.start();
		lp.changePrinter();
		lp.printLabel("Ambi Bobmanuel","Ambi Bobmanuel");
	}
}

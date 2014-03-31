package model.print;

import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.print.PrintService;
import util.PropertyHandler;
import model.IModelToViewAdaptor;


public class LabelPrinter {

	private BarcodeGenerator bcgen;
	private PrintService service;
	
	private IModelToViewAdaptor viewAdaptor;
	private PropertyHandler propHandler;
	private Logger logger; 
	
	public LabelPrinter(IModelToViewAdaptor viewAdaptor) {
		this.bcgen = new BarcodeGenerator();
		this.viewAdaptor = viewAdaptor;
		this.propHandler = PropertyHandler.getInstance();
		this.logger = Logger.getLogger(LabelPrinter.class.getName());
	}
	
	/**
	 * This function will generate a barcode and print a label using
	 * the previously set printer
	 * @param packageID			ID of the package for which barcode will be printed
	 * @param ownerName			Name of the owner of the package
	 */
	public void printLabel(String packageID, String ownerName) {
		
		String progDirName = propHandler.getProperty("program_directory");
		String tempBarcodeFileName = progDirName + "/barcode.png";
		int dpi = 300;
		
		try {
			File tempFile = new File(tempBarcodeFileName);
			FileOutputStream outStream = new FileOutputStream(tempFile);
			bcgen.getBarcode(packageID, ownerName, dpi, outStream);
			sendToPrinter(tempBarcodeFileName);
			tempFile.delete();
			
		} catch (IOException e) {
			// Log and display warning
			logger.warning("Failed to generate barcode in file: " + tempBarcodeFileName);
			viewAdaptor.displayWarning("Failed to generate barcode. Barcode will not be printed. \n"
					+ "Please reprint the barcode from the admin panel.", 
					"Barcode");
		}
	}
	
	/**
	 * Returns a list of all of the printers
	 * @return					Array of printer names
	 */
	public String[] getPrinterNames() {
		
		// lookup printers
		PrintService[] services = PrinterJob.lookupPrintServices();
		
		//
		ArrayList<String> serviceNames = new ArrayList<String>();
		for (PrintService pservice: services) {
			System.out.println(pservice);
			serviceNames.add(pservice.getName());
		}
		
		return (String[]) serviceNames.toArray();
	}
	
	/**
	 * Sets the printer by changing the set service and setting it
	 * on the PropertyHandler
	 * @param printerName		Name of the printer to be set
	 */
	public void setPrinter(String printerName) {
		PrintService[] services = PrinterJob.lookupPrintServices();
		
		boolean printerFound = false;
		for (PrintService pservice: services) {
			if (pservice.getName().equals(printerName)) {
				service = pservice;
				if(!pservice.getName().equals(propHandler.getProperty("printer.printer_name"))) {
					propHandler.setProperty("printer.printer_name", pservice.getName());
				}
				printerFound = true;
				break;
			}
		}
		
		if(!printerFound) {
			//TODO
		}
	}
	
	/**
	 * Send a barcode file to the printer
	 * @param tempBarcodeFileName	Name of the barcode file		
	 */
	private void sendToPrinter(String tempBarcodeFileName) {
		PrinterJob pj = PrinterJob.getPrinterJob();
		
		//TODO Throw a warning to the view
		//TODO Throws PrinterException
		//TODO Make the calling function handle exceptions
		
		//TODO This is the only case that should be handled
        if (service == null) {
        	return;
        }
    	try {
			pj.setPrintService(service);
		} catch (PrinterException e) {
			// TODO Log and send a warning to the view
			e.printStackTrace();
		}
    	
    	// set Paper and PageFormat settings
        PageFormat pf = pj.defaultPage();
        Paper paper = pf.getPaper();    
        double width = mm2Pixels(54);
        double height = mm2Pixels(101);    
        paper.setSize(width, height);
        paper.setImageableArea(
                        mm2Pixels(0), 
                        mm2Pixels(10), 
                        width - mm2Pixels(7.5), 
                        height - mm2Pixels(20));                
        System.out.println("Before- " + dump(paper));    
        pf.setOrientation(PageFormat.LANDSCAPE);
        pf.setPaper(paper);    
        System.out.println("After- " + dump(paper));
        System.out.println("After- " + dump(pf));                
        dump(pf);    
        PageFormat validatePage = pj.validatePage(pf);
        System.out.println("Valid- " + dump(validatePage));                

        // Try to print the label
        pj.setPrintable(new LabelPrintable(tempBarcodeFileName), pf);
        try {
            pj.print();
        } catch (PrinterException ex) {
        	// Log and send a warning to the view
        	logger.warning("Printer failed to print label.");
        	viewAdaptor.displayWarning("Printer failed to print label.\n"
        			+ "Please reprint label from Admin panel.", 
        			"Reprint Label");
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

	private void getPrinterFromView() {
		// TODO Consider replacing with function
		setPrinter(viewAdaptor.getChoiceFromList("Please choose a printer from the list below:", 
				"Change Printer", getPrinterNames()));
	}
	
	public void start() {
		// Load printer from properties
		String printerName = propHandler.getProperty("print.printer_name");
		
		// Check if the printer currently exists
		boolean printerFound = false;
		if(printerName != null) {
			String[] printerNames = getPrinterNames(); 
			for(String pn: printerNames) {
				if(printerName.equals(pn)) {
					setPrinter(printerName);
					printerFound = true;
				}
			}
		}

		// If the printer is not found, get from view
		if(!printerFound) {
			getPrinterFromView();
		}
		
	}

	
	public static void main(String[] args) {
		LabelPrinter lp = new LabelPrinter(null);
		PropertyHandler.getInstance().init("testfiles");
		lp.setPrinter("PrimoPDF");
		lp.printLabel("0123456789ABCDEF","Christopher Weldon Henderson");
	}
}

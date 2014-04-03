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
	 * @return					Success state of printing the label
	 */
	public boolean printLabel(String packageID, String ownerName) {
		
		// If the printer is not selected, warn the user and do not print label
		if (service == null) {
			logger.info("Printer was not selected and barcode was not printed");
			viewAdaptor.displayMessage("You have not selected a printer. Barcode will not be printed. \n"
					+ "Please choose a printer and reprint the barcode from the admin panel", "Barcode");
			return false;
		}
		
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
			return false;
		}
		
		return true;
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
	 * @return					True if the printer was found and is set
	 */
	public boolean setPrinter(String printerName) {
		PrintService[] services = PrinterJob.lookupPrintServices();
		
		// loop through printers until the printer name is found
		for (PrintService pservice: services) {
			if (pservice.getName().equals(printerName)) {
				service = pservice;
				if(!pservice.getName().equals(propHandler.getProperty("printer.printer_name"))) {
					propHandler.setProperty("printer.printer_name", pservice.getName());
				}
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Send a barcode file to the printer
	 * @param tempBarcodeFileName	Name of the barcode file		
	 */
	private void sendToPrinter(String tempBarcodeFileName) {
		
		PrinterJob pj = PrinterJob.getPrinterJob();
    	try {
			pj.setPrintService(service);
		} catch (PrinterException e) {
			logger.info("Printer was not found and barcode was not printed");
			viewAdaptor.displayMessage("Selected printer was not found. The barcode will not be printed. \n"
					+ "Please choose a printer and reprint the barcode from the admin panel", "Barcode");
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
        pf.setOrientation(PageFormat.LANDSCAPE);
        pf.setPaper(paper);                

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
	
	private double mm2Pixels(double mm) {
		return in2Pixels(mm*0.0393700787);
	}
	
	private double in2Pixels(double inch) {
		return inch * 72d;
	}

	private void getPrinterFromView() {
		setPrinter(viewAdaptor.getChoiceFromList("Please choose a printer from the list below:", 
				"Change Printer", getPrinterNames()));
	}
	
	public void start() {
		// Load printer from properties
		String printerName = propHandler.getProperty("print.printer_name");
		
		// Check if the printer currently exists
		boolean printerFound = false;
		if(printerName != null) {
			printerFound = setPrinter(printerName);
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

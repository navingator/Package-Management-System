package model.print;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.Attribute;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.Size2DSyntax;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PrinterIsAcceptingJobs;
import javax.print.attribute.standard.PrinterResolution;





public class LabelPrinter {

	private BarcodeGenerator bcgen;
	private PrintService service;
	private DocFlavor docFlavor;
	private PrintRequestAttributeSet attributeSet;
	
	public LabelPrinter() {
		this.bcgen = new BarcodeGenerator();
		this.docFlavor = DocFlavor.INPUT_STREAM.PNG;
		this.attributeSet = new HashPrintRequestAttributeSet();
		
		// define parameters for 300dpi on Standard Shipping paper
//		MediaSize ms = new MediaSize(54,101,MediaSize.MM);
		//attributeSet.add(new MediaSize(54,101,MediaSize.MM));
		attributeSet.add(new PrinterResolution(300,300,PrinterResolution.DPI));
		attributeSet.add(OrientationRequested.LANDSCAPE);
		//attributeSet.add(new MediaPrintableArea(2, 4, 50, 93, MediaPrintableArea.MM)); TODO
	}
	
	// function to print the label
	public void printLabel(String packageID, String ownerName) {
		
		PrintServiceAttributeSet aSet = service.getAttributes();
		PrinterResolution pr = (PrinterResolution) attributeSet.get(PrinterResolution.class);
		//MediaPrintableArea mpa = (MediaPrintableArea) attributeSet.get(MediaPrintableArea.class);
		
		int[] dpi = pr.getResolution(PrinterResolution.DPI);
		//float[] printableArea = mpa.getPrintableArea(MediaPrintableArea.INCH);
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		try {
			bcgen.getBarcode(packageID, ownerName, outStream, dpi[0]);//, printableArea);
			ByteArrayInputStream inStream = new ByteArrayInputStream(outStream.toByteArray());
			DocPrintJob pj = service.createPrintJob();
			Doc doc = new SimpleDoc(inStream, docFlavor, null);
			pj.print(doc, attributeSet);
		} catch (IOException e) {
			// TODO Add other stuff
			System.out.println("Failed to generate barcode.");
			e.printStackTrace();
		} catch (PrintException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//TODO
	private void setDefaultPrinter(PrintService service) {
		this.service = service;
	}
	
	private void changePrinter() {
		PrintService[] services = PrintServiceLookup.
				lookupPrintServices(docFlavor,attributeSet);
		
		ArrayList<String> serviceNames = new ArrayList<String>();
		for (PrintService pservice: services) {
			System.out.println(pservice);
			serviceNames.add(pservice.getName());
			Class[] attCategories = pservice.getSupportedAttributeCategories();
			for(Class c: attCategories) {
				System.out.println(c);
			}
		}
		//TODO get printer from view
		if(services.length != 0) {
			service = services[0];
		}
//		if(serviceNames.size() > 0) {
//			int sIndex = viewAdaptor.getChoiceFromList(serviceNames);
//			setPrinter(services[sIndex]);
//		}
	}
	
	public void start() {
		changePrinter();
		printLabel("Navin Pathak","--Navin Pathak--");
		
	}
	//TODO
	
	public static void main(String[] args) {
		LabelPrinter lp = new LabelPrinter();
		lp.start();
	}
}

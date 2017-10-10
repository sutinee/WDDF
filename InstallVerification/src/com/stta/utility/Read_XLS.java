package com.stta.utility;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;



public class Read_XLS {	
	public  String filelocation;
	public  FileInputStream ipstr = null;
	public  FileOutputStream opstr =null;
	private HSSFWorkbook wb = null;
	private HSSFSheet ws = null;	
	
	public Read_XLS(String filelocation) {		
		this.filelocation=filelocation;
		try {
			ipstr = new FileInputStream(filelocation);
			wb = new HSSFWorkbook(ipstr);
			ws = wb.getSheetAt(0);
			ipstr.close();
		} catch (Exception e) {			
			e.printStackTrace();
		} 
		
	}
	
	//To retrieve No Of Rows from .xls file's sheets.
	public int retrieveNoOfRows(String wsName){		
		int sheetIndex=wb.getSheetIndex(wsName);
		if(sheetIndex==-1)
			return 0;
		else{
			ws = wb.getSheetAt(sheetIndex);
			int rowCount=ws.getLastRowNum()+1;		
			return rowCount;		
		}
	}
	
	//To retrieve No Of Columns from .cls file's sheets.
	public int retrieveNoOfCols(String wsName){
		int sheetIndex=wb.getSheetIndex(wsName);
		if(sheetIndex==-1)
			return 0;
		else{
			ws = wb.getSheetAt(sheetIndex);
			int colCount=ws.getRow(0).getLastCellNum();			
			return colCount;
		}
	}
	
	//To retrieve SuiteToRun and CaseToRun flag of test suite and test case.
	public String retrieveToRunFlag(String wsName, String colName, String rowName){
		
		int sheetIndex=wb.getSheetIndex(wsName);
		if(sheetIndex==-1)
			return null;
		else{
			int rowNum = retrieveNoOfRows(wsName);
			int colNum = retrieveNoOfCols(wsName);
			int colNumber=-1;
			int rowNumber=-1;			
			
			HSSFRow Suiterow = ws.getRow(0);				
			
			for(int i=0; i<colNum; i++){
				if(Suiterow.getCell(i).getStringCellValue().equals(colName.trim())){
					colNumber=i;					
				}					
			}
			
			if(colNumber==-1){
				return "";				
			}
			
			
			for(int j=0; j<rowNum; j++){
				HSSFRow Suitecol = ws.getRow(j);				
				if(Suitecol.getCell(0).getStringCellValue().equals(rowName.trim())){
					rowNumber=j;	
				}					
			}
			
			if(rowNumber==-1){
				return "";				
			}
			
			HSSFRow row = ws.getRow(rowNumber);
			HSSFCell cell = row.getCell(colNumber);
			if(cell==null){
				return "";
			}
			String value = cellToString(cell);
			return value;			
		}			
	}
	
	//To retrieve DataToRun flag of test data.
	public String[] retrieveToRunFlagTestData(String wsName, String colName){
		
		int sheetIndex=wb.getSheetIndex(wsName);
		if(sheetIndex==-1)
			return null;
		else{
			int rowNum = retrieveNoOfRows(wsName);
			int colNum = retrieveNoOfCols(wsName);
			int colNumber=-1;
					
			
			HSSFRow Suiterow = ws.getRow(0);				
			String data[] = new String[rowNum-1];
			for(int i=0; i<colNum; i++){
				if(Suiterow.getCell(i).getStringCellValue().equals(colName.trim())){
					colNumber=i;					
				}					
			}
			
			if(colNumber==-1){
				return null;				
			}
			
			for(int j=0; j<rowNum-1; j++){
				HSSFRow Row = ws.getRow(j+1);
				if(Row==null){
					data[j] = "";
				}
				else{
					HSSFCell cell = Row.getCell(colNumber);
					if(cell==null){
						data[j] = "";
					}
					else{
						String value = cellToString(cell);
						data[j] = value;	
					}	
				}
			}
			
			return data;			
		}			
	}
	
	//To retrieve test data from test case data sheets.
	public Object[][] retrieveTestData(String wsName){
		int sheetIndex=wb.getSheetIndex(wsName);
		if(sheetIndex==-1)
			return null;
		else{
				int rowNum = retrieveNoOfRows(wsName);
				int colNum = retrieveNoOfCols(wsName);
		
				Object data[][] = new Object[rowNum-1][colNum-2];
		
				for (int i=0; i<rowNum-1; i++){
					HSSFRow row = ws.getRow(i+1);
					for(int j=0; j< colNum-2; j++){					
						if(row==null){
							data[i][j] = "";
						}
						else{
							HSSFCell cell = row.getCell(j);	
					
							if(cell==null){
								data[i][j] = "";							
							}
							else{
								cell.setCellType(Cell.CELL_TYPE_STRING);
								String value = cellToString(cell);
								data[i][j] = value;						
							}
						}
					}				
				}			
				return data;		
		}
	
	}		
	
	
	public static String cellToString(HSSFCell cell){
		int type;
		Object result;
		type = cell.getCellType();			
		switch (type){
			case 0 :
				result = cell.getNumericCellValue();
				break;
				
			case 1 : 
				result = cell.getStringCellValue();
				break;
				
			default :
				throw new RuntimeException("Unsupportd cell.");			
		}
		return result.toString();
	}
	
	//To write result In test data and test case list sheet.
	public boolean writeResult(String wsName, String colName, int rowNumber, String Result){
		try{
			int sheetIndex=wb.getSheetIndex(wsName);
			if(sheetIndex==-1)
				return false;			
			int colNum = retrieveNoOfCols(wsName);
			int colNumber=-1;
					
			
			HSSFRow Suiterow = ws.getRow(0);			
			for(int i=0; i<colNum; i++){				
				if(Suiterow.getCell(i).getStringCellValue().equals(colName.trim())){
					colNumber=i;					
				}					
			}
			
			if(colNumber==-1){
				return false;				
			}
			
			HSSFRow Row = ws.getRow(rowNumber);
			HSSFCell cell = Row.getCell(colNumber);
			if (cell == null)
		        cell = Row.createCell(colNumber);			
			
			cell.setCellValue(Result);
			
			opstr = new FileOutputStream(filelocation);
			wb.write(opstr);
			opstr.close();
			
			
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	//To write result In test suite list sheet.
	public boolean writeResult(String wsName, String colName, String rowName, String Result){
		try{
			int rowNum = retrieveNoOfRows(wsName);
			int rowNumber=-1;
			int sheetIndex=wb.getSheetIndex(wsName);
			if(sheetIndex==-1)
				return false;			
			int colNum = retrieveNoOfCols(wsName);
			int colNumber=-1;
					
			
			HSSFRow Suiterow = ws.getRow(0);			
			for(int i=0; i<colNum; i++){				
				if(Suiterow.getCell(i).getStringCellValue().equals(colName.trim())){
					colNumber=i;					
				}					
			}
			
			if(colNumber==-1){
				return false;				
			}
			
			for (int i=0; i<rowNum-1; i++){
				HSSFRow row = ws.getRow(i+1);				
				HSSFCell cell = row.getCell(0);	
				cell.setCellType(Cell.CELL_TYPE_STRING);
				String value = cellToString(cell);	
				if(value.equals(rowName)){
					rowNumber=i+1;
					break;
				}
			}		
			
			HSSFRow Row = ws.getRow(rowNumber);
			HSSFCell cell = Row.getCell(colNumber);
			if (cell == null)
		        cell = Row.createCell(colNumber);			
			
			cell.setCellValue(Result);
			
			opstr = new FileOutputStream(filelocation);
			wb.write(opstr);
			opstr.close();
			
			
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean writeResultRow(String wsName, String colName, int rowNumber, String testActualRows[], String testResultRows[]){
	try{
		int sheetIndex=wb.getSheetIndex(wsName);
		if(sheetIndex==-1)
			return false;			
		int colNum = retrieveNoOfCols(wsName);
		int colNumber=-1;
		
		// Font Color 
	    CellStyle styleRed = wb.createCellStyle();   
	    CellStyle styleGreen = wb.createCellStyle();   

	    Font fontRed = wb.createFont();
        fontRed.setColor(IndexedColors.RED.getIndex());
        fontRed.setFontHeightInPoints((short) 8);
          
        Font fontGreen = wb.createFont();
        fontGreen.setColor(IndexedColors.GREEN.getIndex());
        fontGreen.setFontHeightInPoints((short) 8);
        
        styleRed.setFont(fontRed);
        styleGreen.setFont(fontGreen);
        // End Font Color
		
		HSSFRow Suiterow = ws.getRow(0);			
		for(int i=0; i<colNum; i++){				
			if(Suiterow.getCell(i).getStringCellValue().equals(colName.trim())){
				colNumber=i;					
			}					
		}
		
		if(colNumber==-1){
			return false;				
		}
		
		HSSFRow Row = ws.getRow(rowNumber);
		HSSFCell cell = Row.getCell(colNumber);
		if (cell == null)
	        cell = Row.createCell(colNumber);			
		String stringToWrite = "";
		System.out.println("Number of Rwos:" + testActualRows.length + "\n");
	    for (int i = 0; i < testActualRows.length; i++) {
	          System.out.println(testActualRows[i] + " ");
	          stringToWrite = stringToWrite + testActualRows[i]+"\n";
	     }
	   HSSFRichTextString richString = new HSSFRichTextString(stringToWrite);
	   int letterCount = 0;
	   for (int j = 0; j < testActualRows.length; j++) {
		   if(testResultRows[j].equals("RowFail"))
		   {
			   richString.applyFont(letterCount, testActualRows[j].length()+letterCount, fontRed);
			   letterCount = letterCount + testActualRows[j].length() + 1;
			   //System.out.println("letterCount = " + letterCount + "\n");
			   //System.out.println("Pass/Fail = " + testResultRows[j] + "\n");
		   }
		   if(testResultRows[j].equals("RowPass"))
		   {
			   richString.applyFont(letterCount, testActualRows[j].length()+letterCount, fontGreen);
			   letterCount = letterCount + testActualRows[j].length() + 1;
			   //System.out.println("letterCount = " + letterCount + "\n");			   
			   //System.out.println("Pass/Fail = " + testResultRows[j] + "\n");		   
		   }
		   
	   }


		cell.setCellValue(richString);
		opstr = new FileOutputStream(filelocation);
		wb.write(opstr);
		opstr.close();
		
		
	}catch(Exception e){
		e.printStackTrace();
		return false;
	}
	return true;
}
}

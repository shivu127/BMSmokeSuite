/**
 * 
 */
package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Platform;

public class ExcelUtils {
	public static String ExcelPath;
	static File file;
	static FileInputStream ExcelFile;

	// Main Directory of the project
	public static final String currentDir = System.getProperty("user.dir");

	public static String excelfileName = null;

	// Location of Test data excel file
	public static String testDataExcelPath = null;

	// Excel WorkBook
	private static Workbook excelWBook;

	// Excel Sheet
	private static Sheet excelWSheet;

	// Excel cell
	private static Cell cell;

	// Excel row
	private static Row row;

	// Row Number
	public static int rowNumber;

	// Column Number
	public static int columnNumber;

	// Setter and Getters of row and columns
	public static void setRowNumber(int pRowNumber) {
		rowNumber = pRowNumber;
	}

	public static int getRowNumber() {
		return rowNumber;
	}

	public static void setColumnNumber(int pColumnNumber) {
		columnNumber = pColumnNumber;
	}

	public static int getColumnNumber() {
		return columnNumber;
	}

	/**
	 * This method has 1 parameter: "Excel sheet name". It creates FileInputStream
	 * and set excel file and excel sheet to excelWBook and excelWSheet variables.
	 */
	public static void setExcelFileSheet(String sheetName) {

		// MAC or Windows Selection for excel path
		if (Platform.getCurrent().toString().equalsIgnoreCase("MAC")) {
			testDataExcelPath = currentDir + "//input//";
		} else if (Platform.getCurrent().toString().contains("WIN")) {
			testDataExcelPath = currentDir + "\\input\\";
		}
		try {

			// Create an object of File class to open excel file
			File file = new File(testDataExcelPath + excelfileName);

			// Create an object of FileInputStream class to read excel file
			FileInputStream inputStream = new FileInputStream(file);

			// Find the file extension by splitting file name in substring and
			// getting only extension name
			String fileExtensionName = excelfileName.substring(excelfileName.indexOf("."));

			// Check condition if the file is xlsx or xls file
			if (fileExtensionName.equals(".xlsx")) {

				// If it is xlsx file then create object of XSSFWorkbook class
				excelWBook = new XSSFWorkbook(inputStream);

			} else if (fileExtensionName.equals(".xls")) {

				// If it is xls file then create object of XSSFWorkbook class
				excelWBook = new HSSFWorkbook(inputStream);

			}

			excelWSheet = excelWBook.getSheet(sheetName);

		} catch (Exception e) {
			try {
				throw (e);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public static void setEnvExcelFile(String envName) {
		// MAC or Windows Selection for excel path
		if (Platform.getCurrent().toString().equalsIgnoreCase("MAC")) {
			testDataExcelPath = currentDir + "//input//";
		} else if (Platform.getCurrent().toString().contains("WIN")) {
			testDataExcelPath = currentDir + "\\input\\";
		}
		try {
			excelfileName = "TestData_Automation_" + envName + ".xlsx";
			// Create an object of File class to open excel file
			File file = new File(testDataExcelPath + excelfileName);

			System.out.println("Excel File set : " + excelfileName);

			// Create an object of FileInputStream class to read excel file
			FileInputStream inputStream = new FileInputStream(file);

			// Find the file extension by splitting file name in substring and
			// getting only extension name
			String fileExtensionName = excelfileName.substring(excelfileName.indexOf("."));

			// Check condition if the file is xlsx or xls file
			if (fileExtensionName.equals(".xlsx")) {

				// If it is xlsx file then create object of XSSFWorkbook class
				excelWBook = new XSSFWorkbook(inputStream);

			} else if (fileExtensionName.equals(".xls")) {

				// If it is xls file then create object of XSSFWorkbook class
				excelWBook = new HSSFWorkbook(inputStream);

			}
			// excelWSheet = excelWBook.getSheet(sheetName);

		} catch (Exception e) {
			try {
				throw (e);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * This method reads the test data from the Excel cell. We are passing row
	 * number and column number as parameters.
	 */
	public static String getCellData(int rowNum, int colNum) {
		try {
			cell = excelWSheet.getRow(rowNum).getCell(colNum);
			DataFormatter formatter = new DataFormatter();
			String cellData = formatter.formatCellValue(cell);
			return cellData;
		} catch (Exception e) {
			throw (e);
		}
	}

	public static String getCellData(String sheetName, int rowNum, int colNum) {
		try {
			excelWSheet = excelWBook.getSheet(sheetName);
			cell = excelWSheet.getRow(rowNum).getCell(colNum);
			DataFormatter formatter = new DataFormatter();
			// String cellData = formatter.formatCellValue(cell);
			String cellData = formatter.formatCellValue(cell).toString().trim();
			return cellData;
		} catch (Exception e) {
			throw (e);
		}
	}

	/**
	 * This method takes row number as a parameter and returns the data of given row
	 * number.
	 * 
	 * @param RowNum
	 * @return
	 */
	public static Row getRowData(int rowNum) {
		try {
			row = excelWSheet.getRow(rowNum);
			return row;
		} catch (Exception e) {
			throw (e);
		}
	}

	/**
	 * This method gets excel file, row and column number and set a value to the
	 * that cell.
	 * 
	 * @param value
	 * @param RowNum
	 * @param ColNum
	 */
	public static void setCellData(String value, int rowNum, int colNum) {
		try {
			row = excelWSheet.getRow(rowNum);
			cell = row.getCell(colNum);
			if (cell == null) {
				cell = row.createCell(colNum);
				cell.setCellValue(value);
			} else {
				cell.setCellValue(value);
			}

			// Create an object of File class to write excel file
			File excelFileName = new File(testDataExcelPath + excelfileName);

			// Constant variables Test Data path and Test Data file name
			FileOutputStream fileOut = new FileOutputStream(excelFileName);

			excelWBook.write(fileOut);

			fileOut.flush();
			fileOut.close();

		} catch (Exception e) {
			try {
				throw (e);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * @author srajput
	 * @description This method is to read excel data based on passed header value.
	 * @param RowNum
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<String> getTestData(String sheetName, String columnName) throws IOException {
		ArrayList<String> testdata = new ArrayList<String>();
		try {
			if (Platform.getCurrent().toString().equalsIgnoreCase("MAC")) {
				testDataExcelPath = currentDir + "//resourse//" + "Testdata.xlsx";
			} else if (Platform.getCurrent().toString().contains("WIN")) {
				testDataExcelPath = "C:\\Users\\Admin\\eclipse-workspace\\SmokeSuiteBM\\resource\\Testdata.xlsx";
			}
			FileInputStream fis = new FileInputStream(testDataExcelPath);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheet(sheetName);
			System.out.println("Input sheet" + sheet + " is read from location: " + testDataExcelPath);
			XSSFRow row = sheet.getRow(0);
			int col_num = -1;
			int count = 1;
			for (int i = 0; i < row.getLastCellNum(); i++) {
				if (row.getCell(i).getStringCellValue().trim().equals(columnName))
					col_num = i;
			}
			XSSFCell cell = row.getCell(col_num);
			row = sheet.getRow(count);
			cell = row.getCell(col_num);

			while (cell.getStringCellValue() != "") {
				String value = cell.getStringCellValue();
				testdata.add(value);
				System.out.println(count + " Data value of the Excel Cell is - " + testdata.get(count - 1));
				count++;
				row = sheet.getRow(count);
				cell = row.getCell(col_num);
			}
			return testdata;
		} catch (IllegalArgumentException e1) {
			System.out.println("Invalid Data has been given");
			throw (e1);
		} catch (NullPointerException e) {
			return testdata;
		} catch (Exception e) {
			throw (e);
		}
	}

	/*
	 * public static int getRowNum(String sheetName, String value) { try {
	 * excelWSheet = excelWBook.getSheet(sheetName); int totalRows =
	 * excelWSheet.getLastRowNum(); for (int i = 0; i< totalRows ; i++) { row=
	 * excelWSheet.getRow(i); int totalColmns } } catch (Exception e) { throw (e); }
	 * }
	 */

	public static String[] getExcelDataByColumn(String sheetName, int columnIndex) {

		if (Platform.getCurrent().toString().equalsIgnoreCase("MAC")) {
			testDataExcelPath = currentDir + "//resourse//" + "Testdata.xlsx";
		} else if (Platform.getCurrent().toString().contains("WIN")) {
			testDataExcelPath = "C:\\Users\\Admin\\eclipse-workspace\\SmokeSuiteBM\\resource\\Testdata.xlsx";
		}
		String[] retVal = (String[]) null;
		FileInputStream fis = null;
		List<String> lst = new ArrayList();
		try {
			fis = new FileInputStream(testDataExcelPath);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheet(sheetName);
			Iterator rowIter = sheet.rowIterator();

			while (rowIter.hasNext()) {
				XSSFRow myRow = (XSSFRow) rowIter.next();
				Iterator cellIter = myRow.cellIterator();
				Vector<String> cellStoreVector = new Vector();
				while (cellIter.hasNext()) {
					DataFormatter formatter = new DataFormatter();
					XSSFCell myCell = (XSSFCell) cellIter.next();
					// Returns the formatted value of a cell as a String regardless of the cell
					// type.
					String cellvalue = formatter.formatCellValue(myCell);
					// String cellvalue = myCell.getStringCellValue();
					cellStoreVector.addElement(cellvalue);
				}
				String columnValue = null;
				columnValue = ((String) cellStoreVector.get(columnIndex - 1)).toString();
				lst.add(columnValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		retVal = (String[]) lst.toArray(new String[lst.size()]);
		
				return retVal;
	}

}

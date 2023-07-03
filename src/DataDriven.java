

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataDriven {
	
	public ArrayList<String> getData(String testCaseName, String sheetName) throws IOException
	{
		ArrayList<String> testDataList = new ArrayList<String>();
		FileInputStream inputStream = new FileInputStream(System.getProperty("user.dir")
				+File.separator+"src"+File.separator+"test"+File.separator+"resources"+File.separator+"TestDataSheet.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
		int sheets = workbook.getNumberOfSheets();
		XSSFSheet xssfSheet = null;
		for(int i =0; i<sheets; i++) {
			if(workbook.getSheetName(i).equalsIgnoreCase(sheetName))
			{
				 xssfSheet = workbook.getSheetAt(i);
				 break;
			}
		}
		Iterator<Row> iteratorRow = xssfSheet.rowIterator();//sheet is a collection of rows
		Row firstrow = iteratorRow.next();// will give the first row
		
		Iterator<Cell> iteratorCol = firstrow.cellIterator();//row is a collection of cells
		int k = 0;
		int colNum = 0;
			while(iteratorCol.hasNext())
			{
				Cell cell = iteratorCol.next();
				if(cell.getStringCellValue().equalsIgnoreCase("Testcases"))
				{
					colNum=k;//finding the test case column from the first row 
				}
				k++;
			}
		System.out.println(colNum);
		while(iteratorRow.hasNext())
		{
			Row row = iteratorRow.next();
			if(row.getCell(colNum).getStringCellValue().equalsIgnoreCase(testCaseName))
					{
						Iterator<Cell> iteratorTestCol = row.cellIterator();
						while(iteratorTestCol.hasNext())
						{
							Cell c = iteratorTestCol.next();
							if(c.getCellType()==CellType.STRING)
							{
								 testDataList.add(c.getStringCellValue());	
							} else {
								 testDataList.add(NumberToTextConverter.toText(
										(c.getNumericCellValue())));
							}
						}
							
					}		
		}
		return testDataList;
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
	}	
}

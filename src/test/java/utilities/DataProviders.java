package utilities;

import java.io.IOException;

import org.testng.annotations.DataProvider;

public class DataProviders {
	
	//DataProvider1
	@DataProvider(name="LoginData")
	public String [][] getData() throws IOException
	{
		String path=".\\testData\\Opencart_LoginData.xlsx"; //taking xl file from testdata
		
		ExcelUtility xlutil=new ExcelUtility(path); //creating an object for XLUtility
		
		int totalrows=xlutil.getRowCount("Sheet1");
		int totalcols=xlutil.getCellCount("Sheet1", 1);
		
		String logindata[][]=new String[totalrows][totalcols]; //created for two dimensional array which store
		
		for(int i=1;i<=totalrows;i++) //1  //read the data from xl storing in two dimensional array
		{
			for(int j=0;j<totalcols;j++) //0 i is rows j is col
			{
				logindata[i-1][j]=xlutil.getCellData("Sheet1", i, j);
			}
		}
		return logindata; //returning two dimensional array
	}
	
	//Dataprovider 2
	
	//Dataprovider 3
	
	//Dataprovider 4

}

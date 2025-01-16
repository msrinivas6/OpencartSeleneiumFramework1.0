package utilities;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.ImageHtmlEmail;
import org.apache.commons.mail.resolver.DataSourceUrlResolver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Report;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import testBase.BaseClass;

public class ExtentReportManager implements ITestListener{
	
	public ExtentSparkReporter sparkReporter; //UI of the report
	public ExtentReports extent; //populate common info on the reports like browsername,env,tester executing..
	public ExtentTest test; // creating test case entries in the report and update status of the reports
	
	String repName;
	
public void onStart(ITestContext testContext) {
	
	    String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()); //to generate time stamp
	    repName = "Test-Report-" + timestamp + ".html";
		
		sparkReporter=new ExtentSparkReporter(".\\reports\\" + repName); //specify the path where report to be generated
		
		sparkReporter.config().setDocumentTitle("Opencart Automation Report"); //Title of the report
		sparkReporter.config().setReportName("Opencart Functional Testing"); //Name of the report
		sparkReporter.config().setTheme(Theme.DARK);
		
		extent= new ExtentReports();
		extent.attachReporter(sparkReporter);
		
		extent.setSystemInfo("Application", "opencart");
		extent.setSystemInfo("Module", "Admin");
		extent.setSystemInfo("Sub Module", "Customers");
		extent.setSystemInfo("User Name", System.getProperty("user.name"));
		extent.setSystemInfo("Environment", "QA");
		
		String os = testContext.getCurrentXmlTest().getParameter("os");
		extent.setSystemInfo("Operating System", os);
		
		String browser = testContext.getCurrentXmlTest().getParameter("browser");
		extent.setSystemInfo("Browser", browser);
		
		List<String> includeGroups = testContext.getCurrentXmlTest().getIncludedGroups();
		if(!includeGroups.isEmpty()) {
		extent.setSystemInfo("Groups", includeGroups.toString());
		}	
		
	}
	
	public void onTestSuccess(ITestResult result)
	{
		test=extent.createTest(result.getTestClass().getName());  //create a new entry in the report
		test.assignCategory(result.getMethod().getGroups()); //to display groups in report
		test.log(Status.PASS,result.getName() + "got successfully executed"); //update status pass/fail/skip
	}
	
	public void onTestFailure(ITestResult result) {
		test=extent.createTest(result.getTestClass().getName()); 
		test.assignCategory(result.getMethod().getGroups());
		test.log(Status.FAIL, result.getName() + "got failed");
		test.log(Status.INFO, result.getThrowable().getMessage());
		
		try {
			String imgPath = new BaseClass().captureScreen(result.getName());
			test.addScreenCaptureFromPath(imgPath);
		}catch (IOException e1) {
			e1.printStackTrace();
		}
		  }
	public void onTestSkipped(ITestResult result) { 
		test=extent.createTest(result.getTestClass().getName()); 
		test.assignCategory(result.getMethod().getGroups()); 
		test.log(Status.SKIP, result.getName() + "got skipped");
		test.log(Status.INFO, result.getThrowable().getMessage());
		  }
	 public void onFinish(ITestContext context) { 
              
		 extent.flush();
		 
		 String pathOfExtentReport = System.getProperty("user.dir")+ "\\reports\\"+repName;
		 File extentReport=new File(pathOfExtentReport);
		 
		 try {
			 Desktop.getDesktop().browse(extentReport.toURI());
		 }catch (IOException e) {
			 e.printStackTrace();
		 }

		/*
		 try { 
			 URL url =new URL("file:///"+System.getProperty("user.dir")+"\\reports\\"+repName);
		 //create the email message 
		 ImageHtmlEmail email = new ImageHtmlEmail();
		 email.setDataSourceResolver(new DataSourceUrlResolver(url));
		 email.setHostName("smtp.googlemail.com"); email.setSmtpPort(465);
		 email.setAuthenticator(new
		DefaultAuthenticator("sritester85@gmail.com","$rilaxmi6"));
		 email.setSSLOnConnect(true); email.setFrom("sritester85@gmail.com");
		 //sender email.setSubject("Test Results");
		 email.setMsg("Please find Attached Report...");
		 email.addTo("sritester85@gmail.com"); //Receiver 
		 email.attach(url,"extent report", "Please check Report.class.");
		 email.send(); //send email 
		 }catch(Exception e) {
			 e.printStackTrace();
		 
		 }	 
		 
	 */
		  }

}

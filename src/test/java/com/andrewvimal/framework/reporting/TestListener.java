package com.andrewvimal.framework.reporting;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.andrewvimal.framework.driver.DriverFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Hooks into TestNG to write each test into the Extent report.
 * On UI failures it attaches a screenshot taken at the moment of failure.
 */
public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        String name = result.getMethod().getMethodName();
        Object[] params = result.getParameters();
        if (params.length > 0) {
            name += " " + java.util.Arrays.toString(params);
        }
        ExtentTest test = ExtentManager.getInstance()
                .createTest(name, result.getMethod().getDescription())
                .assignCategory(result.getMethod().getGroups());
        ExtentManager.setTest(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentManager.getTest().pass("Passed");
        ExtentManager.removeTest();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = ExtentManager.getTest();
        test.fail(result.getThrowable());
        if (DriverFactory.hasDriver()) {
            try {
                String base64 = ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.BASE64);
                test.fail("Screenshot at failure",
                        MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());
            } catch (Exception e) {
                test.warning("Could not capture screenshot: " + e.getMessage());
            }
        }
        ExtentManager.removeTest();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = ExtentManager.getTest();
        if (test == null) {
            test = ExtentManager.getInstance().createTest(result.getMethod().getMethodName());
        }
        test.skip(result.getThrowable() != null ? result.getThrowable().getMessage() : "Skipped");
        ExtentManager.removeTest();
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentManager.getInstance().flush();
    }
}

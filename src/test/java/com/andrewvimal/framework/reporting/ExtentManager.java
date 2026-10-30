package com.andrewvimal.framework.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.andrewvimal.framework.config.ConfigReader;

public final class ExtentManager {

    public static final String REPORT_DIR = "target/extent-report";
    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> CURRENT = new ThreadLocal<>();

    private ExtentManager() {
    }

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            ExtentSparkReporter spark = new ExtentSparkReporter(REPORT_DIR + "/index.html");
            spark.config().setTheme(Theme.STANDARD);
            spark.config().setDocumentTitle("Automation Test Report");
            spark.config().setReportName("Selenium + REST Assured Test Run");

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Browser", ConfigReader.get("browser"));
            extent.setSystemInfo("Headless", ConfigReader.get("headless"));
            extent.setSystemInfo("Java", System.getProperty("java.version"));
        }
        return extent;
    }

    public static void setTest(ExtentTest test) {
        CURRENT.set(test);
    }

    public static ExtentTest getTest() {
        return CURRENT.get();
    }

    public static void removeTest() {
        CURRENT.remove();
    }
}

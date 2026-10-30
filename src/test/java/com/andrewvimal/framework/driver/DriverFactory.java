package com.andrewvimal.framework.driver;

import com.andrewvimal.framework.config.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * Creates one WebDriver per thread so tests can run in parallel safely.
 * Selenium Manager (built into Selenium 4.6+) downloads the right driver binary automatically.
 */
public final class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverFactory() {
    }

    public static void initDriver() {
        String browser = ConfigReader.get("browser").toLowerCase();
        boolean headless = ConfigReader.getBoolean("headless");

        WebDriver driver = switch (browser) {
            case "firefox" -> {
                FirefoxOptions options = new FirefoxOptions();
                if (headless) options.addArguments("-headless");
                yield new FirefoxDriver(options);
            }
            case "edge" -> {
                EdgeOptions options = new EdgeOptions();
                if (headless) options.addArguments("--headless=new");
                options.setExperimentalOption("prefs", passwordManagerOff());
                options.addArguments("--disable-features=PasswordLeakDetection");
                yield new EdgeDriver(options);
            }
            case "chrome" -> {
                ChromeOptions options = new ChromeOptions();
                if (headless) options.addArguments("--headless=new");
                options.addArguments("--window-size=1920,1080", "--no-sandbox", "--disable-dev-shm-usage");
                // Chrome's "Change your password" breach popup appears after logging in with a
                // well-known demo password and steals focus mid-test, so turn the password manager off.
                options.setExperimentalOption("prefs", passwordManagerOff());
                options.addArguments("--disable-features=PasswordLeakDetection");
                yield new ChromeDriver(options);
            }
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };

        if (!headless) {
            driver.manage().window().maximize();
        }
        DRIVER.set(driver);
    }

    private static Map<String, Object> passwordManagerOff() {
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);
        return prefs;
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("Driver not initialised. Call initDriver() first.");
        }
        return driver;
    }

    public static boolean hasDriver() {
        return DRIVER.get() != null;
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }
}

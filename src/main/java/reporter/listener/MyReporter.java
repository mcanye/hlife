package reporter.listener;

import com.aventstack.extentreports.ExtentTest;

/**
 * @Auther: Mchi
 * @Date: 2019年9月16日12:21:47
 * @Description:
 */
public class MyReporter {
    public static ExtentTest report;
    private static String testName;

    public static String getTestName() {
        return testName;
    }

    public static void setTestName(String testName) {
        MyReporter.testName = testName;
    }
}

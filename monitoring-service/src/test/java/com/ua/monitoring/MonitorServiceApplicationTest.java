package com.ua.monitoring;

public class MonitorServiceApplicationTest {

//report
//    @Test
//    void test() {
//        Results results = Runner.path("classpath:com/ua/monitoring").outputCucumberJson(true).parallel(1);
//        AssertionErrors.assertEquals("Tests completed successful", 0, results.getFailCount());
//
//        Collection<File> jsonFiles = FileUtils.listFiles(new File(results.getReportDir()), new String[] {"json"}, true);
//        List<String> jsonPaths = new ArrayList<>(jsonFiles.size());
//        jsonFiles.forEach(file -> jsonPaths.add(file.getAbsolutePath()));
//        Configuration config = new Configuration(new File("target"), "demo");
//        ReportBuilder reportBuilder = new ReportBuilder(jsonPaths, config);
//        reportBuilder.generateReports();
//    }
}

package com.pontusvision.tika;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.eclipse.jetty.server.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Unit test for simple App.
 */
public class AppTest {
  /**
   * Create the test case
   *
   * @param testName name of the test case
   */

  /**
   * @return the suite of tests being tested
   */

  public static Server server;
  static Gson gson = new Gson();

  @AfterClass
  public static void after() throws Exception {
//    App.gserver.stop().join();
//    App.oServer.shutdown();
    server.stop();

  }

  @BeforeClass
  public static void before() throws Exception {
    Path resourceDirectory = Paths.get(".");
    String absolutePath = resourceDirectory.toFile().getAbsolutePath();

//    String jpostalDataDir = Paths.get(absolutePath, "jpostal", "libpostal").toString();
    System.setProperty("user.dir", absolutePath);

//    System.setProperty("pg.jpostal.datadir", jpostalDataDir);

    server = App.createJettyServer();

    server.start();
    App.init();


  }

  /**
   * Rigourous Test :-)
   */
  @Test
  public void testApp() {
    Gson gson = new Gson();
    assertEquals("a", "a");


  }

  @Test
  public void test1() throws InterruptedException {


    String res = null;
    try {
//      res = App.executor.eval("App.g.V().hasNext();").get().toString();
      System.out.println(res);

    } catch (Exception e) {
      e.printStackTrace();
      assertNull(e);

    }

  }

  public void jsonTestUtil(String jsonFile, String jsonPath, String ruleName) throws InterruptedException {

    String res = null;
    try {
      Path resourceDirectory = Paths.get(".");
      String pwdAbsolutePath = resourceDirectory.toFile().getAbsolutePath();
      String jsonFilePath = Paths.get(pwdAbsolutePath, "src", "test", "resources", jsonFile).toString();

      String jsonString = FileUtils.readFileToString(new File(jsonFilePath), "UTF-8");

      System.out.println(res);

    } catch (IOException e) {
      e.printStackTrace();
      assertNull(e);
    }
  }
}

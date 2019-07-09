package com.google.cloud.gameservices.samples;

import static org.junit.Assert.assertTrue;

import com.google.cloud.gameservices.samples.realms.CreateRealm;
import com.google.cloud.gameservices.samples.realms.DeleteRealm;
import com.google.cloud.gameservices.samples.realms.GetRealm;
import com.google.cloud.gameservices.samples.realms.ListRealms;
import com.google.cloud.gameservices.samples.realms.UpdateRealm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class RealmTests {
  private static final String PROJECT_ID = System.getenv("GOOGLE_CLOUD_PROJECT");
  private static final String REGION_ID = "us-central1";

  private static String realmId = "realm-1";
  private static String realmName = String.format(
      "projects/%s/locations/%s/realms/%s", PROJECT_ID, REGION_ID, realmId);

  private final PrintStream originalOut = System.out;
  private ByteArrayOutputStream bout;

  @Before
  public void setUp() {
    bout = new ByteArrayOutputStream();
    System.setOut(new PrintStream(bout));
  }

  @BeforeClass
  public static void init()
      throws InterruptedException, TimeoutException, IOException {
    try {
      CreateRealm.createRealm(PROJECT_ID, REGION_ID, realmId);
    } catch (ExecutionException exception) {}
  }

  @After
  public void tearDown() {
    System.setOut(originalOut);
    bout.reset();
  }

  @AfterClass
  public static void tearDownClass()
      throws InterruptedException, ExecutionException, TimeoutException, IOException {
    try {
      DeleteRealm.deleteRealm(PROJECT_ID, REGION_ID, realmId);
    } catch (ExecutionException exception) {}
  }

  @Test
  public void createDeleteRealmTest()
      throws IOException, InterruptedException, ExecutionException, TimeoutException {
    String newRealmId = "realm-2";
    String newRealmName = String.format(
        "projects/%s/locations/%s/realms/%s", PROJECT_ID, REGION_ID, realmId);
    CreateRealm.createRealm(PROJECT_ID, REGION_ID, newRealmId);
    DeleteRealm.deleteRealm(PROJECT_ID, REGION_ID, newRealmId);
    assertTrue(bout.toString().contains("Realm created: " + newRealmName));
    assertTrue(bout.toString().contains("Realm deleted: " + newRealmName));
  }

  @Test
  public void getRealmTest() throws IOException{
    GetRealm.getRealm(PROJECT_ID, REGION_ID, realmId);

    assertTrue(bout.toString().contains("Realm found: " + realmName));
  }

  @Test
  public void listRealmsTest() throws IOException{
    ListRealms.listRealms(PROJECT_ID, REGION_ID);

    assertTrue(bout.toString().contains("Realm found: " + realmName));
  }

  @Test
  public void updateRealmTest()
      throws IOException, InterruptedException, ExecutionException, TimeoutException {
    UpdateRealm.updateRealm(PROJECT_ID, REGION_ID, realmId);

    assertTrue(bout.toString().contains("Realm updated: " + realmName));
  }
}

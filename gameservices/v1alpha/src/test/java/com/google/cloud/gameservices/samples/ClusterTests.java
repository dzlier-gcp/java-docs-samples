package com.google.cloud.gameservices.samples;

import static org.junit.Assert.assertTrue;

import com.google.cloud.gameservices.samples.clusters.CreateCluster;
import com.google.cloud.gameservices.samples.clusters.DeleteCluster;
import com.google.cloud.gameservices.samples.clusters.GetCluster;
import com.google.cloud.gameservices.samples.clusters.ListClusters;
import com.google.cloud.gameservices.samples.clusters.UpdateCluster;
import com.google.cloud.gameservices.samples.realms.CreateRealm;
import com.google.cloud.gameservices.samples.realms.DeleteRealm;

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
public class ClusterTests {
  private static final String PROJECT_ID = System.getenv("GOOGLE_CLOUD_PROJECT");
  private static final String REGION_ID = "us-central1";

  private static String realmId = "realm-1";

  private static String clusterId = "cluster-1";
  private static String clusterName = String.format(
      "projects/%s/locations/%s/gameServerClusters/%s", PROJECT_ID, REGION_ID, clusterId);

  private static String gkeClusterName = System.getenv("GKE_CLUSTER");

  private final PrintStream originalOut = System.out;
  private ByteArrayOutputStream bout;

  @Before
  public void setUp() {
    bout = new ByteArrayOutputStream();
    System.setOut(new PrintStream(bout));
  }

  @BeforeClass
  public static void init() throws InterruptedException, TimeoutException, IOException {
    try {
      CreateRealm.createRealm(PROJECT_ID, REGION_ID, realmId);
    } catch (ExecutionException exception) {}
    try {
      CreateCluster.createGameServerCluster(
          PROJECT_ID, REGION_ID, clusterId, realmId, gkeClusterName);
    } catch (ExecutionException exception) {}
  }

  @After
  public void tearDown() {
    System.setOut(originalOut);
    bout.reset();
  }

  @AfterClass
  public static void tearDownClass() throws InterruptedException, TimeoutException, IOException {
    try {
      DeleteCluster.deleteGameServerCluster(PROJECT_ID, REGION_ID, realmId, clusterId);
    } catch (ExecutionException exception) {}
    try {
      DeleteRealm.deleteRealm(PROJECT_ID, REGION_ID, realmId);
    } catch (ExecutionException exception) {}
  }

  @Test
  public void createDeleteClusterTest()
      throws IOException, InterruptedException, ExecutionException, TimeoutException {
    String newClusterId = "cluster-2";
    String newClusterName = String.format(
        "projects/%s/locations/%s/gameServerClusters/%s", PROJECT_ID, REGION_ID, clusterId);
    CreateCluster.createGameServerCluster(
        PROJECT_ID, REGION_ID, realmId, newClusterId, gkeClusterName);
    DeleteCluster.deleteGameServerCluster(PROJECT_ID, REGION_ID, realmId, newClusterId);
    assertTrue(bout.toString().contains("Game Server Cluster created: " + newClusterName));
    assertTrue(bout.toString().contains("Game Server Cluster deleted: " + newClusterName));
  }

  @Test
  public void getClusterTest() throws IOException{
    GetCluster.getGameServerCluster(PROJECT_ID, REGION_ID, realmId, clusterId);

    assertTrue(bout.toString().contains("Game Server Cluster found: " + clusterName));
  }

  @Test
  public void listClustersTest() throws IOException{
    ListClusters.listGameServerClusters(PROJECT_ID, REGION_ID, realmId);

    assertTrue(bout.toString().contains("Game Server Cluster found: " + clusterName));
  }

  @Test
  public void updateClusterTest()
      throws IOException, InterruptedException, ExecutionException, TimeoutException {
    UpdateCluster.updateGameServerCluster(PROJECT_ID, REGION_ID, realmId, clusterId);

    assertTrue(bout.toString().contains("Game Server Cluster updated: " + clusterName));
  }
}

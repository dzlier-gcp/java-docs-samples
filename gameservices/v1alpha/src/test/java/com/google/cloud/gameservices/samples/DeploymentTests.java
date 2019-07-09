package com.google.cloud.gameservices.samples;

import static org.junit.Assert.assertTrue;

import com.google.cloud.gameservices.samples.deployments.CommitRollout;
import com.google.cloud.gameservices.samples.deployments.CreateDeployment;
import com.google.cloud.gameservices.samples.deployments.DeleteDeployment;
import com.google.cloud.gameservices.samples.deployments.GetDeployment;
import com.google.cloud.gameservices.samples.deployments.GetDeploymentTarget;
import com.google.cloud.gameservices.samples.deployments.ListDeployments;
import com.google.cloud.gameservices.samples.deployments.RevertRollout;
import com.google.cloud.gameservices.samples.deployments.SetRolloutTarget;
import com.google.cloud.gameservices.samples.deployments.StartRollout;
import com.google.cloud.gameservices.samples.deployments.UpdateDeployment;

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
public class DeploymentTests {
  private static final String PROJECT_ID = System.getenv("GOOGLE_CLOUD_PROJECT");

  private static String deploymentId = "deployment-1";
  private static String deploymentName = String.format(
      "projects/%s/locations/global/gameServerDeployment/%s", PROJECT_ID, deploymentId);

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
      CreateDeployment.createGameServerDeployment(PROJECT_ID, deploymentId);
    } catch (ExecutionException exception) {}
  }

  @After
  public void tearDown() {
    System.setOut(originalOut);
    bout.reset();
  }

  @AfterClass
  public static void tearDownClass()
      throws InterruptedException, TimeoutException, IOException {
    try {
      DeleteDeployment.deleteGameServerDeployment(PROJECT_ID, deploymentId);
    } catch (ExecutionException exception) {}
  }

  @Test
  public void createDeleteGameServerDeploymentTest()
      throws IOException, InterruptedException, ExecutionException, TimeoutException {
    String newDeploymentId = "deployment-2";
    String newDeploymentName = String.format(
        "projects/%s/locations/global/gameServerDeployment/%s", PROJECT_ID, newDeploymentId);
    CreateDeployment.createGameServerDeployment(PROJECT_ID, newDeploymentId);
    DeleteDeployment.deleteGameServerDeployment(PROJECT_ID, newDeploymentId);
    assertTrue(bout.toString().contains("Game Server Deployment created: " + newDeploymentName));
    assertTrue(bout.toString().contains("Game Server Deployment deleted: " + newDeploymentName));
  }

  @Test
  public void getGameServerDeploymentTest() throws IOException{
    GetDeployment.getGameServerDeployment(PROJECT_ID, deploymentId);

    assertTrue(bout.toString().contains("Game Server Deployment found: " + deploymentName));
  }

  @Test
  public void listGameServerDeploymentsTest() throws IOException{
    ListDeployments.listGameServerDeployments(PROJECT_ID);

    assertTrue(bout.toString().contains("Game Server Deployment found: " + deploymentName));
  }

  @Test
  public void updateGameServerDeploymentTest()
      throws IOException, InterruptedException, ExecutionException, TimeoutException {
    UpdateDeployment.updateGameServerDeployment(PROJECT_ID, deploymentId);

    assertTrue(bout.toString().contains("Game Server Deployment updated: " + deploymentName));
  }

  @Test
  public void rolloutStartSetGetTargetCommitTests()
      throws InterruptedException, ExecutionException, TimeoutException, IOException {
    StartRollout.startRollout(deploymentName, "new-game-template-1");
    assertTrue(bout.toString().contains("Rollout started:"));

    SetRolloutTarget.setRolloutTarget(deploymentName);
    assertTrue(bout.toString().contains("Rollout target set:"));

    CommitRollout.commitRollout(deploymentName);
    assertTrue(bout.toString().contains("Rollout target set:"));

    GetDeploymentTarget.getDeploymentTarget(deploymentName);
    assertTrue(bout.toString().contains("Found target with "));
  }

  @Test
  public void rolloutRevertTest()
      throws InterruptedException, ExecutionException, TimeoutException, IOException {
    String newDeploymentId = "deployment-2";
    String newDeploymentName = String.format(
        "projects/%s/locations/global/gameServerDeployment/%s", PROJECT_ID, newDeploymentId);
    CreateDeployment.createGameServerDeployment(PROJECT_ID, newDeploymentId);
    StartRollout.startRollout(newDeploymentName, "new-game-template-1");
    assertTrue(bout.toString().contains("Rollout started:"));

    RevertRollout.revertRollout(newDeploymentName);
    assertTrue(bout.toString().contains("Rollout reverted:"));
  }
}

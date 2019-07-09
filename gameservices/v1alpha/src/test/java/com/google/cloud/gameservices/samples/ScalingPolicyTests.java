package com.google.cloud.gameservices.samples;

import static org.junit.Assert.assertTrue;

import com.google.cloud.gameservices.samples.allocationpolicies.CreateAllocationPolicy;
import com.google.cloud.gameservices.samples.deployments.CreateDeployment;
import com.google.cloud.gameservices.samples.deployments.DeleteDeployment;
import com.google.cloud.gameservices.samples.scalingpolicies.CreateScalingPolicy;
import com.google.cloud.gameservices.samples.scalingpolicies.DeleteScalingPolicy;
import com.google.cloud.gameservices.samples.scalingpolicies.GetScalingPolicy;
import com.google.cloud.gameservices.samples.scalingpolicies.ListScalingPolicies;
import com.google.cloud.gameservices.samples.scalingpolicies.UpdateScalingPolicy;

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
public class ScalingPolicyTests {
  private static final String PROJECT_ID = System.getenv("GOOGLE_CLOUD_PROJECT");
  private static final String REGION_ID = "us-central1";

  private static String deploymentId = "deployment-1";
  private static String deploymentName = String.format(
      "projects/%s/locations/%s/scalingPolicies/%s", PROJECT_ID, REGION_ID, deploymentId);
  private static String policyId = "policy-1";
  private static String policyName = String.format(
      "projects/%s/locations/%s/scalingPolicies/%s", PROJECT_ID, REGION_ID, policyId);

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
    try {
      CreateScalingPolicy.createScalingPolicy(PROJECT_ID, REGION_ID, policyId, deploymentName);
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
      DeleteScalingPolicy.deleteScalingPolicy(PROJECT_ID, REGION_ID, policyId);
      DeleteDeployment.deleteGameServerDeployment(PROJECT_ID, deploymentId);
    } catch (ExecutionException exception) {}
  }

  @Test
  public void createDeleteScalingPolicyTest()
      throws IOException, InterruptedException, ExecutionException, TimeoutException {
    String newPolicyId = "deployment-2";
    String newPolicyName = String.format(
        "projects/%s/locations/%s/scalingPolicies/%s", PROJECT_ID, REGION_ID, policyId);
    CreateScalingPolicy.createScalingPolicy(PROJECT_ID, REGION_ID, newPolicyId, deploymentName);
    assertTrue(bout.toString().contains("Scaling Policy created: " + newPolicyName));

    DeleteScalingPolicy.deleteScalingPolicy(PROJECT_ID, REGION_ID, newPolicyId);
    assertTrue(bout.toString().contains("Scaling Policy deleted: " + newPolicyName));
  }

  @Test
  public void getScalingPolicyTest() throws IOException{
    GetScalingPolicy.getScalingPolicy(PROJECT_ID, REGION_ID, policyId);

    assertTrue(bout.toString().contains("Scaling Policy found: " + policyName));
  }

  @Test
  public void listScalingPoliciesTest() throws IOException{
    ListScalingPolicies.listScalingPolicies(PROJECT_ID, REGION_ID);

    assertTrue(bout.toString().contains("Scaling Policy found: " + policyName));
  }

  @Test
  public void updateScalingPoliciesTest()
      throws IOException, InterruptedException, ExecutionException, TimeoutException {
    UpdateScalingPolicy.updateScalingPolicy(PROJECT_ID, REGION_ID, policyId);

    assertTrue(bout.toString().contains("Scaling Policy updated: " + policyName));
  }
}

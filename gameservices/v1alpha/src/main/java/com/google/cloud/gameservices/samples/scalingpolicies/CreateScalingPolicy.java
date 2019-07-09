package com.google.cloud.gameservices.samples.scalingpolicies;

// [START cloud_game_servers_scaling_policy_create]

import com.google.api.gax.longrunning.OperationSnapshot;
import com.google.api.gax.retrying.RetryingFuture;
import com.google.cloud.gaming.v1alpha.FleetAutoscalerSettings;
import com.google.cloud.gaming.v1alpha.LabelSelector;
import com.google.cloud.gaming.v1alpha.ScalingPoliciesServiceClient;
import com.google.cloud.gaming.v1alpha.ScalingPoliciesServiceSettings;
import com.google.cloud.gaming.v1alpha.ScalingPolicy;
import com.google.cloud.gaming.v1alpha.CreateScalingPolicyRequest;
import com.google.cloud.gaming.v1alpha.Schedule;
import com.google.protobuf.Int32Value;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CreateScalingPolicy {
  private static String gameServersEndpoint = Optional
      .of(System.getenv("GAME_SERVER_ENDPOINT"))
      .orElse("gameservices.googleapis.com");

  public static void createScalingPolicy(
      String projectId, String regionId, String policyId, String deploymentName)
      throws IOException, ExecutionException, InterruptedException, TimeoutException {
    // String projectId = "your-project-id";
    // String regionId = "us-central1-f";
    // String policyId = "your-policy-id";
    ScalingPoliciesServiceClient client = ScalingPoliciesServiceClient.create(
        ScalingPoliciesServiceSettings.newBuilder().setEndpoint(gameServersEndpoint).build()
    );

    String parent = String.format("projects/%s/locations/%s", projectId, regionId);
    String policyName = String.format("%s/scalingPolicies/%s", parent, policyId);

    ScalingPolicy policy = ScalingPolicy
        .newBuilder()
        .setName(policyName)
        .setPriority(Int32Value.newBuilder().setValue(1))
        .setFleetAutoscalerSettings(FleetAutoscalerSettings
            .newBuilder()
            .setBufferSizeAbsolute(1000)
            .setMinReplicas(1)
            .setMaxReplicas(2)
            .build())
        .setGameServerDeployment(deploymentName)
        .build();

    RetryingFuture<OperationSnapshot> poll = client.createScalingPolicyAsync(
        CreateScalingPolicyRequest
            .newBuilder()
            .setParent(parent)
            .setScalingPolicyId(policyId)
            .setScalingPolicy(policy)
            .build()).getPollingFuture();

    if (poll.get(1, TimeUnit.MINUTES).isDone()) {
      System.out.println("Scaling Policy created: " + policy.getName());
    } else {
      throw new RuntimeException("Scaling Policy create request unsuccessful.");
    }
  }
}
// [END cloud_game_servers_scaling_policy_create]

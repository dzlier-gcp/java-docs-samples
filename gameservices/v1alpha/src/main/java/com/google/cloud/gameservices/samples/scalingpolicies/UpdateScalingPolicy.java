package com.google.cloud.gameservices.samples.scalingpolicies;

// [START cloud_game_servers_scaling_policy_update]

import com.google.api.gax.longrunning.OperationSnapshot;
import com.google.api.gax.retrying.RetryingFuture;
import com.google.cloud.gaming.v1alpha.ScalingPoliciesServiceClient;
import com.google.cloud.gaming.v1alpha.ScalingPoliciesServiceSettings;
import com.google.cloud.gaming.v1alpha.ScalingPolicy;
import com.google.cloud.gaming.v1alpha.UpdateScalingPolicyRequest;
import com.google.protobuf.FieldMask;
import com.google.protobuf.Int32Value;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class UpdateScalingPolicy {
  private static String gameServersEndpoint = Optional
      .of(System.getenv("GAME_SERVER_ENDPOINT"))
      .orElse("gameservices.googleapis.com");

  public static void updateScalingPolicy(String projectId, String regionId, String policyId)
      throws IOException, InterruptedException, ExecutionException, TimeoutException {
    // String projectId = "your-project-id";
    // String regionId = "us-central1-f";
    // String policyId = "your-policy-id";
    ScalingPoliciesServiceClient client = ScalingPoliciesServiceClient.create(
        ScalingPoliciesServiceSettings.newBuilder().setEndpoint(gameServersEndpoint).build()
    );

    String policyName = String.format(
        "projects/%s/locations/%s/scalingPolicies/%s", projectId, regionId, policyId);

    ScalingPolicy policy = ScalingPolicy
        .newBuilder()
        .setPriority(Int32Value.newBuilder().setValue(10).build())
        .build();

    RetryingFuture<OperationSnapshot> poll = client.updateScalingPolicyAsync(UpdateScalingPolicyRequest
        .newBuilder()
        .setScalingPolicy(policy)
        .setUpdateMask(FieldMask.newBuilder().addPaths("priority"))
        .build())
        .getPollingFuture();

    if (poll.get(1, TimeUnit.MINUTES).isDone()) {
      ScalingPolicy updatedPolicy = client.getScalingPolicy(policyName);
      System.out.println("Scaling Policy updated: " + updatedPolicy.getName());
    } else {
      throw new RuntimeException("Scaling Policy update request unsuccessful.");
    }
  }
}
// [END cloud_game_servers_scaling_policy_update]

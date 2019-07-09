package com.google.cloud.gameservices.samples.scalingpolicies;

// [START cloud_game_servers_scaling_policy_get]

import com.google.cloud.gaming.v1alpha.ScalingPoliciesServiceClient;
import com.google.cloud.gaming.v1alpha.ScalingPoliciesServiceSettings;
import com.google.cloud.gaming.v1alpha.ScalingPolicy;

import java.io.IOException;
import java.util.Optional;

public class GetScalingPolicy {
  private static String gameServersEndpoint = Optional
      .of(System.getenv("GAME_SERVER_ENDPOINT"))
      .orElse("gameservices.googleapis.com");

  public static void getScalingPolicy(String projectId, String regionId, String policyId)
      throws IOException {
    // String projectId = "your-project-id";
    // String regionId = "us-central1-f";
    // String policyId = "your-policy-id";
    ScalingPoliciesServiceClient client = ScalingPoliciesServiceClient.create(
        ScalingPoliciesServiceSettings.newBuilder().setEndpoint(gameServersEndpoint).build()
    );

    String policyName = String.format(
        "projects/%s/locations/%s/scalingPolicies/%s", projectId, regionId, policyId);

    ScalingPolicy scalingPolicy = client.getScalingPolicy(policyName);

    System.out.println("Scaling Policy found: " + scalingPolicy.getName());
  }
}
// [END cloud_game_servers_scaling_policy_get]

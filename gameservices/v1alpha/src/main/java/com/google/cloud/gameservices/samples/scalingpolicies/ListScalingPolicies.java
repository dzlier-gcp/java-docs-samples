package com.google.cloud.gameservices.samples.scalingpolicies;

// [START cloud_game_servers_scaling_policy_list]

import com.google.cloud.gaming.v1alpha.ScalingPoliciesServiceClient;
import com.google.cloud.gaming.v1alpha.ScalingPoliciesServiceClient.ListScalingPoliciesPagedResponse;
import com.google.cloud.gaming.v1alpha.ScalingPoliciesServiceSettings;
import com.google.cloud.gaming.v1alpha.ScalingPolicy;

import java.io.IOException;
import java.util.Optional;

public class ListScalingPolicies {
  private static String gameServersEndpoint = Optional
      .of(System.getenv("GAME_SERVER_ENDPOINT"))
      .orElse("gameservices.googleapis.com");

  public static void listScalingPolicies(String projectId, String regionId)
      throws IOException {
    // String projectId = "your-project-id";
    // String regionId = "us-central1-f";
    ScalingPoliciesServiceClient client = ScalingPoliciesServiceClient.create(
        ScalingPoliciesServiceSettings.newBuilder().setEndpoint(gameServersEndpoint).build()
    );

    String parent = String.format("projects/%s/locations/%s", projectId, regionId);

    ListScalingPoliciesPagedResponse response = client.listScalingPolicies(parent);

    for (ScalingPolicy policy : response.iterateAll()) {
      System.out.println("Scaling Policy found: " + policy.getName());
    }

  }
}
// [END cloud_game_servers_scaling_policy_list]

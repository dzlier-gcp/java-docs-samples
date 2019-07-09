package com.google.cloud.gameservices.samples.allocationpolicies;

// [START cloud_game_servers_allocation_policy_get]

import com.google.cloud.gaming.v1alpha.AllocationPoliciesServiceClient;
import com.google.cloud.gaming.v1alpha.AllocationPoliciesServiceSettings;
import com.google.cloud.gaming.v1alpha.AllocationPolicy;

import java.io.IOException;
import java.util.Optional;

public class GetAllocationPolicy {
  private static String gameServersEndpoint = Optional
      .of(System.getenv("GAME_SERVER_ENDPOINT"))
      .orElse("gameservices.googleapis.com");

  public static void getAllocationPolicy(String projectId, String policyId) throws IOException {
    // String projectId = "your-project-id";
    // String policyId = "your-policy-id";
    AllocationPoliciesServiceClient client = AllocationPoliciesServiceClient.create(
        AllocationPoliciesServiceSettings.newBuilder().setEndpoint(gameServersEndpoint).build()
    );

    String policyName = String.format(
        "projects/%s/locations/global/allocationPolicies/%s", projectId, policyId);

    AllocationPolicy allocationPolicy = client.getAllocationPolicy(policyName);

    System.out.println("Allocation Policy found: " + allocationPolicy.getName());
  }
}
// [END cloud_game_servers_allocation_policy_get]

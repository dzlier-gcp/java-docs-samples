package com.google.cloud.gameservices.samples.allocationpolicies;

// [START cloud_game_servers_allocation_policy_list]

import com.google.cloud.gaming.v1alpha.AllocationPoliciesServiceClient;
import com.google.cloud.gaming.v1alpha.AllocationPoliciesServiceClient.ListAllocationPoliciesPagedResponse;
import com.google.cloud.gaming.v1alpha.AllocationPoliciesServiceSettings;
import com.google.cloud.gaming.v1alpha.AllocationPolicy;

import java.io.IOException;
import java.util.Optional;

public class ListAllocationPolicies {
  private static String gameServersEndpoint = Optional
      .of(System.getenv("GAME_SERVER_ENDPOINT"))
      .orElse("gameservices.googleapis.com");

  public static void listAllocationPolicies(String projectId) throws IOException {
    // String projectId = "your-project-id";
    AllocationPoliciesServiceClient client = AllocationPoliciesServiceClient.create(
        AllocationPoliciesServiceSettings.newBuilder().setEndpoint(gameServersEndpoint).build()
    );

    String parent = String.format("projects/%s/locations/global", projectId);

    ListAllocationPoliciesPagedResponse response = client.listAllocationPolicies(parent);

    for (AllocationPolicy policy : response.iterateAll()) {
      System.out.println("Allocation Policy found: " + policy.getName());
    }

  }
}
// [END cloud_game_servers_allocation_policy_list]

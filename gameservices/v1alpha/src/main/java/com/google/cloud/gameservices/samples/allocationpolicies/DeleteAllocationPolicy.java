package com.google.cloud.gameservices.samples.allocationpolicies;

// [START cloud_game_servers_allocation_policy_delete]

import com.google.api.gax.longrunning.OperationSnapshot;
import com.google.api.gax.retrying.RetryingFuture;
import com.google.cloud.gaming.v1alpha.AllocationPoliciesServiceClient;
import com.google.cloud.gaming.v1alpha.AllocationPoliciesServiceSettings;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class DeleteAllocationPolicy {
  private static String gameServersEndpoint = Optional
      .of(System.getenv("GAME_SERVER_ENDPOINT"))
      .orElse("gameservices.googleapis.com");

  public static void deleteAllocationPolicy(String projectId, String policyId)
      throws IOException, ExecutionException, InterruptedException, TimeoutException {
    // String projectId = "your-project-id";
    // String policyId = "your-policy-id";
    AllocationPoliciesServiceClient client = AllocationPoliciesServiceClient.create(
        AllocationPoliciesServiceSettings.newBuilder().setEndpoint(gameServersEndpoint).build()
    );

    String parent = String.format("projects/%s/locations/global", projectId);
    String policyName = String.format("%s/allocationPolicies/%s", parent, policyId);

    RetryingFuture<OperationSnapshot> poll = client
        .deleteAllocationPolicyAsync(policyName)
        .getPollingFuture();

    if (poll.get(1, TimeUnit.MINUTES).isDone()) {
      System.out.println("Allocation Policy deleted: " + policyName);
    } else {
      throw new RuntimeException("Allocation Policy delete request unsuccessful.");
    }
  }
}
// [END cloud_game_servers_allocation_policy_delete]

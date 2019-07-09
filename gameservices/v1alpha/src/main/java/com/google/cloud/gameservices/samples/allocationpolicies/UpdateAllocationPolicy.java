package com.google.cloud.gameservices.samples.allocationpolicies;

// [START cloud_game_servers_allocation_policy_update]

import com.google.api.gax.longrunning.OperationSnapshot;
import com.google.api.gax.retrying.RetryingFuture;
import com.google.cloud.gaming.v1alpha.AllocationPoliciesServiceClient;
import com.google.cloud.gaming.v1alpha.AllocationPoliciesServiceSettings;
import com.google.cloud.gaming.v1alpha.AllocationPolicy;
import com.google.cloud.gaming.v1alpha.UpdateAllocationPolicyRequest;
import com.google.protobuf.FieldMask;
import com.google.protobuf.Int32Value;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class UpdateAllocationPolicy {
  private static String gameServersEndpoint = Optional
      .of(System.getenv("GAME_SERVER_ENDPOINT"))
      .orElse("gameservices.googleapis.com");

  public static void updateAllocationPolicy(String projectId, String policyId)
      throws IOException, InterruptedException, ExecutionException, TimeoutException {
    // String projectId = "your-project-id";
    // String policyId = "your-policy-id";
    AllocationPoliciesServiceClient client = AllocationPoliciesServiceClient.create(
        AllocationPoliciesServiceSettings.newBuilder().setEndpoint(gameServersEndpoint).build()
    );

    String policyName = String.format(
        "projects/%s/locations/global/allocationPolicies/%s", projectId, policyId);

    AllocationPolicy policy = AllocationPolicy
        .newBuilder()
        .setWeight(5)
        .build();

    RetryingFuture<OperationSnapshot> poll = client.updateAllocationPolicyAsync(
        UpdateAllocationPolicyRequest
            .newBuilder()
            .setAllocationPolicy(policy)
            .setUpdateMask(FieldMask.newBuilder().addPaths("weight"))
            .build())
            .getPollingFuture();

    if (poll.get(1, TimeUnit.MINUTES).isDone()) {
      AllocationPolicy updatedPolicy = client.getAllocationPolicy(policyName);
      System.out.println("Allocation Policy updated: " + updatedPolicy.getName());
    } else {
      throw new RuntimeException("Allocation Policy update request unsuccessful.");
    }
  }
}
// [END cloud_game_servers_allocation_policy_update]

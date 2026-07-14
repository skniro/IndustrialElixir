package com.skniro.industrial_elixir.api.item.replicator;

/**
 * Holds the replication cost for a single item: UU matter and energy required.
 *
 * @param uuCost     UU matter cost in mB per replication
 * @param energyCost energy cost in EU per replication
 */
public record ReplicatorCost(int uuCost, long energyCost) {
}

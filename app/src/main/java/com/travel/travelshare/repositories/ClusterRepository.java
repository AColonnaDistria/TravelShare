package com.travel.travelshare.repositories;

import com.google.firebase.firestore.FieldValue;
import com.travel.travelshare.model.location.Cluster;
import com.travel.travelshare.model.location.ExactLocation;
import com.travel.travelshare.model.location.Location;

public class ClusterRepository extends SimpleRepository<Cluster> {
    public ClusterRepository() {
        super(Cluster.class, "travelshare_clusters");
    }

    // ATOMIC; PREVENTS RACE CONDITIONS
    /*
    public void incrementPostCount(String geohash, ExactLocation location) {
        this.database.runTransaction(transaction -> {
            Cluster existingCluster = transaction.get(geohash);

            if (existingCluster == null) {
                // SCENARIO 1:s New Cluster
                // If no cluster exists for this geohash, create one.
                Cluster newCluster = new Cluster(geohash, location, 1);
                transaction.set(geohash, newCluster);
            } else {
                transaction.update(geohash, "numberOfPosts", FieldValue.increment(1))
            }
        });
    }
    */
}

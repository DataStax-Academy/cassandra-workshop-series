#!/usr/bin/env bash

# Flsuh Memtables to disk, clears out commitlog
for RACK_ID in 1 2 3; do
  kubectl exec ebs-cluster-ebs-dc-1-rack-$RACK_ID-sts-0 -- nodetool flush
done

# Truncate table
kubectl exec ebs-cluster-ebs-dc-1-rack-1-sts-0 -- cqlsh -e "TRUNCATE baselines.iot"

# Remove snapshots and hints
for RACK_ID in 1 2 3; do
  kubectl exec ebs-cluster-ebs-dc-1-rack-$RACK_ID-sts-0 -- nodetool clearsnapshot --all
  kubectl exec ebs-cluster-ebs-dc-1-rack-$RACK_ID-sts-0 -- nodetool truncatehints
done

package org.inek.dataportal.care.proof.util;

import org.inek.dataportal.care.proof.ProofWardInfo;
import org.inek.dataportal.care.proof.entity.Proof;

import java.util.HashSet;
import java.util.Set;

public class ProofUpdateSets {
    private Set<Proof> deletionSet = new HashSet<>();
    private Set<Proof> updateSet = new HashSet<>();
    private Set<ProofWardInfo> usedProofWardInfos = new HashSet<>();

    public ProofUpdateSets(Set<Proof> deletionSet, Set<Proof> updateSet, Set<ProofWardInfo> usedProofWardInfos) {
        this.deletionSet = deletionSet;
        this.updateSet = updateSet;
        this.usedProofWardInfos = usedProofWardInfos;
    }

    public Set<Proof> getDeletionSet() {
        return deletionSet;
    }

    public Set<Proof> getUpdateSet() {
        return updateSet;
    }

    public Set<ProofWardInfo> getUsedProofWardInfos() {
        return usedProofWardInfos;
    }
}

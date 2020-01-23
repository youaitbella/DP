package org.inek.dataportal.care.proof;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ProofWard {
    private Date from;
    private Date to;
    private int locationNumber;
    private String wardName;
    private Set<Integer> sensitiveAreas = new HashSet<>();
    private Set<String> depts = new HashSet<>();
    private Set<String> deptNames = new HashSet<>();
    private int beds;

}

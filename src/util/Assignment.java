package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Assignment {

    private List<Triplet<Integer, Integer, Nogood>> assignment;
    private Integer recentlyChanged = 0;
    public Assignment() {
        recentlyChanged = 0;
        assignment = new ArrayList<>();
    }

    public Assignment(Assignment a) {
        assignment = new ArrayList<>(a.getAssignment());
        recentlyChanged = a.getRecentlyChanged();
    }

    public List<Triplet<Integer, Integer, Nogood>> getAssignment() {
        return assignment;
    }

    public void addAssignment(Integer literal, Integer dl, Nogood nogood) {
        recentlyChanged = literal;
        assignment.add(new Triplet<>(literal, dl, nogood));
    }

    public Set<Integer> getLiteralSet() {
        return assignment.stream().map(Triplet::getFirst).collect(Collectors.toSet());
    }

    public Integer getDl(Integer literal) {
        for(Triplet<Integer, Integer, Nogood> t: assignment) {
            if(t.getFirst().equals(literal)) {
                return t.getSecond();
            }
        }
        return 0;
    }

    public Nogood getImplicatn(Integer literal) {
        for(Triplet<Integer, Integer, Nogood> t: assignment) {
            if(t.getFirst().equals(literal)) {
                return t.getThird();
            }
        }
        return null;
    }

    public Integer getMaxDlWithoutLit(Nogood n, Integer literal) {
        Integer max = 0;
        for(Integer lit: n.getLiterals()) {
            if(!lit.equals(literal)) {
                Integer dl = getDl(lit);
                if(dl > max) {
                    max = dl;
                }
            }
        }

        return max;
    }

    public Integer getMaxDl(Nogood n) {
        Integer max = 0;
        for(Integer lit: n.getLiterals()) {
            Integer dl = getDl(lit);
            if(dl > max) {
                max = dl;
            }
        }
        return max;
    }
    public void remove(Integer maxDl) {


        assignment = assignment.stream().filter(integerIntegerNogoodTriplet -> {
            Integer dl = integerIntegerNogoodTriplet.getSecond();
            Integer lit = integerIntegerNogoodTriplet.getFirst();
            return dl <= maxDl;
        }).collect(Collectors.toList());
    }

    public boolean isComplete(Instance instance) {
        for(Nogood n: instance.getInstance()) {
            for(Integer lit:n.getLiterals()) {
                if(!getLiteralSet().contains(lit*(-1)) && !getLiteralSet().contains(lit)) {
                    return false;
                }
            }
        }
        return true;
    }

    public Integer getSecondHighestDl(Nogood n) {
        int highest = 0;
        int secondHighest = 0;

        for(Integer lit: n.getLiterals()) {
            int dl = getDl(lit);
            if(dl > highest) {
                secondHighest = highest;
                highest = dl;
            } else if(dl > secondHighest) {
                secondHighest = dl;
            }
        }

        return secondHighest;
    }

    public int getRecentlyChanged() {
        return recentlyChanged;
    }

    public void setRecentlyChanged(Integer recentlyChanged) {
        this.recentlyChanged = recentlyChanged;
    }

}

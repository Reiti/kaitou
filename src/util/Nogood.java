package util;

import java.util.*;

public class Nogood {
    private Set<Integer> literals;

    public Nogood(Integer[] literals) {
        this.literals = new HashSet<>(Arrays.asList(literals));
    }

    public Nogood(Nogood n) {
        this.literals = new HashSet<>(n.getLiterals());
    }

    public Nogood(Set<Integer> n) {
        this.literals = new HashSet<>(n);
    }


    public Set<Integer> getLiterals() {
        return literals;
    }

    public Set<Integer> under(Assignment a) {
        Set<Integer> diff = new HashSet<>();
        for(Integer lit: literals) {
            if(a.getLiteralSet().contains(lit*(-1))) {
                return new HashSet<>();
            }
            else if(!a.getLiteralSet().contains(lit)) {
                diff.add(lit);
            }
        }

        return diff;
    }

    public boolean contains2WithDl(Assignment a, int dl) {
        List<Integer> lits = new ArrayList<>();
        for(Integer lit: literals) {
            if(a.getDl(lit) == dl) {
                lits.add(lit);
            }
        }

        return lits.size() >= 2;
    }

    //Only return if implicant isn't null (falsum)
    public Integer getWithDl(Assignment a, int dl) {
        for(Integer lit: literals) {
            if(a.getDl(lit) == dl && a.getImplicatn(lit)!=null) {
                return lit;
            }
        }
        return null;
    }
}

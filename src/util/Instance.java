package util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Instance {
    private List<Nogood> instance;

    public Instance(List<Nogood> instance) {
        this.instance = instance;
    }

    public List<Nogood> getInstance() {
        return instance;
    }

    public void setInstance(List<Nogood> instance) {
        this.instance = instance;
    }
    public void addNogood(Nogood n) {
        instance.add(n);
    }

    public Nogood findUnit(Assignment a) {
        Set<Integer> ls = a.getLiteralSet();
        for(Nogood n: instance) {
            Set<Integer> under = n.under(a);
            if(under.size() == 1){
                return n;
            }

        }
        return null;
    }

    public Nogood findSubset(Assignment a) {
        Set<Integer> ls = a.getLiteralSet();
        for(Nogood n: instance) {
            if(ls.containsAll(n.getLiterals())) {
                return n;
            }
        }

        return null;
    }

    public boolean isContained(Assignment a) {
        Set<Integer> ls = a.getLiteralSet();
        for(Nogood n: instance) {
            Set<Integer> intersection = new HashSet<>(n.getLiterals());
            intersection.retainAll(ls);
            if(intersection.size() == n.getLiterals().size()) { //Nogood is fully contained
                return true;
            }

        }
        return false;
    }

    public Integer select(Assignment a) {
        for(Nogood n: instance) {
            for(Integer lit: n.getLiterals()) {
                if(!a.getLiteralSet().contains(lit) && !a.getLiteralSet().contains(lit*(-1))) {
                    if(lit < 0) {
                        return lit;
                    }
                    else {
                        return lit*(-1);
                    }
                }
            }
        }
        return null;
    }
}

package util;

import java.util.*;

public class WatchedLiteralsUnitPropagation implements PropagationStrategy{
    private Map<Nogood, Set<Integer>> watchedLiterals;
    private Map<Integer, Set<Nogood>> watchingNogoods;

    public WatchedLiteralsUnitPropagation(Instance i) {
        watchedLiterals = new HashMap<>();
        watchingNogoods = new HashMap<>();
        for(Nogood n: i.getInstance()) {
            Iterator<Integer> lit = n.getLiterals().iterator();
            Set<Integer> watched = new HashSet<>();
            Integer l1 = lit.next();
            Set<Nogood> watchingN1 = watchingNogoods.get(l1);
            if(watchingN1 == null) {
                watchingN1 = new HashSet<>();
            }
            watchingN1.add(n);
            watched.add(l1);
            watchingNogoods.put(l1, watchingN1);
            if(lit.hasNext()) {
                Integer l2 = lit.next();
                Set<Nogood> watchingN2 = watchingNogoods.get(l2);
                if(watchingN2 == null) {
                    watchingN2 = new HashSet<>();
                }
                watchingN2.add(n);
                watched.add(l2);
                watchingNogoods.put(l2, watchingN2);
            }

            watchedLiterals.put(n, watched);
        }
    }

    public Assignment propagate(Instance i, Assignment a, Integer recentlyChanged) {
        Integer changedLiteral = recentlyChanged;
        Set<Integer> changedLits = new HashSet<>();
        changedLits.add(changedLiteral);
        while(!changedLits.isEmpty()) {
            Integer lit = changedLits.iterator().next();
            Assignment ap = propagationStep(i, a, lit);
            changedLits.remove(lit);
            changedLits.addAll(calculateChanged(a, ap));
            a = ap;
        }

        return a;
    }

    private Assignment propagationStep(Instance i, Assignment ap, Integer changedLiteral) {
        Assignment a = new Assignment(ap);
        if(watchingNogoods.get(changedLiteral) == null) {
            return a;
        }
        Set<Nogood> watching = watchingNogoods.get(changedLiteral);
        for(Nogood n: watching) {
            Set<Integer> w = new HashSet<>(watchedLiterals.get(n));
            Integer w2;
            if(w.size() == 1) {
                w2 = w.iterator().next();
            }
            else {
                w.remove(changedLiteral);
                w2 = w.iterator().next();
            }
            if(n.under(a).size() == 0) {
                continue;
            }
            else if(findOther(w2, n, a) != null) {
                Integer lit = findOther(w2, n, a);
                //Updating the watched literals in n
                Set<Integer> watched = new HashSet<>();
                watched.add(w2);
                watched.add(lit);
                watchedLiterals.put(n, watched);

                //Updating watching nogoods for the changed literal (sigma a)
                Set<Nogood> wn = new HashSet<>(watchingNogoods.get(changedLiteral));
                wn.remove(n);
                watchingNogoods.put(changedLiteral, wn);

                //Updating watching nogoods for the new literal (sigma' a')
                Set<Nogood> wn2 = watchingNogoods.get(lit);
                if (wn2 == null) {
                    wn2 = new HashSet<>();
                }
                wn2.add(n);
                watchingNogoods.put(lit, wn2);

            }
            else {
                int maxdl = a.getMaxDlWithoutLit(n, w2);
                a.addAssignment(w2*(-1), maxdl,n);


            }

        }


        return a;
    }

    private Integer findOther(Integer w2, Nogood n, Assignment a) {
        for(Integer lit: n.getLiterals()) {
            if(!a.getLiteralSet().contains(lit) && !a.getLiteralSet().contains(lit*(-1)) && !(lit.equals(w2))) {
                return lit;
            }
        }
        return null;
    }
    private Set<Integer> calculateChanged(Assignment a, Assignment ap) {
        Set<Integer> res = new HashSet<>();
        for (Integer lit : ap.getLiteralSet()) {
            if (!a.getLiteralSet().contains(lit)) {
                res.add(lit);
            }
        }
        return res;
    }

    public void addNogood(Nogood n, Assignment a, Integer rec) {
        Set<Integer> watched = new HashSet<>();
        if(n.getLiterals().size() == 1) {
            a.setRecentlyChanged(n.getLiterals().iterator().next());
            watched.add(n.getLiterals().iterator().next());
        }
        else {
            for (Integer lit : n.getLiterals()) {
                if (!a.getLiteralSet().contains(lit) && !a.getLiteralSet().contains(lit * (-1))) {
                    watched.add(lit);
                }
                if (watched.size() == 1) {
                    break;
                }
            }

            for(Integer lit: n.getLiterals()) {
                if(a.getLiteralSet().contains(lit) || a.getLiteralSet().contains(lit*(-1))) {
                    watched.add(lit);
                    a.setRecentlyChanged(lit);
                    break;
                }
            }
        }

        watchedLiterals.put(n, watched);

        for(Integer lit: watched) {
            Set<Nogood> watching = watchingNogoods.get(lit);
            if(watching == null) {
                watching = new HashSet<>();
            }
            watching.add(n);
            watchingNogoods.put(lit, watching);
        }


    }


}

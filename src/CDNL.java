import util.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("Duplicates")
public class CDNL {

    private static Map<Integer, Integer> guesses = new HashMap<>();

    public static boolean solve(Instance instance) {
        Integer current_dl = 0;
        Assignment a = new Assignment();

        while(true) {
            a = unitPropagationNaive(instance, a);
            if(instance.isContained(a) && current_dl == 0) {
                return false;
            }
            else if(instance.findSubset(a) != null && current_dl > 0) {
                Nogood conflict = instance.findSubset(a);
                Tuple<Nogood, Integer> nk = ConflictAnalysisFirstUIP(conflict, instance, a);
                Nogood nprime = nk.getFirst();
                Integer k = nk.getSecond();
                instance.addNogood(nprime);
                a.remove(k);
                current_dl = k;

            } else if(a.isComplete(instance)) {
                /*for(Triplet<Integer, Integer, Nogood> t: a.getAssignment()) {
                    Integer lit = t.getFirst();
                    Integer dl = t.getSecond();
                    System.out.println("Literal: "+lit +"@"+dl);
                }*/
                return true;
            }
            else {
                Integer I = instance.select(a);
                current_dl = current_dl + 1;
                setGuess(current_dl, I);
                a.addAssignment(I, current_dl, null);
            }
        }
    }

    private static void setGuess(Integer dl, Integer lit) {
        guesses.put(dl, lit);
    }

    private static Integer getGuess(Integer dl) {
        return guesses.get(dl);
    }

    public static Assignment unitPropagationNaive(Instance instance, Assignment curr) {
        int size = 0;
        do {
            size = curr.getLiteralSet().size();
            curr = unitPropagationStepNaive(instance, curr);
        } while(size != curr.getLiteralSet().size());

        return curr;
    }

    public static Assignment unitPropagationStepNaive(Instance instance, Assignment curr) {
        Nogood n = instance.findUnit(curr);
        while(n != null) {
            if(instance.isContained(curr)) {
                return curr;
            }

            Integer I = n.under(curr).iterator().next(); //We only have one element here, since n is unit under curr

            Integer decisionLevel = curr.getMaxDlWithoutLit(n, I);

            Integer Ineg = I*(-1);
            curr.addAssignment(Ineg, decisionLevel, n);

            n = instance.findUnit(curr);
        }

        return curr;
    }

    public static Tuple<Nogood, Integer> ConflictAnalysisFirstUIP(Nogood conflict, Instance instance, Assignment a) {
        Nogood c = new Nogood(conflict);
        int maxdl = a.getMaxDl(conflict);
        while(c.contains2WithDl(a, maxdl)) {
            Integer lit = c.getWithDl(a, maxdl);
            Set<Integer> newLitsC = new HashSet<>(c.getLiterals());
            newLitsC.remove(lit);
            Set<Integer> newLitsI = new HashSet<>(a.getImplicatn(lit).getLiterals());
            newLitsI.remove(lit*(-1));
            newLitsC.addAll(newLitsI);
            c = new Nogood(newLitsC);
        }

        Integer k = a.getSecondHighestDl(c);
        return new Tuple<Nogood, Integer>(c, k);
    }

}

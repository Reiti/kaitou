import util.Assignment;
import util.Instance;
import util.Nogood;

import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("Duplicates")
public class DPLL {

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
                Nogood n = instance.findSubset(a);
                int max = 0;
                for (int i = 1; i <= current_dl; i++) {
                    int g = getGuess(i);
                    if (a.getLiteralSet().contains(g) && a.getDl(g) > max) {
                        max = a.getDl(g);
                    }
                }

                int k = max - 1;
                a.remove(k);

                Integer I = getGuess(k + 1);
                current_dl = current_dl - 1;
                a.addAssignment(I * (-1), k, null);
            } else if(a.isComplete(instance)) {
               /* for(Triplet<Integer, Integer, Nogood> t: a.getAssignment()) {
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

}

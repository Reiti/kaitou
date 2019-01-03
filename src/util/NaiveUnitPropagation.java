package util;

public class NaiveUnitPropagation implements PropagationStrategy{

    public NaiveUnitPropagation() {

    }

    public Assignment propagate(Instance instance, Assignment curr, Integer recentlyChanged) {
        int size = 0;
        do {
            size = curr.getLiteralSet().size();
            curr = propagationStep(instance, curr);
        } while(size != curr.getLiteralSet().size());

        return curr;
    }

    private Assignment propagationStep(Instance instance, Assignment curr) {
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

    public void addNogood(Nogood n, Assignment a, Integer rec) {
        //Do nothing
    }
}

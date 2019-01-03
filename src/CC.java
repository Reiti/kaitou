import util.Instance;
import util.Nogood;
import util.Rule;
import util.Tuple;

import java.util.*;

public class CC {

    private static int newAtoms = 0;
    private static Map<Set<Integer>, Integer> bodyToBetaMap;
    public static Instance translate(List<Rule> program, int highestAtomName, Set<Integer> atoms) {
        bodyToBetaMap = new HashMap<>();
        newAtoms = highestAtomName + 1;
        List<Nogood> nogoods = new ArrayList<>();
        for(Rule r: program) { //Rules as implications
            nogoods.addAll(gamma(r.getBody()));
            int beta = bodyToBetaMap.get(r.getBody());
            Set<Integer> n = new HashSet<>();
            n.add(beta);
            n.add(r.getHead()*(-1));
            nogoods.add(new Nogood(n));
        }

        for(Integer at: atoms) {
            List<Rule> atAsHead = getRulesWithAtomAsHead(program, at);
            for(Rule r:atAsHead) {
                Set<Integer> n = new HashSet<>();
                n.add(at);
                n.add(bodyToBetaMap.get(r.getBody())*(-1));
                nogoods.add(new Nogood(n));
            }
        }

        return new Instance(nogoods);
    }

    private static List<Rule> getRulesWithAtomAsHead(List<Rule> program, Integer atom) {
        List<Rule> res = new ArrayList<>();
        for(Rule r: program) {
            if(r.getHead().equals(atom)) {
                res.add(r);
            }
        }
        return res;
    }

    private static List<Nogood> gamma(Set<Integer> body) {
        Set<Integer> fc = new HashSet<>(body);
        List<Nogood> nogoods = new ArrayList<>();
        int beta = getNewAtomName();
        fc.add(beta*(-1));
        nogoods.add(new Nogood(fc)); //Negative part of Gamma
        for(Integer lit: body) {
            Set<Integer> c = new HashSet<>();
            c.add(beta);
            c.add(lit*(-1));
            nogoods.add(new Nogood(c));
        }

        bodyToBetaMap.put(body, beta);
        return nogoods;

    }

    private static int getNewAtomName() {
        int atom = newAtoms;
        newAtoms++;
        return atom;
    }
}

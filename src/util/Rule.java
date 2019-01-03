package util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Rule {
    private Integer head;
    private Set<Integer> nbody;
    private Set<Integer> pbody;
    private Set<Integer> body;
    public Rule(Integer head, Set<Integer> nbody, Set<Integer> pbody) {
        this.head = head;
        this.nbody = nbody;
        this.pbody = pbody;
        this.body = new HashSet<>(pbody);
        for(Integer i: nbody) {
            body.add(i*(-1));
        }
    }

    public Set<Integer> getNbody() {
        return nbody;
    }

    public void setNbody(Set<Integer> nbody) {
        this.nbody = nbody;
    }

    public Set<Integer> getPbody() {
        return pbody;
    }

    public void setPbody(Set<Integer> pbody) {
        this.pbody = pbody;
    }

    public Integer getHead() {
        return head;
    }

    public Set<Integer> getBody() {
        return body;
    }
}

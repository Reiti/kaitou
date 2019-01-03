package util;

public interface PropagationStrategy {
    Assignment propagate(Instance i, Assignment a, Integer changedLiteral);
    void addNogood(Nogood n, Assignment a, Integer rec);
}

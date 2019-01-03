import util.Instance;
import util.Nogood;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("Duplicates")
public class Kaitou {
    public static void main(String[] args) throws IOException {
       /* Instance instance = readInput("C:\\Users\\Reiti\\Documents\\Uni\\Master\\STKR\\uf20-02-sat.cnf");
        System.out.println(DPLL.solve(instance));
        */
       //testSAT_DPLL();
       //testUNSAT_DPLL();
        testSAT_CDNL();
        testUNSAT_CDNL();
    }


    public static Instance readInput(String filename) throws IOException {
        List<String> lines = Files.lines(Paths.get(filename)).filter(s -> !s.startsWith("c")).collect(Collectors.toList());
        String format = lines.get(0);
        List<Nogood> nogoods = new ArrayList<>();
        for(int i = 1; i<lines.size(); i++) {
            String l = lines.get(i).trim();
            String[] split = l.split(" ");
            if(split.length == 0 || split.length == 1) {
                continue;
            }
            List<Integer> lits = new ArrayList<>();
            for(String s: split) {
                if(!s.equals("0") && !s.equals("") && !s.equals("%")) {
                    Integer lit = Integer.parseInt(s);
                    lits.add(lit * (-1));
                }
            }
            Integer[] arr = lits.toArray(new Integer[lits.size()]);
            nogoods.add(new Nogood(arr));
        }

        return new Instance(nogoods);
    }

    public static void testSAT_DPLL() {
        String path = "C:\\Users\\Reiti\\Documents\\Uni\\Master\\STKR\\uf20-91.tar\\uf20-91\\uf20-0";
        for(int i=1; i<=1000; i++) {
            String currp = path+i+".cnf";
            try {
                Instance in = readInput(currp);
                if(!DPLL.solve(in)) {
                    System.out.println("Error");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static void testUNSAT_DPLL() {
        String path = "C:\\Users\\Reiti\\Documents\\Uni\\Master\\STKR\\uuf50-218.tar\\uuf50-218\\UUF50.218.1000\\uuf50-0";
        for(int i=1; i<=1000; i++) {
            String currp = path+i+".cnf";
            System.out.println(i);
            try {
                Instance in = readInput(currp);
                if(DPLL.solve(in)) {
                    System.out.println("Error");
                    throw new RuntimeException();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static void testSAT_CDNL() {
        String path = "C:\\Users\\Reiti\\Documents\\Uni\\Master\\STKR\\uf20-91.tar\\uf20-91\\uf20-0";
        for(int i=1; i<=1000; i++) {
            String currp = path+i+".cnf";
            try {
                Instance in = readInput(currp);
                if(!CDNL.solve(in)) {
                    System.out.println("Error");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static void testUNSAT_CDNL() {
        String path = "C:\\Users\\Reiti\\Documents\\Uni\\Master\\STKR\\uuf50-218.tar\\uuf50-218\\UUF50.218.1000\\uuf50-0";
        for(int i=1; i<=1000; i++) {
            String currp = path+i+".cnf";
            System.out.println(i);
            try {
                Instance in = readInput(currp);
                if(CDNL.solve(in)) {
                    System.out.println("Error");
                    throw new RuntimeException();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}

import util.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("Duplicates")
public class Kaitou {
    public static void main(String[] args) throws IOException {
        Instance instance = readSatInput("C:\\Users\\Reiti\\Documents\\Uni\\Master\\STKR\\input.cnf");
        System.out.println(CDNL.solve(instance));

       /*
       long time = System.currentTimeMillis();
       testSAT_DPLL();
        System.out.println("DPLL-Sat: "+(System.currentTimeMillis()-time));
        time = System.currentTimeMillis();
       testUNSAT_DPLL();
        System.out.println("DPLL-UnSat: "+(System.currentTimeMillis()-time));
        time = System.currentTimeMillis();
        testSAT_CDNL();
        System.out.println("CDNL-Sat: "+(System.currentTimeMillis()-time));
        time = System.currentTimeMillis();
        testUNSAT_CDNL();
        System.out.println("CDNL-UnSat: "+(System.currentTimeMillis()-time));


        Tuple<List<Rule>, Map<Integer, String>> res = readASPInput("C:\\Users\\Reiti\\Documents\\Uni\\Master\\STKR\\coloring.lparse");

        List<Rule> program = res.getFirst();
        Map<Integer, String> dictionary = res.getSecond();
        for(Rule r: program) {
            String h = dictionary.get(r.getHead());
            System.out.print(h+"<-");
            for(Integer n: r.getNbody()) {
                System.out.print("not "+dictionary.get(n)+",");
            }
            for(Integer p: r.getPbody()) {
                System.out.print(dictionary.get(p)+",");
            }
            System.out.println("");
        }

        int max = dictionary.keySet().stream().max(Integer::compareTo).get();

        Instance i = CC.translate(program, max, dictionary.keySet());

        Set<String> answerSet = new HashSet<>();
        Assignment a = CDNL_ASP.solve(i);
        if(a.getLiteralSet().size() == 0) {
            System.out.println("No answer set");
        }
        else {
            for (Integer lit : a.getLiteralSet()) {
                boolean neg = lit < 0;
                if (neg) {
                    if (dictionary.get(lit * (-1)) != null) {
                        //answerSet.add("-" + dictionary.get(lit * (-1)));
                    }
                } else {
                    if (dictionary.get(lit) != null) {
                        answerSet.add(dictionary.get(lit));
                    }
                }
            }


            System.out.println("{");
            for (String l : answerSet) {
                System.out.println(l);
            }
            System.out.println("}");
        }
        */
    }

    public static Tuple<List<Rule>, Map<Integer, String>> readASPInput(String filename) throws IOException {
        Iterator<String> lines = Files.lines(Paths.get(filename)).iterator();
        String currline = lines.next();
        List<Rule> program = new ArrayList<>();
        Map<Integer, String> dict = new HashMap<>();
        List<Integer> alwaysTrue = new ArrayList<>(); //might need later
        List<Integer> alwaysFalse = new ArrayList<>(); //might need later
        while(!currline.startsWith("0")) { //Parse rules
            currline = currline.replaceAll("\\s+", " ");
            String[] split = currline.trim().split(" ");
            Integer head = Integer.parseInt(split[1]);
            int totalNumber = Integer.parseInt(split[2]);
            int noNeg = Integer.parseInt(split[3]);
            Set<Integer> nbody = new HashSet<>();
            Set<Integer> pbody = new HashSet<>();
            for(int i=4; i<(4 + noNeg); i++) {
                Integer n = Integer.parseInt(split[i]);
                nbody.add(n);
            }
            for(int i=(4 + noNeg); i<(4+totalNumber); i++) {
                Integer n = Integer.parseInt(split[i]);
                pbody.add(n);
            }
            program.add(new Rule(head, nbody, pbody));
            currline = lines.next();
        }
        currline = lines.next();
        while(!currline.startsWith("0")) { //Dictionary
            currline = currline.replaceAll("\\s+", " ");
            String[] split = currline.trim().split(" ");
            Integer lit = Integer.parseInt(split[0]);
            dict.put(lit, split[1]);
            currline = lines.next();
        }
        currline = lines.next(); //B+
        currline = lines.next();
        while(!currline.startsWith("0")) {
            String lit = currline.trim();
            alwaysTrue.add(Integer.parseInt(lit));
            currline = lines.next();
        }
        currline = lines.next(); //B-
        currline = lines.next();
        while(!currline.startsWith("0")) {
            String lit = currline.trim();
            alwaysFalse.add(Integer.parseInt(lit));
            currline = lines.next();
        }
        //done

        return new Tuple<List<Rule>, Map<Integer, String>>(program, dict);
    }


    public static Instance readSatInput(String filename) throws IOException {
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
                Instance in = readSatInput(currp);
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
            try {
                Instance in = readSatInput(currp);
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
                Instance in = readSatInput(currp);
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
            try {
                Instance in = readSatInput(currp);
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

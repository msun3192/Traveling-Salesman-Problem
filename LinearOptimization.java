import java.util.ArrayList;

public class LinearOptimization {
    public static void main(String[] args) {
        // Generate all possible pairs
        ArrayList<Pair> pairs = new ArrayList<>();
        for (Canada.Province from: Canada.Province.values()) {
            for (Canada.Province to: Canada.Province.values()){
                if(from.equals(to)){
                    continue;
                }
                Pair p = new Pair(from,to);
                if(!pairs.contains(p)) {
                    pairs.add(new Pair(from, to));
                }
            }
        }

        // target
        Region place = Region.find(args[0],Canada.Province.values());
        // all pairs generated
        String s = "";
        ArrayList<Pair> specificPairs = new ArrayList<>();
        ArrayList<Pair> allPairs = new ArrayList<>();
        for (Pair p: pairs) {
            if(p.x1.equals(place) || p.x2.equals(place)){
                specificPairs.add(p);
            }
            if(!allPairs.contains(p)){
                allPairs.add(p);
            }
        } //
        for (Pair p: allPairs) {
            System.out.println("var " + p.name + " >=0, <= 1;");
        }
        for (Canada.Province province: Canada.Province.values()) {
            System.out.print("subject to " + province.code() + ": ");
            String g = "";
            for (Pair p : allPairs) {
                if (p.x1.equals(province) || p.x2.equals(province)) {
                    g += p.name + " + ";
                }
            }
            g = g.substring(0,g.length()-3) + " = 2;";
            System.out.println(g);
        }

        System.out.println("minimize z:");
        s = "";
        for (Pair p: specificPairs) {
            s+= p.name + "*" + p.x2.capital().distance(p.x1.capital()) + " + ";
        }
        s = s.substring(0,s.length()-3) + ";";
        System.out.println(s);
        System.out.println("solve;");
        for (Pair pair: specificPairs) {
            System.out.println("printf " + pair.name + ";");
        }

    }
    private static class Pair{
        private Canada.Province x1;
        private Canada.Province x2;
        private String name;
        public Pair(Canada.Province to, Canada.Province from) {
            this.x1 = to;
            this.x2 = from;
            this.name = x1.code() + "_" + x2.code();
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof Pair){
                return this.equal((Pair) obj);
            }
            return false;
        }

        public boolean equal(Pair other) {
            return (x1.equals(other.x1) && x2.equals(other.x2)) || (x1.equals(other.x2) && x2.equals(other.x1));
        }
    }


}

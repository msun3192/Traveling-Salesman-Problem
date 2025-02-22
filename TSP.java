// Matthew Sun
// AI
// Mr. Paige
// 10/18/24
import java.util.ArrayList;
import java.util.Arrays;

public class TSP {
    public static void main(String[] args) {
        boolean nn = false;
        boolean far = false;
        boolean near = false;
        boolean random = false;
        int size = 3;
        int count = 1;
        int seed = 0;
        Region[] regions = null;
        boolean twoOPT = true;
        boolean stochasticHillClimb = false;
        ArrayList<Region> origins = new ArrayList<>();
        for (int i =0; i< args.length;i++) {
            switch (args[i]){
                case "-nn" -> nn =true;
                case "-far" -> far = true;
                case "-near" -> near = true;
                case "-random" -> random = true;
                case "-size" -> size = Integer.parseInt(args[++i]);
                case "-count" -> count = Integer.parseInt(args[++i]);
                case "-seed" -> seed = Integer.parseInt(args[++i]);
                case"-usa" -> regions = US.State.values();
                case"-canada" -> regions = Europe.Country.values();
                case"-europe" -> regions = Canada.Province.values();
                case"-caribbean" -> regions = Caribbean.Country.values();
                case"-northamerica" -> regions = NorthAmerica.Country.values();
                case "-americas" ->  regions = Americas.Country.values();
                case"-centralamerica" ->regions =  CentralAmerica.Country.values();
                case"-southamerica" -> regions =SouthAmerica.Country.values();
                case"-noTwoOpt" -> twoOPT = false;
                case "-shc" -> stochasticHillClimb = true;
                default -> {
                    Region r = Region.find(args[i], US.State.values()); // search all regions
                    if(r != null){
                        origins.add(r);
                        regions = US.State.values();
                        break;
                    }
                    r = Region.find(args[i],Canada.Province.values());
                    if(r != null){
                        origins.add(r);
                        regions = Canada.Province.values();
                        break;
                    }
                    r = Region.find(args[i],Europe.Country.values());
                    if(r != null){
                        origins.add(r);
                        regions = Europe.Country.values();
                    }
                    r = Region.find(args[i],Caribbean.Country.values());
                    if(r != null){
                        origins.add(r);
                        regions = Caribbean.Country.values();
                    }
                    r = Region.find(args[i],CentralAmerica.Country.values());
                    if(r != null){
                        origins.add(r);
                        regions = CentralAmerica.Country.values();
                    }
                    r = Region.find(args[i],NorthAmerica.Country.values());
                    if(r != null){
                        origins.add(r);
                        regions = NorthAmerica.Country.values();
                    }
                    r = Region.find(args[i],SouthAmerica.Country.values());
                    if(r != null){
                        origins.add(r);
                        regions = SouthAmerica.Country.values();
                    }

                }
            }
        }
        if(stochasticHillClimb){
            Tour t = Tour.stochasticHillClimbing(10,regions );
            System.out.println(t);
            return;
        }
        Tour tour = new Tour();
        if(!origins.isEmpty()) {
            if (far) {
                for (Region r : origins) {
                    tour.add(r);
                }
                Tour.far(tour, regions);
            } else if (near) {
                for (Region r : origins) {
                    tour.add(r);
                }
                Tour.close(tour, regions);
            } else if (random) {
                for (Region r : origins) {
                    tour.add(r);
                }
                Tour.random(tour, regions);
            }else if(nn){
                Tour[] tours = new Tour[origins.size()];
                for (int i = 0; i < origins.size(); i++) {
                    Tour t = new Tour();
                    t.add(origins.get(i));
                    tours[i] = t;
                }
                for (Tour t: tours) {
                    Tour.NearestNeighbor(t,regions);
                }
                for (int i = 1; i <= tours.length; i++) {
                    System.out.println("Tour #" + i);
                    for (Region s:tours[i-1].getTour()) {
                        System.out.println(s);
                    }
                    System.out.println();
                }

            }
        }else {
            Tour[] tours = new Tour[count];
            // pick random dataset to use
            regions = randomDataset();
            Region[] randomRegions = shuffle(regions);
            for (int i = 0; i < count; i++) {
                tours[i] = new Tour();
                for (int j = 0; j < size; j++) {
                    tours[i].add(randomRegions[j]);
                }
                randomRegions = shuffle(regions);
                if (far) {
                    Tour.far(tours[i], regions);
                } else if (near) {
                    Tour.close(tours[i], regions);

                } else if (random) {
                    Tour.random(tours[i], regions);
                } else if (nn) {
                    Tour.NearestNeighbor(tours[i], regions);
                }
            }
            if (twoOPT) {
                for (Tour t : tours) {
                    t.two_opt();
                }
            }
            for (int i = 1; i <= tours.length; i++) {
                Tour j = tours[i - 1];
                System.out.println(j);
                System.out.println();
            }

        }


    }
    public static Region[] shuffle(Region[] regions){
        for (int i = regions.length - 1; i > 0; i--) {
            int index = (int)(Math.random()* regions.length);
            // Simple swap
            Region a = regions[index];
            regions[index] = regions[i];
            regions[i] = a;
        }
        return regions;
    }
    public static Region[] randomDataset(){
        String[] allRegions = {"Americas", "Canada", "Caribbean", "CentralAmerica","Europe","NorthAmerica","SouthAmerica","US"};
        int randIndex = (int)(Math.random()*allRegions.length);
        switch (allRegions[randIndex]){
            case "Americas" -> {
                return Americas.Country.values();
            }
            case "Canada" -> {
                return Canada.Province.values();
            }
            case "Caribbean" -> {
                return Caribbean.Country.values();
            }
            case "CentralAmerica" -> {
                return CentralAmerica.Country.values();
            }
            case "Europe" -> {
                return Europe.Country.values();
            }
            case "NorthAmerica" -> {return NorthAmerica.Country.values();}
            case "SouthAmerica" -> {return SouthAmerica.Country.values();}
            case "US" -> {return  US.State.values();}
        }
        return null;
    }
}
// Matthew Sun
// AI
// Mr. Paige
// 10/18/24
import java.util.LinkedList;
import java.util.List;

import static java.util.Collections.*;

public class Tour {
    List<Region> tour = new LinkedList<>();


    public boolean contains(Region item) {
        for (Region list: tour) {
            if(item.code().equals(list.code())){
                return true;
            }
        }
        return false;
    }
    public void add(Region item){
        // check if item is in tour
        if(!tour.contains(item)){
            tour.add(item);
        }
    }
    public Region tail(){return tour.get(tour.size()-1);}
    public Region head(){return tour.get(0);}

    public List<Region> getTour() {
        return tour;
    }
    public static void far(Tour initial, Region[] regions){
        while(true) {
            double maxDist = -1;
            Region max = null;
            Region target = initial.tail();
            for (Region r : regions) {
                if (initial.contains(r)) {
                    continue;
                }
                double distance = r.capital().distance(target.capital());
                if (distance > maxDist) {
                    maxDist = distance;
                    max = r;
                }
            }
            if(max == null){
                return;
            }
            initial.add(max);
        }
    }
    public static void close(Tour initial, Region[] regions){
        while(true) {
            double minDist = -1;
            Region min = null;
            Region target = initial.tail();
            for (Region r : regions) {
                if (initial.contains(r)) {
                    continue;
                }
                double distance = r.capital().distance(target.capital());
                if (distance < minDist) {
                    minDist = distance;
                    min = r;
                }
            }
            if(min == null){
                return;
            }
            initial.add(min);
        }
    }
    public static void random(Tour initial, Region[] regions){
        int length = regions.length;
        for (int i = 0; i < length; i++) {
            int randIndex = (int)(Math.random()*length);
            Region temp = regions[i];
            regions[i] = regions[randIndex];
            regions[randIndex] = temp;
        }
        for (Region r: regions) {
            if(!initial.contains(r)){
                initial.add(r);
            }
        }
    }

    public static Tour NearestNeighbor(Tour tour, Region[] regions){
        Region nextRegion = nn(tour.tail(), tour,regions);
        while (nextRegion != null) {
            tour.add(nextRegion);
            nextRegion = nn(nextRegion, tour,regions);
        }
        return tour;
    }
    public static Region nn(Region origin, Tour tour, Region[] regions){
        double minDist = Double.MAX_VALUE;
        City originCity = origin.capital();
        Region minRegion = null;
        for (Region region: regions) { // find nearest neighbor
            if(tour.contains(region)){
                continue;
            }
            double distance = originCity.distance(region.capital());
            if (distance < minDist) {
                minRegion = region;
                minDist = distance;
            }
        }
        return minRegion;
    }
    public static Tour stochasticHillClimbing(Region[] randomRegions){
        return stochasticHillClimbing(20,randomRegions);
    }
    public static Tour stochasticHillClimbing(int numInitialTours, Region[] randomRegions) {
        Tour[] tours = new Tour[numInitialTours];
        if(randomRegions.length < numInitialTours){
            tours = new Tour[randomRegions.length];
        }
        for (int i = 0; i < tours.length; i++) {
            tours[i] = new Tour();
            tours[i].add(randomRegions[i]);
            NearestNeighbor(tours[i],randomRegions);
        }
        Tour best = tours[0];
        double bestDistance = Double.MAX_VALUE;
        for (Tour t: tours) {
            t.two_opt();
            double tDistance = totalDistance(t);
            if(tDistance < bestDistance){
                best = t;
                bestDistance = tDistance;
            }
        }
        return best;
    }
    public void two_opt() {
        boolean improvement = true;
        while (improvement) {
            improvement = false;
            for (int i = 0; i < tour.size(); i++) {
                for (int j = i + 1; j < tour.size(); j++) {
                    Tour newTour = two_OptSwap(i, j);
                    if (totalDistance(newTour) < totalDistance(this)) {
                        tour = newTour.tour;
                        improvement = true;
                    }
                }
            }
        }
    }
    public Tour two_OptSwap(int index1, int index2 ){
        LinkedList<Region> newTour = new LinkedList<>(tour.subList(0,index1));
        LinkedList<Region> reversedSegment = new LinkedList<>(tour.subList(index1, index2 + 1));
        reverse(reversedSegment);
        newTour.addAll(reversedSegment);
        // Add the rest of the tour after k
        newTour.addAll(tour.subList(index2 + 1, tour.size()));
        Tour t = new Tour();
        t.tour = newTour;
        return t;
    }
    public static double totalDistance(Tour t){
        double total = 0;
        Region r = t.head();
        for (int i = 1; i < t.tour.size(); i++) {
            total += r.capital().distance(t.tour.get(i).capital());
            r = t.tour.get(i);
        }
        total += t.head().capital().distance(t.tail().capital());
        return total;
    }

    @Override
    public String toString() {
        StringBuilder obj = new StringBuilder();
        for (Region r: tour) {
            obj.append(r.code()).append(" ");
        }
        obj = new StringBuilder(obj.substring(0, obj.length() - 1));
        return obj.toString();
    }
}

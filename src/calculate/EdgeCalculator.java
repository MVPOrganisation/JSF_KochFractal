package calculate;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;

/**
 * @author Max Meijer.
 */
public class EdgeCalculator implements Callable<ArrayList<Edge>>, Observer {

    private KochFractal kf;
    private int side = -1;
    private ArrayList<Edge> edges;

    public EdgeCalculator(int side) {
        this.kf = new KochFractal();
        this.side = side;
        kf.addObserver(this);
        edges = new ArrayList<>();
    }

    @Override
    public void update(Observable o, Object arg) {
        // Add the edge to local list
        edges.add((Edge) arg);
    }


    public void setLevel(int level) {
        kf.setLevel(level);
    }

    @Override
    public ArrayList<Edge> call() throws Exception {
        switch (side) {
            case 0:
                kf.generateBottomEdge();
                break;
            case 1:
                kf.generateLeftEdge();
                break;
            case 2:
                kf.generateRightEdge();
                break;
            default:
                System.out.println("Choose a side ya twat");
                return null;
        }
        return edges;
    }
}

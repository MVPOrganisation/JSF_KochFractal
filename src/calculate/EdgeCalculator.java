package calculate;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Max Meijer.
 */
public class EdgeCalculator implements Runnable, Observer {

    private KochManager km;
    private KochFractal kf;
    private int side = -1;
    private ArrayList<Edge> edges;

    public EdgeCalculator(KochManager km, KochFractal fractal, int side) {
        this.km = km;
        this.kf = fractal;
        this.side = side;
        kf.addObserver(this);
        edges = new ArrayList<>();
    }

    @Override
    public void run() {
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
                return;
        }

        // try and do all the calculating and copying the entire list
        //km.addEdges(edges);

        km.increaseCounter();
    }

    @Override
    public void update(Observable o, Object arg) {
        // Add the edge to local list
        //edges.add((Edge) arg);
        km.addEdge((Edge) arg);
    }
}

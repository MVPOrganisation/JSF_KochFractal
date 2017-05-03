package calculate;

import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import static java.lang.Thread.sleep;

/**
 * @author Max Meijer.
 */
public class EdgeCalculator extends Task<ArrayList<Edge>> implements Observer {

    private KochManager km;
    private KochFractal kf;
    private int side = -1;
    private ArrayList<Edge> edges;

    public EdgeCalculator(KochManager km, int side) {
        this.km = km;
        this.kf = new KochFractal();
        this.side = side;
        kf.addObserver(this);
        edges = new ArrayList<>();
    }

    @Override
    public void update(Observable o, Object arg) {
        // Add the edge to local list
        edges.add((Edge) arg);
        km.requestDrawTempEdge((Edge) arg);

        updateProgress(edges.size(), Math.pow(4, kf.getLevel() - 1));
        updateMessage("Nr edges: " + String.valueOf(edges.size()));

        try {
            sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
            kf.cancel();
            edges.clear();
        }


    }

    public void setLevel(int level) {
        kf.setLevel(level);
    }

    @Override
    protected ArrayList<Edge> call() throws InterruptedException {
            edges.clear();
            System.out.println("running at level: " + kf.getLevel());
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

            // try and do all the calculating and copying the entire list
            km.addEdges(edges);
            km.increaseCounter();

            if (km.getCounter() == 3) {
                km.requestDrawEdges();
            }

            return null;
    }
}

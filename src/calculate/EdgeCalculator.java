package calculate;

import timeutil.TimeStamp;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * @author Max Meijer.
 */
public class EdgeCalculator implements Callable<ArrayList<Edge>>, Observer {

    private KochFractal kf;
    private int side = -1;
    private ArrayList<Edge> edges;
    private CyclicBarrier barrier;

    EdgeCalculator(int side, CyclicBarrier barrier) {
        this.barrier = barrier;
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

    void setLevel(int level) {
        kf.setLevel(level);
    }

    @Override
    public ArrayList<Edge> call() throws Exception {
        TimeStamp ts = new TimeStamp();
        ts.setBegin("Thread: " + this + " starting calculation");
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
        ts.setEnd("Thread: " + this + " finished calculation");
        System.out.println(ts.toString());
        System.out.println("Thread: " + this + ", Generated: " + String.valueOf(edges.size() + " edges"));

        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException ex) {
            ex.printStackTrace();
        }

        return edges;
    }
}

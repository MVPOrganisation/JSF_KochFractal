package calculate;

import javafx.application.Platform;
import javafx.concurrent.Task;
import jsf31kochfractalfx.JSF31KochFractalFX;
import sun.nio.ch.ThreadPool;
import timeutil.TimeStamp;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Max Meijer
 * Creation date 15/03/2017.
 */
public class KochManager implements Observer {

    private KochFractal kf;
    private JSF31KochFractalFX application;
    private List<Edge> edges;
    private int counter;
    private ExecutorService pool;



    public KochManager(JSF31KochFractalFX application) {
        kf = new KochFractal();
        this.application = application;
        edges =  Collections.synchronizedList(new ArrayList());
        counter = 0;

    }

    public void changeLevel(int nxt) throws ExecutionException, InterruptedException {
        edges.clear();

        pool = Executors.newFixedThreadPool(3);

        EdgeCalculator edgeBot = new EdgeCalculator(0);
        EdgeCalculator edgeLeft = new EdgeCalculator(1);
        EdgeCalculator edgeRight = new EdgeCalculator(2);

        edgeBot.setLevel(nxt);
        edgeLeft.setLevel(nxt);
        edgeRight.setLevel(nxt);

        TimeStamp ts = new TimeStamp();


        ts.setBegin("Starting calculation");

        Future<ArrayList<Edge>> futLeft = pool.submit(edgeLeft);
        Future<ArrayList<Edge>> futRight = pool.submit(edgeRight);
        Future<ArrayList<Edge>> futBot = pool.submit(edgeBot);

        edges.addAll(futLeft.get());
        edges.addAll(futRight.get());
        edges.addAll(futBot.get());

        ts.setEnd("Finished calculation");
        requestDrawing();
        application.setTextCalc(ts.toString());
        counter = 0;

    }

    public void drawEdges() {
        TimeStamp tsd = new TimeStamp();
        tsd.setBegin("Start drawing");

        application.clearKochPanel();

        application.setTextNrEdges(String.valueOf(kf.getNrOfEdges()));
        for(Edge e: edges) {
            application.drawEdge(e);
        }

        tsd.setEnd("Finished drawing");
        application.setTextDraw(tsd.toString());
    }

    public void requestDrawing() {
        application.requestDrawEdges();
    }

    // Add a single edge to the local collection
    synchronized void addEdge(Edge edge) {
        edges.add(edge);
    }

    // Increase the counter to show what edge generation has finished
    synchronized  void increaseCounter() {
        counter++;
    }

    // Add all the calculated edges at once
    synchronized void addEdges(ArrayList<Edge> newEdges) {
        edges.addAll(newEdges);
    }

    @Override
    public void update(Observable o, Object arg) {
        edges.add((Edge) arg);
    }
}

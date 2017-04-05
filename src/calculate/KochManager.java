package calculate;

import jsf31kochfractalfx.JSF31KochFractalFX;
import timeutil.TimeStamp;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author Max Meijer
 * Creation date 15/03/2017.
 */
public class KochManager {

    private JSF31KochFractalFX application;
    private List<Edge> edges;
    private ExecutorService pool;
    private CyclicBarrier barrier;
    private Future<ArrayList<Edge>> futLeft;
    private Future<ArrayList<Edge>> futRight;
    private Future<ArrayList<Edge>> futBot;

    public KochManager(JSF31KochFractalFX application) {
        this.application = application;
        edges =  new ArrayList<>();
        barrier = new CyclicBarrier(4);
    }

    public void changeLevel(int nxt) throws ExecutionException, InterruptedException {
        edges.clear();
        pool = Executors.newFixedThreadPool(4);

        EdgeCalculator edgeBot = new EdgeCalculator(0, barrier);
        EdgeCalculator edgeLeft = new EdgeCalculator(1, barrier);
        EdgeCalculator edgeRight = new EdgeCalculator(2, barrier);
        WaitingThread waitThread = new WaitingThread(barrier, this);

        edgeBot.setLevel(nxt);
        edgeLeft.setLevel(nxt);
        edgeRight.setLevel(nxt);


        futLeft = pool.submit(edgeLeft);
        futRight = pool.submit(edgeRight);
        futBot = pool.submit(edgeBot);
        pool.submit(waitThread);
    }

    void getResults() {
        try {
            edges.addAll(futLeft.get());
            edges.addAll(futRight.get());
            edges.addAll(futBot.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        requestDrawing();
    }

    public void drawEdges() {
        TimeStamp tsd = new TimeStamp();
        tsd.setBegin("Start drawing");

        application.clearKochPanel();

        application.setTextNrEdges(String.valueOf(edges.size()));
        for(Edge e: edges) {
            application.drawEdge(e);
        }

        tsd.setEnd("Finished drawing");
        application.setTextDraw(tsd.toString());
    }

    void requestDrawing() {
        application.requestDrawEdges();
    }


}

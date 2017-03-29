package calculate;

import jsf31kochfractalfx.JSF31KochFractalFX;
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
public class KochManager {

    private KochFractal kf;
    private JSF31KochFractalFX application;
    private List<Edge> edges;
    private ExecutorService pool;

    public KochManager(JSF31KochFractalFX application) {
        kf = new KochFractal();
        this.application = application;
        edges =  Collections.synchronizedList(new ArrayList());
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

    private void requestDrawing() {
        application.requestDrawEdges();
    }
}

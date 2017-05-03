package calculate;

import javafx.concurrent.Task;
import javafx.scene.paint.Color;
import jsf31kochfractalfx.JSF31KochFractalFX;
import timeutil.TimeStamp;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Max Meijer
 * Creation date 15/03/2017.
 */
public class KochManager implements Observer {

    private JSF31KochFractalFX application;
    private List<Edge> edges;
    private int counter;
    private TimeStamp calculateTimer;
    private final ExecutorService pool;
    private int level;

    EdgeCalculator edgeBot;
    EdgeCalculator edgeLeft;
    EdgeCalculator edgeRight;

    public KochManager(JSF31KochFractalFX application) {
        this.application = application;
        edges =  Collections.synchronizedList(new ArrayList());
        counter = 0;
        pool = Executors.newFixedThreadPool(3);


    }

    public void changeLevel(int nxt) {
        if(edgeBot != null || edgeRight != null || edgeLeft != null) {
            edgeBot.cancel(true);
            edgeRight.cancel(true);
            edgeLeft.cancel(true);
        }

        edgeBot = new EdgeCalculator(this, 0);
        edgeLeft = new EdgeCalculator(this, 1);
        edgeRight = new EdgeCalculator(this, 2);

        edges.clear();
        level = nxt;

        edgeBot.setLevel(nxt);
        edgeLeft.setLevel(nxt);
        edgeRight.setLevel(nxt);

        application.bindBottomProgress(edgeBot);
        application.bindLeftProgress(edgeLeft);
        application.bindRightProgress(edgeRight);

        calculateTimer = new TimeStamp();
        calculateTimer.setBegin("Starting calculation");

        pool.submit(edgeBot);
        pool.submit(edgeLeft);
        pool.submit(edgeRight);
    }

    public void drawEdges() {
        calculateTimer.setEnd("Finished calculating");
        TimeStamp ts = new TimeStamp();

        ts.setBegin("Starting drawing");

        application.clearKochPanel();
        application.setTextNrEdges(String.valueOf((int)(3 * Math.pow(4, level - 1))));
        for(Edge e: edges) {
            application.drawEdge(e);
        }
        ts.setEnd("Finished drawing");
        application.setTextDraw(ts.toString());
        application.setTextCalc(calculateTimer.toString());
    }

    public void requestDrawTempEdge(Edge e) {
        application.requestDrawTempEdge(e);
    }

    public synchronized void drawTempEdge(Edge edge) {
        application.drawTempEdge(edge);
    }

    void requestDrawEdges() {
        counter = 0;
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

    int getCounter() {
        return counter;
    }

    void stopCalculateTimer() {
        calculateTimer.setEnd("Finished calculating");
    }

    void bindProgress() {

    }
}

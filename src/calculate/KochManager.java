package calculate;

import jsf31kochfractalfx.JSF31KochFractalFX;
import timeutil.TimeStamp;
import java.util.*;

/**
 * @author Max Meijer
 * Creation date 15/03/2017.
 */
public class KochManager implements Observer {

    private KochFractal kf;
    private JSF31KochFractalFX application;
    private List<Edge> edges;
    private int counter;
    private boolean edgeSet;
    private boolean counterSet;

    public KochManager(JSF31KochFractalFX application) {
        kf = new KochFractal();
        this.application = application;
        edges =  Collections.synchronizedList(new ArrayList());
        counter = 0;
    }

    public void changeLevel(int nxt) {
        edges.clear();

        kf.setLevel(nxt);
        TimeStamp ts = new TimeStamp();
        TimeStamp tsd = new TimeStamp();

        ts.setBegin("Starting calculation");
        EdgeCalculator edgeBot = new EdgeCalculator(this ,kf, 0);
        EdgeCalculator edgeLeft = new EdgeCalculator(this ,kf, 1);
        EdgeCalculator edgeRight = new EdgeCalculator(this ,kf, 2);

        Thread t1 = new Thread(edgeBot);
        Thread t2 = new Thread(edgeLeft);
        Thread t3 = new Thread(edgeRight);

        t1.start();
        t2.start();
        t3.start();

        while(counter != 3) {
            try {
                t1.join();
                t2.join();
                t3.join();
            } catch(InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        ts.setEnd("Finished calculation");
        tsd.setBegin("Start drawing");
        drawEdges();
        tsd.setEnd("Finished drawing");

        application.setTextCalc(ts.toString());
        application.setTextDraw(tsd.toString());
        System.out.println("Finished");
        counter = 0;
    }

    public void drawEdges() {
        application.clearKochPanel();

        application.setTextNrEdges(String.valueOf(kf.getNrOfEdges()));
        for(Edge e: edges) {
            application.drawEdge(e);
        }
    }

    public synchronized void addEdge(Edge edge) {
        if (edgeSet) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("InterruptedException caught");
            }
        }
        edges.add(edge);
        edgeSet = true;
        System.out.println("Edge added: " + edge);
        notify();
        edgeSet = false;
    }

    public synchronized  void increaseCounter() {
        if (counterSet) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("InterruptedException caught");
            }
        }
        counter++;
        counterSet = true;
        System.out.println("Counter at: " + counter);
        notify();

        counterSet = false;
    }

    @Override
    public void update(Observable o, Object arg) {
        edges.add((Edge) arg);
    }
}

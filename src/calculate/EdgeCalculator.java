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

    public EdgeCalculator(KochManager km, KochFractal fractal, int side) {
        this.km = km;
        kf = fractal;
        this.side = side;
        kf.addObserver(this);
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
        km.increaseCounter();
    }

    @Override
    public void update(Observable o, Object arg) {
        km.addEdge((Edge) arg);
    }
}

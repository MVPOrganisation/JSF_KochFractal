package calculate;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by max1_ on 15/03/2017.
 */
public class EdgyObserver implements Observer {

    public EdgyObserver () {
    }

    @Override
    public void update(Observable o, Object arg) {
        Edge e = (Edge) arg;

        System.out.println(String.format("Edge located at: X1:%f, Y1:%f to X2:%f, X2:%f", e.X1, e.Y1, e.X2, e.Y2));
    }
}

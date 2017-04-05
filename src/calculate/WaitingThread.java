package calculate;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by Max Meijer on 05/04/2017.
 * Fontys University of Applied Sciences, Eindhoven
 */
public class WaitingThread implements Runnable {

    private CyclicBarrier barrier;
    private KochManager manager;

    public WaitingThread(CyclicBarrier barrier, KochManager manager) {
        this.barrier = barrier;
        this.manager = manager;
    }


    @Override
    public void run() {
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException ex) {
            ex.printStackTrace();
        }

        manager.getResults();
    }
}

package com.company;

import calculate.Edge;
import calculate.EdgyObserver;
import calculate.KochFractal;
import javafx.beans.value.ObservableValue;

import java.util.Observable;
import java.util.Observer;

public class Main {

    public static void main(String[] args) {
	// write your code here
        KochFractal kf = new KochFractal();
        kf.setLevel(2);

        EdgyObserver eo = new EdgyObserver();
        kf.addObserver(eo);

        kf.generateBottomEdge();
        kf.generateLeftEdge();
        kf.generateRightEdge();

    }

}

package team2485.util;

import javafx.scene.canvas.Canvas;

/*  This file is from Team 6135, Arctos. We greatly appreciate the example on
    how to create our own custom widgets! Without this resource, and the videos by
    Team 3735, VorTX, I don't think that we would have figured this out at all.
    THANKS!!!!
 */

public class ResizableCanvas extends Canvas {
    
    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getHeight();
    }
}

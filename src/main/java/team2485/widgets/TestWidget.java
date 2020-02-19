package team2485.widgets;

import edu.wpi.first.shuffleboard.api.data.types.NumberType;
import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import team2485.util.ResizableCanvas;


//The Description and ParametrizedController annotations are required!
@Description(name = "2485 Test Widget", dataTypes = {NumberType.class} )
@ParametrizedController(value = "TestWidget.fxml")
public class TestWidget extends SimpleAnnotatedWidget<Number> {

    //Standard FXML stuff created in the FXML file...
    @FXML
    public Label numberLabel;
    @FXML
    public TextField numberField;
    @FXML
    public Button clicky;
    @FXML
    public StackPane imgParentPane;
    @FXML
    protected Pane zPaneZPane;
    @FXML
    private ResizableCanvas canvas;


    //The GraphicsContext is the "pen" that can draw shapes and images
    private GraphicsContext gc;

    //An image to draw...and data to use in the drawings
    private Image peppaLives;
    double angle;
    private int centerX, centerY, size;


    //Note: no constructor. All initialization logic can take place in the initialize()
    //method...if you do provide a constructor, you MUST provide a default (no parameter)
    //constructor. This is a JavaFX requirement.



    //Mandatory...will likely do this exact thing (but change your variable name as appropriate).
    //The returned object should be your top-level Pane in the FXML document.
    @Override
    public Pane getView() {
        return zPaneZPane;
    }


    //This is called for us by the JavaFX engine
    @FXML
    private void initialize() {
        gc = canvas.getGraphicsContext2D();


        peppaLives = new Image(TestWidget.class.getResourceAsStream("/peppa.png"));
        size = 300;
        centerX = centerY = size/2;
        angle = 0;

        /*  CRUCIAL! This sets up the widget to react to changing data values. As the
            robot code streams values back to Shuffleboard, we want our widgets to
            automatically get notified and then make appropriate updates to the widget.
            This widget only listens for a single number, so we grab the newValue, convert
            it to a decimal, and then use that as the rotation angle (used by the draw
            method). Then we call draw() or else the update will not appear onscreen.
         */
        dataProperty().addListener((observable, oldValue, newValue) -> {
            angle = newValue.doubleValue();
            draw();
        });



        // This is code from Team 6135, Arctos. Thanks for sharing!! In particular,
        // this uses their ResizableCanvas object and some resizing code below
        // to make the canvas behave nicely. I haven't tried using a simple Canvas
        // object (which is a standard JavaFX class that they used as a superclass
        // for their ResizableCanvas class).
        // Set the minimum size
        // This is required, otherwise it won't resize properly when getting smaller
        imgParentPane.setMinSize(0, 0);
        // Set up the resizable canvas
        canvas.widthProperty().bind(imgParentPane.widthProperty());
        canvas.heightProperty().bind(imgParentPane.heightProperty());

        // Add a change listener to the size of the canvas and aspect ratio
        ChangeListener<Object> resizeListener = (observable, oldValue, newValue) -> {
            // If the settings change, redraw the image
            draw();
        };
        canvas.widthProperty().addListener(resizeListener);
        canvas.heightProperty().addListener(resizeListener);
    }



    /*  This method is called from our dataProperty().addListener...event handler above.
        There is nothing special about the name of the method...it is never called for
        us automatically unless we set it up to be automatically called (which the
        event handling logic above does in fact set up).
     */
    private void draw() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(Color.BLUE);
        gc.fillOval(0, 0, size, size);

        gc.translate(centerX, centerY);
        gc.rotate( -angle/2);
        gc.drawImage(peppaLives, -size/2, - size/2,
                    size, size);
        gc.rotate( angle/2);
        gc.translate( -centerX, -centerY);

        gc.setFill(Color.RED);

        gc.translate(centerX, centerY);
        gc.rotate(angle);
        gc.fillRect(-15, 0, 30, size);
        gc.rotate(-angle);
        gc.translate(-centerX, -centerY);

        numberField.setText(""+angle);
    }

    /*  This is just a demo method...it is only called by the Button in our FXML
        document. Look at the onAction attribute for the Button inside of TestWidget.fxml.
     */
    public void rotate(ActionEvent actionEvent) {
        angle += 6;
        angle %= 360;
        draw();
    }
}

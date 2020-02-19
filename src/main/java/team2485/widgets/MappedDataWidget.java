package team2485.widgets;

import edu.wpi.first.shuffleboard.api.data.MapData;
import edu.wpi.first.shuffleboard.api.data.types.MapType;
import edu.wpi.first.shuffleboard.api.prefs.Group;
import edu.wpi.first.shuffleboard.api.prefs.Setting;
import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import team2485.util.ResizableCanvas;

import java.util.List;

/*  Note that we are subclassing SimpleAnnotatedWidget because we only have one data
    object...but that data object is a map, which means that it can store many values
    (similar to an array but values are stored by some "key", a String in this case
    that will specify what logical value is associated with it). With an array, you
    can get the value associated with a position like 0; with a map, you can get the
    value associated with anything, or with "gyro" or "encoder" for example.
 */
//The Description and ParametrizedController annotations are required!
@Description(name = "2485 Mapped Widget", dataTypes = {MapType.class})
@ParametrizedController(value = "MappedDataWidget.fxml")
public class MappedDataWidget extends SimpleAnnotatedWidget<MapData> {

    /*  These properties correspond to the widget's configurable properties. They are String
        properties where the "value" set on the widget's "Edit Properties" page is the named
        value...but that named value is then used as a key in a map to retrieve the data associated
        with the named value. That's a rough sentence to parse.
     */
    private final SimpleStringProperty gyroValue = new SimpleStringProperty(this, "gyroValue", "");
    private final SimpleStringProperty encValue = new SimpleStringProperty(this, "encoderValue", "");


    /*  Just references to the objects created in FXML. Note that we don't have to
        initialize these objets...JavaFX does that for us.
     */
    @FXML
    protected Pane thePane;
    @FXML
    public StackPane imgParentPane;
    @FXML
    private ResizableCanvas canvas;
    @FXML
    public Label number1Label, number2Label;
    @FXML
    public TextField number1Field, number2Field;

    /*  Extra variables to do the things we need to do...keep track of a couple of
        angles, an image to draw things...some numbers and stuff. The GraphicsContext
        object is the "pen" that can set colors, draw shapes and images, etc.
     */
    private Image peppaLives;
    double angle1, angle2;
    private int centerX, centerY, size;

    private GraphicsContext gc;


    /*  We don't actually need a constructor but, if you do provide a constructor, then
        you need a default (no parameters) constructor as JavaFX requires it.
     */




    /*  This method exports the settings that we want to be configurable. This example
        creates two values that users can bind to the widget. In the "Properties"
        section of the widget (in Shuffleboard, right click the widget and choose
        "Edit Properties"), you will see the "name" values below. These are then configured
        to go with the SimpleStringProperty objects gyroValue and encValue. This maps
        the "properties" to our code objects. We can then retrieve the actual data values
        via the dataProperty() method, which returns a map of data values.
     */
    @Override
    public List<Group> getSettings() {
        return List.of(
                Group.of("Widget Options",
                        Setting.of("Gyro Value", gyroValue, String.class),
                        Setting.of("Encoder Value", encValue, String.class)
                )
        );
    }


    //Mandatory...should return the top-level Pane defined in the FXML file
    @Override
    public Pane getView() {
        return thePane;
    }


    /*  This method gets called for us by the JavaFX engine. It should be used
        to *INITIALIZE* your widget (you probably didn't see that one coming... :)
     */
    @FXML
    private void initialize() {

        gc = canvas.getGraphicsContext2D();

        //Load the image relative to the widget's code base
        peppaLives = new Image(MappedDataWidget.class.getResourceAsStream("/peppa.png"));
        size = 300;
        centerX = centerY = size / 2;
        angle1 = angle2 = 0;

        //CRUCIAL!!
        /*  This sets up a listener to react to changing data values...when
            the data bound to the widget updates, this code runs.
         */
        dataProperty().addListener( (observable, oldValue, newValue) -> {
            //From the dataProperty(), use get() to access the map...it's a map
            //because we are using MapType and MapData for the data types in this
            //widget. From the map, get the value associated with the name bound
            //to the gyroValue (whatever was entered on the "Properties" page of
            //the widget). Cast this value to a double (since the map's get method
            //returns a generic object and we know it should be a double).
            angle1 = (double) dataProperty().get().get(gyroValue.getValue());

            angle2 = (double) dataProperty().get().get(encValue.getValue());
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

    /*  This method is nothing special (not inherited or called automatically). It
        simply draws the interface and updates the number fields/labels. It is called
        from the "change listener" code found under dataProperty().addListener...
     */
    private void draw() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(Color.BLUE);
        gc.fillOval(0, 0, size, size);

        //draw Peppa rotated by angle1
        gc.translate(centerX, centerY);
        gc.rotate( angle1);
        gc.drawImage(peppaLives, -size/2, - size/2,
                size, size);
        gc.rotate( -angle1);
        gc.translate( -centerX, -centerY);

        gc.setFill(Color.RED);

        //draw needle rotated by angle2
        gc.translate(centerX, centerY);
        gc.rotate(angle2);
        gc.fillRect(-15, 0, 30, size);
        gc.rotate(-angle2);
        gc.translate(-centerX, -centerY);

        number1Field.setText(""+angle1);
        number2Field.setText(""+angle2);
        number1Label.setText(gyroValue.getValue());
        number2Label.setText(encValue.getValue());

        //For testing...
//        gc.setStroke(Color.BLACK);
//        gc.strokeText(gyroValue.getValue(), 0, 20);
//        gc.strokeText(gyroValue.get(), 0, 40);
    }
}

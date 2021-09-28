package RayTracer18;


import RayTracer18.Light.PointLight;
import RayTracer18.Primitives.Material;
import RayTracer18.Primitives.Plane;
import RayTracer18.Primitives.Sphere;
import RayTracer18.Primitives.Triangle;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    Button button;
    Slider slider;
    Label label;

    Scene3D scene = new Scene3D();


    @Override
    public void start(Stage primaryStage) throws Exception {
        Canvas canvas = new Canvas(600, 300);

        primaryStage.setTitle("Ray tracer");


        label = new Label();
        label.setText(String.format("Field of View (FoV): %.1f ", Math.abs(.3 * 100 - 100)));

        slider = new Slider();
        slider.setMin(0);
        slider.setMax(1);
        slider.setValue(.3);
        slider.setBlockIncrement(1);
        slider.setMajorTickUnit(.1);
        slider.setMinorTickCount(0);
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                scene.camera.setFov(newValue.doubleValue());
                label.setText(String.format("Field of View (FoV): %.1f ", Math.abs((double) newValue * 100 - 100)));
            }
        });

        button = new Button();
        button.setText("Trace da rays");
        button.setOnAction(e -> {
            Material blue = new Material(Color.BLUE);
            Material green = new Material(Color.GREEN);
            Material floorm = new Material(Color.ORANGE);

            Triangle floor = new Triangle(
                    new Vector3(0,-0.5,-10),
                    new Vector3(-10,-0.5,0),
                    new Vector3(12,-0.5,12)
            );
            floor.applyMaterial(floorm);
            scene.add(floor);

            Triangle t = new Triangle(new Vector3(-3, 0, 4), new Vector3(0, 6, 4), new Vector3(3, 0, 4));
            scene.add(t);
            t.applyMaterial(blue);





            //TODO: make this working correclty
            /*Plane p = new Plane(.2);
            scene.add(p);
            p.applyMaterial(green);*/

            Sphere s = new Sphere(new Vector3(0, -.5,1.2), 1);
            s.applyMaterial(green);
            scene.add(s);

            PointLight l = new PointLight(new Vector3(-2, 1, 0), 1f, Color.WHITE);
            scene.add(l);
            scene.camera.setProjectorSize(new Vector2(canvas.getWidth(), canvas.getHeight()));
            new Renderer().renderScene(scene, canvas);
        });


        BorderPane borderPane = new BorderPane();
        GridPane gridPane = new GridPane();

        gridPane.addRow(1, button);
        gridPane.addRow(2, label);
        gridPane.addRow(3, slider);

        GridPane mainc = new GridPane();
        mainc.getChildren().add(canvas);


        HBox statusbar = new HBox();
        Label l = new Label("Text");
        borderPane.setTop(gridPane);
        borderPane.setCenter(mainc);
        borderPane.setBottom(statusbar);

        primaryStage.setScene(new Scene(borderPane, 600, 600));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
package RayTracer18;


import RayTracer18.Light.PointLight;
import RayTracer18.Material.Material;
import RayTracer18.Primitives.*;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

    BorderPane borderPane;
    GridPane gridPane;
    ColorPicker colorPicker;

    Scene3D scene = new Scene3D();
    Canvas canvas = new Canvas(600, 300);

    public void addMouseScrolling(Node node) {
        node.setOnScroll((ScrollEvent event) -> {
            // Adjust the zoom factor as per your requirement
            double zoomFactor = 1.05;
            double deltaY = event.getDeltaY();
            scene.camera.zoomCamera(canvas, scene, deltaY);
        });
    }

    TreeItem<String> rootHierarchy = new TreeItem<String>("Entities");
    TreeItem<String> rootObjects = new TreeItem<String>("Objects");
    TreeItem<String> rootLights = new TreeItem<String>("Lights");

    public void createHierarchy() {
        ArrayList<String> objectList = new ArrayList<>();
        ArrayList<String> lightList = new ArrayList<>();

        objectList.add(scene.getObjects().toString());
        lightList.add(scene.getLights().toString());
        rootObjects.setExpanded(true);
        rootLights.setExpanded(true);
        for (int i = 0; i < objectList.size(); i++) {
            TreeItem<String> item = new TreeItem<String>(objectList.get(i));
            rootObjects.getChildren().add(item);
        }
        for (int i = 0; i < lightList.size(); i++) {
            TreeItem<String> item = new TreeItem<String>(lightList.get(i));
            rootLights.getChildren().add(item);
        }
    }


    private void customizeLights() {
        var lights = scene.getLights();
        var label = new Label();
        label.setText("Lights");
        gridPane.add(label, 4, 0);

        for (int i = 0; i < lights.size(); i++) {
            //Intensity slider
            var slider = new Slider();
            slider.setValue(lights.get(0).intensity);
            slider.setMin(0f);
            slider.setMax(10f);
            slider.setValue(2f);
            slider.setBlockIncrement(1);
            slider.setMajorTickUnit(2);
            slider.setMinorTickCount(1);
            slider.setShowTickLabels(true);
            slider.setSnapToTicks(true);
            gridPane.add(slider, 5, i + 1);

            //Colorpicker
            var colorPicker = new ColorPicker();
            colorPicker.setValue(lights.get(i).color);
            gridPane.add(colorPicker, 3, i + 1);


            //TODO: do something with position
//            lights.get(i).setPosition(new Vector3(0,0,0));

            int currentLight = i;
            //Sets intensity of current light
            slider.valueProperty().addListener((observable, oldValue, newValue) -> {
                lights.get(currentLight).setIntensity((Double) newValue);
            });
            //Sets color of current light
            colorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
                lights.get(currentLight).setColor(newValue);
            });

        }

        //Creates button and applies light changes
        var button = new Button();
        button.setText("Apply");
        button.setOnAction(e -> {
            System.out.println(lights);
            Renderer.renderScene(scene, canvas);
            createHierarchy();
        });
        gridPane.add(button, 4, 5);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        addMouseScrolling(canvas);

        primaryStage.setTitle("Ray tracer");


        Label label = new Label();
        label.setText(String.format("Field of View (FoV): %.1f ", Math.abs(.3 * 100 - 100)));

        Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax(1);
        slider.setValue(.3);
        slider.setBlockIncrement(1);
        slider.setMajorTickUnit(.1);
        slider.setMinorTickCount(0);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            scene.camera.setFov(newValue.doubleValue());
            label.setText(String.format("Field of View (FoV): %.1f ", Math.abs((double) newValue * 100 - 100)));


            Renderer.renderScene(scene, canvas);

        });

        Button button = new Button();
        button.setText("Trace da rays");
        button.setOnAction(e -> {
            initRender(scene, canvas);

            createHierarchy();

            customizeLights();
        });

        TreeView<String> tree = new TreeView<>(rootHierarchy);
        rootHierarchy.getChildren().add(rootObjects);
        rootHierarchy.getChildren().add(rootLights);
        tree.setShowRoot(false);
        tree.setMaxHeight(150);

        gridPane.addRow(1, button);
        gridPane.addRow(2, label);
        gridPane.addRow(3, slider);
        gridPane.addRow(4, tree);

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


    public static void initRender(Scene3D scene, Canvas canvas) {
        Material blue = new Material(Color.BLUE);
        Material green = new Material(Color.GREEN);
        Material floorm = new Material(Color.ORANGE);


        Triangle t = new Triangle(
                new Vector3(0, 0, 5),
                new Vector3(-3, 0, 5),
                new Vector3(0, 6, 5),
                new Vector3(3, 0, 5));

        scene.add(t);
        t.applyMaterial(blue);


        //TODO: make this working correclty
        Plane p = new Plane(new Vector3(0, -0.5, 0), new Vector3(0, 1, 0));
        scene.add(p);
        p.applyMaterial(green);
        Plane p2 = new Plane(new Vector3(0, 10, 0), new Vector3(0, 1, 0));
        scene.add(p2);
        p2.applyMaterial(green);
        Plane p3 = new Plane(new Vector3(0, 0, 10), new Vector3(0, 0, -1));
        scene.add(p3);
        p3.applyMaterial(green);

        Sphere s = new Sphere(new Vector3(2, 0.5, 2), 1);
        s.applyMaterial(green);


        scene.add(s);


        PointLight l = new PointLight(new Vector3(2,3,0), 1f, Color.WHITE);
        scene.add(l);
        scene.camera.setProjectorSize(new Vector2(canvas.getWidth(), canvas.getHeight()));
        new Renderer().renderScene(scene, canvas);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
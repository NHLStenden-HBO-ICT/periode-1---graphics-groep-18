package RayTracer18.Material;

import RayTracer18.Vector3;
import javafx.scene.paint.Color;

public class Material {
    public Color color;
    private double reflection;


    public boolean isChecker = false;
    public Material(Color c){
        this.color = c;
    }



    public void setReflection(double v){
        this.reflection = v;
    }

    //Use this later with parameters hitPoint to look up the pixel in the maps if needed
    public Color getColorAt(){
        return color;
    }
    public Color getColorAt(Vector3 point){
        return color;
    }

    public double getReflection(){
        return reflection;
    }

    @Override
    public String toString() {
        return "Material{" +
                "color=" + color +
                '}';
    }
}

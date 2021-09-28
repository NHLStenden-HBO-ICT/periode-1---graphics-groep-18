package RayTracer18.Light;

import RayTracer18.Vector3;
import javafx.scene.paint.Color;

public abstract class Light {
    public float intensity;
    public Color color;
    public Vector3 position;


    public Light(Vector3 position, float intensity, Color color){
        this.intensity = intensity;
        this.color = color;
        this.position = position;
    }

}

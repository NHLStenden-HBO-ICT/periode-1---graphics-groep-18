package RayTracer18.Primitives;

import RayTracer18.Ray;
import RayTracer18.Vector3;

public class Plane extends Object3D{


    private Vector3 normal;

    public Plane(Vector3 pos, Vector3 normal) {
        super(pos);
        this.normal = normal;
        this.name = "Plane";


    }

    @Override
    public Vector3 calculateIntersection(Ray ray) {
        double dem = Vector3.dot(this.normal, ray.getDirection());

        if(Math.abs(dem) > 0.0001){
            Vector3 dif = Vector3.sub(this.position, ray.getOrigin());
            double t = Vector3.dot(dif, this.normal) / dem;
            if(t > 0.0001){
                return ray.getDirection().multiplyScalar(t);
            }
        }
        return null;

    }

    @Override
    public Vector3 getNormalAt(Vector3 pos) {

        return this.normal;
    }
}

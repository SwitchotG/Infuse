package org.infuse.plugin.Class.Ray;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class Ray {

    public static final BuilderCodec<Ray> CODEC = BuilderCodec.builder(Ray.class, Ray::new)
            .append(new KeyedCodec<>("Resistance", Codec.INTEGER),
                    (comp, resistance) -> comp.resistance = resistance,
                    comp -> comp.resistance)
            .add()
            .append(new KeyedCodec<>("Power", Codec.INTEGER),
                    (comp, power) -> comp.power = power,
                    comp -> comp.power)
            .add()
            .append(new KeyedCodec<>("Ray Type", RayType.CODEC),
                    (comp, rayType) -> comp.rayType = rayType,
                    comp -> comp.rayType)
            .add()
            .build();

    private int resistance;

    private int power;

    private RayType rayType;

    public Ray(){
        resistance = 0;
    }

    public Ray(int resistance, int power, RayType rayType) {
        this.resistance = resistance;
    }

    public int getResistance() {
        return resistance;
    }

    public void setResistance(int resistance) {
        this.resistance = resistance;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public RayType getRayType() {
        return rayType;
    }

    public void setRayType(RayType rayType) {
        this.rayType = rayType;
    }

    public Ray clone(){
        return new Ray(this.resistance, this.power, this.rayType);
    }
}

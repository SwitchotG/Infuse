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
            .build();

    private int resistance;

    public Ray(){
        resistance = 0;
    }

    public Ray(int resistance) {
        this.resistance = resistance;
    }

    public int getResistance() {
        return resistance;
    }

    public void setResistance(int resistance) {
        this.resistance = resistance;
    }

    public Ray clone(){
        return new Ray(this.resistance);
    }
}

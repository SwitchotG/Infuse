package org.infuse.plugin.Class.Ray;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.codecs.EnumCodec;

public enum RayModificationType {
    AddResistance {
        @Override
        public void modify(Ray ray, Ray modification){
            ray.setResistance(ray.getResistance() + modification.getResistance());
        }
    },
    SubstractResistance {
        @Override
        public void modify(Ray ray, Ray modification){
            ray.setResistance(ray.getResistance() - modification.getResistance());
        }
    },
    MultiplyResistance {
        @Override
        public void modify(Ray ray, Ray modification){
            ray.setResistance(ray.getResistance() * modification.getResistance());
        }
    },
    DivideResistance {
        @Override
        public void modify(Ray ray, Ray modification){
            ray.setResistance(ray.getResistance() / modification.getResistance());
        }
    };


    public abstract void modify(Ray ray, Ray modification);

    public static final Codec<RayModificationType> CODEC =
            new EnumCodec<>(RayModificationType.class);
}

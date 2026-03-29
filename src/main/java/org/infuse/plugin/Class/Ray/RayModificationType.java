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
    },
    ChangeResistance {
        @Override
        public void modify(Ray ray, Ray modification){
            ray.setResistance(modification.getResistance());
        }
    },
    AddPower {
        @Override
        public void modify(Ray ray, Ray modification){
            ray.setPower(ray.getPower() + modification.getPower());
        }
    },
    SubstractPower {
        @Override
        public void modify(Ray ray, Ray modification){
            ray.setPower(ray.getPower() - modification.getPower());
        }
    },
    MultiplyPower {
        @Override
        public void modify(Ray ray, Ray modification){
            ray.setPower(ray.getPower() * modification.getPower());
        }
    },
    DividePower {
        @Override
        public void modify(Ray ray, Ray modification){
            ray.setPower(ray.getPower() / modification.getPower());
        }
    },
    ChangePower {
        @Override
        public void modify(Ray ray, Ray modification){
            ray.setPower(modification.getPower());;
        }
    },
    ChangeType {
        @Override
        public void modify(Ray ray, Ray modification){
            ray.setRayType(modification.getRayType());
        }
    };


    public abstract void modify(Ray ray, Ray modification);

    public static final Codec<RayModificationType> CODEC =
            new EnumCodec<>(RayModificationType.class);
}

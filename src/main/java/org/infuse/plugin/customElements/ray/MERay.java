package org.infuse.plugin.customElements.ray;

import org.infuse.plugin.customElements.ray.MagicType;

public class MERay {

    private final int power;

    private final int resistance;

    private final MagicType magicType;

    public MERay(){
        this.power = 0;
        this.resistance = 0;
        this.magicType = MagicType.NOTHING;
    }

    public MERay(int power, int resistance, MagicType magicType){
        this.power = power;
        this.resistance = resistance;
        this.magicType = magicType;
    }

    public MERay(MERay otherMagicRay){
        this.power = otherMagicRay.power;
        this.resistance = otherMagicRay.resistance;
        this.magicType = otherMagicRay.magicType;
    }

    public int getPower(){
        return power;
    }

    public int getResistance(){
        return resistance;
    }

    public MagicType getType(){
        return magicType;
    }

    public MERay withPower(int power){
        return new MERay(this.power, this.resistance, this.magicType);
    }

    public MERay withResistance(int resistance){
        return new MERay(this.power, resistance, this.magicType);
    }

    public MERay withType(MagicType magicType){
        return new MERay(this.power, this.resistance, magicType);
    }

}

package org.infuse.plugin.Class.Ray;

import org.infuse.plugin.InfusePlugin;

import java.util.UUID;

public class RayMapData {

    private final UUID uuid;
    private UUID ray;
    private UUID otherRay;
    private int otherResistance;
    private int resistance;

    public RayMapData(){
        this.uuid = UUID.randomUUID();
    }

    public RayMapData(UUID ray, int resistanceRemaining){
        this.uuid = UUID.randomUUID();
        this.ray = ray;
        this.resistance = resistanceRemaining;
    }

    public RayMapData(UUID ray, UUID otherRay, int otherResistance, int resistance){
        this.uuid = UUID.randomUUID();
        this.ray = ray;
        this.otherRay = otherRay;
        this.otherResistance = otherResistance;
        this.resistance = resistance;
    }

    public UUID getUuid() {
        return uuid;
    }

    public UUID getRay() {
        return ray;
    }

    public void setRay(UUID ray) {
        this.ray = ray;
    }

    public UUID getOtherRay() {
        return otherRay;
    }

    public void setOtherRay(UUID otherRay) {
        this.otherRay = otherRay;
    }

    public int getOtherResistance() {
        return otherResistance;
    }

    public void setOtherResistance(int otherResistance) {
        this.otherResistance = otherResistance;
    }

    public int getResistance() {
        return resistance;
    }

    public void setResistance(int resistance) {
        this.resistance = resistance;
    }

    public boolean hasRay(){
        return ray == null;
    }

    public int getResistanceRemaining(){
        return this.getResistance() - this.getOtherResistance();
    }

    public int collideWith(UUID ray, int resistance){
        if(resistance > this.getResistance()){
            InfusePlugin.get().getLOGGER().atInfo().log("becomeKing");
            this.setOtherRay(this.getRay());
            this.setRay(ray);
            this.setOtherResistance(this.getResistance());
            this.setResistance(resistance);
            return this.getResistance();
        }else {
            InfusePlugin.get().getLOGGER().atInfo().log("becomeSideKick");
            this.setOtherRay(ray);
            this.setOtherResistance(resistance);
            return 0;
        }
    }

    public boolean deleteCollisionAs(UUID ray){
        if(ray.equals(this.getRay())){
            this.setRay(this.getOtherRay());
            this.setOtherRay(null);
            this.setResistance(this.getOtherResistance());
            this.setOtherResistance(0);
            return true;
        }else if(ray.equals(this.getOtherRay())){
            this.setOtherRay(null);
            this.setOtherResistance(0);
            return true;
        }else{
            return false;
        }
    }

    public boolean propagateAs(UUID ray, int resistance){
        if(this.getRay() == null){
            this.setRay(ray);
            this.setResistance(resistance);
            return true;
        }else if(this.getOtherRay() == null){
            this.collideWith(ray, resistance);
            return true;
        }else{
            return false;
        }
    }

    public boolean canUnPropagateAs(UUID ray){
        if(this.getRay() != null && this.otherRay != null){
            InfusePlugin.get().getLOGGER().atInfo().log("Why are you here ?");
            return false;
        }else {
            InfusePlugin.get().getLOGGER().atInfo().log("Why are you sayign false because " + ray + " vs " + this.getRay());
            return ray.equals(this.getRay());
        }
    }

    public boolean isPresent(UUID uuid){
        return uuid.equals(this.getRay()) || uuid.equals(this.getOtherRay());
    }

    public boolean update(UUID uuid, int resistanceRemaining){
        if(uuid.equals(this.getRay())){
            this.setResistance(resistanceRemaining);
            return true;
        }else if(uuid.equals(this.getOtherRay())){
            this.setOtherResistance(resistanceRemaining);
            if(this.getResistance() < this.getOtherResistance()){
                InfusePlugin.get().getLOGGER().atInfo().log("Reverse : " + uuid);
                this.reverseCollision();
            }
            return true;
        }else{
            return false;
        }
    }

    public void reverseCollision(){
        UUID transitionRay = this.getRay();
        int transitionResistance = this.getResistance();

        this.setRay(this.getOtherRay());
        this.setResistance(this.getOtherResistance());

        this.setOtherRay(transitionRay);
        this.setOtherResistance(transitionResistance);
    }

    public boolean canPropagate(UUID uuid){
        return uuid.equals(this.getRay());
    }

    public boolean needToUpdateCollision(UUID uuid){
        return uuid.equals(this.getOtherRay());
    }
}

package org.infuse.plugin.Class.Emitter;

import org.infuse.plugin.Class.Ray.Ray;

import java.util.UUID;

public class EmitterMapData {

    public UUID uuid;

    public Ray NRay;

    public UUID NRaySource;

    public Ray WRay;

    public UUID WRaySource;

    public Ray SRay;

    public UUID SRaySource;

    public Ray ERay;

    public UUID ERaySource;

    public Ray URay;

    public UUID URaySource;

    public Ray DRay;

    public UUID DRaySource;

    public EmitterMapData(){

        this.uuid = UUID.randomUUID();
        NRay = null;
        NRaySource = null;
        WRay = null;
        WRaySource = null;
        SRay = null;
        SRaySource = null;
        ERay = null;
        ERaySource = null;
        URay = null;
        URaySource = null;
        DRay = null;
        DRaySource = null;
    }

    public EmitterMapData(UUID uuid){
        this.uuid = uuid;
        NRay = null;
        NRaySource = null;
        WRay = null;
        WRaySource = null;
        SRay = null;
        SRaySource = null;
        ERay = null;
        ERaySource = null;
        URay = null;
        URaySource = null;
        DRay = null;
        DRaySource = null;
    }
}

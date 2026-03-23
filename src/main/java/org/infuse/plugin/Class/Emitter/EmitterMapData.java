package org.infuse.plugin.Class.Emitter;

import org.infuse.plugin.Class.Ray.Ray;

import java.util.UUID;

public class EmitterMapData {

    public UUID uuid;

    public Ray NRay;

    public Ray WRay;

    public Ray SRay;

    public Ray ERay;

    public Ray URay;

    public Ray DRay;

    public EmitterMapData(){

        this.uuid = UUID.randomUUID();
        NRay = null;
        WRay = null;
        SRay = null;
        ERay = null;
        URay = null;
        DRay = null;
    }

    public EmitterMapData(UUID uuid){
        this.uuid = uuid;
        NRay = null;
        WRay = null;
        SRay = null;
        ERay = null;
        URay = null;
        DRay = null;
    }
}

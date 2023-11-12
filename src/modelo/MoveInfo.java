/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;


/**
 *
 * @author F776
 */

public class MoveInfo   {
    
    private int id;
    private String name;
    private String power;
    private String accuracy;
    private String pp;
    private String effect;
    private String effectProb;
    private String idMoveCat;
    private String idType;

    public MoveInfo() {
        
    }

    public MoveInfo(int id, String name, String power, String accuracy, String pp, String effect, String effectProb, String idMoveCat, String idType) {
        this.id = id;
        this.name = name;
        this.power = power;
        this.accuracy = accuracy;
        this.pp = pp;
        this.effect = effect;
        this.effectProb = effectProb;
        this.idMoveCat = idMoveCat;
        this.idType = idType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getPp() {
        return pp;
    }

    public void setPp(String pp) {
        this.pp = pp;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public String getEffectProb() {
        return effectProb;
    }

    public void setEffectProb(String effectProb) {
        this.effectProb = effectProb;
    }

    public String getIdMoveCat() {
        return idMoveCat;
    }

    public void setIdMoveCat(String idMoveCat) {
        this.idMoveCat = idMoveCat;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    
    
    
}

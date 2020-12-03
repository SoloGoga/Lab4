package com.company;

import java.io.Serializable;

public class Cone extends Circle implements IFigure, Serializable {
    protected double Height;

    public Cone(double radius, double height){
        super(radius);
        Height = height;
    }

    public double GetHeight(){
        return Height;
    }

    public double GetVolume(){
        return 0.333 * Height * super.getArea();
    }

    private double GetConeForming(){
        return Math.sqrt(Math.pow(GetRadius(), 2) + Math.pow(Height, 2));
    }

    @Override
    public String getInfo() {
        return "\nCone height: " + Height +
                "\nCone area: " + getArea() +
                "\nCone volume: " + GetVolume();
    }

    @Override
    public double getArea() {
        return GetConeForming() + super.getArea();
    }
}

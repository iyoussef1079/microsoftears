/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ulaval.glo2004.domain.teardrop;

import java.io.Serializable;

/**
 *
 * @author Duroy
 */
public abstract class CurvedProfile implements IDrawable, Cloneable, Serializable {

    public CurvedProfile() {
    }
    
    public abstract CustomPolygon getPolygon();

    // used by hatch to know how to calculate distance with floor
    public abstract int getBottomLeftCornerIndex();
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

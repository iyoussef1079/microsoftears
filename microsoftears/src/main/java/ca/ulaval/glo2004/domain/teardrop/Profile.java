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
public abstract class Profile implements IDrawable, Serializable {

    public Profile() {
    }
    
    public abstract CustomPolygon getPolygon();

    public abstract Object clone() throws CloneNotSupportedException;
}

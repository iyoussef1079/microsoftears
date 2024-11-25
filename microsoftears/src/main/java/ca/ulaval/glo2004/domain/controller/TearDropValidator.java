/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.ulaval.glo2004.domain.controller;

import ca.ulaval.glo2004.domain.teardrop.EllipseCurvedProfile;
import ca.ulaval.glo2004.domain.teardrop.Measurement;
import ca.ulaval.glo2004.domain.teardrop.PointMeasurement;
import ca.ulaval.glo2004.domain.teardrop.Quadrant;
import ca.ulaval.glo2004.domain.teardrop.TearDropTrailer;

import java.util.ArrayList;

/**
 *
 * @author Utilisateur
 */
// TODO: static functions only!
public class TearDropValidator {
    private TearDropTrailer tearDrop;
    
    public TearDropValidator(TearDropTrailer tearDrop){
        this.tearDrop = tearDrop;
    }
    
    public ArrayList<String> validTearDrop(){
        ArrayList<String> validations = new ArrayList<>();
        //validations.add(validSideWall());
        //validations.add(validFloor());
        //validations.add(validBeam());
        //validations.add(validHatch());
        
        
        return validations;
    }
    
    private String validHatch(){
        Measurement thickness = this.tearDrop.getHatch().getThickness();
        Measurement limitThickness = this.tearDrop.getRawProfile().getHeight().div(2);
        Measurement distanceBeam = this.tearDrop.getHatch().getDistanceBeam();
        Measurement distanceFloor = this.tearDrop.getHatch().getDistanceFloor();
        Measurement curveRadius = this.tearDrop.getHatch().getCurveRadius();
        if(thickness.compareTo(Measurement.zero()) != 1){
            String response = "L'épaisseur ne peut être inférieure ou égale 0.";
            return response;
        } else if(thickness.compareTo(limitThickness) != -1){
            String response = "L'épaisseur doit être inférieur à la moitié de la hauteur de la roulotte.";
            return response;
        } else if(distanceFloor.compareTo(Measurement.zero()) != 1){
            String response = "La distance entre le plancher et le coffre ne peut être inférieure ou égale 0.";
            return response;
        } else if(distanceFloor.getRoundedInches() >= 48.0f){
            String response = "La distance entre le plancher et le coffre ne peut dépasser la taille limite de la roulotte.";
            return response;
        } else if(distanceBeam.getRoundedInches() >= 48.0f){
            String response = "La distance entre la poutre arrière et le coffre ne peut dépasser la taille limite de la roulotte.";
            return response;
        } else if(this.tearDrop.getHatch().getPolygon().isWithinBonderies(this.tearDrop.getFloor().getPolygon())){
            String response = "Il y a un conflit entre le coffre et le plancher.";
            return response;
        } else if(this.tearDrop.getHatch().getPolygon().isWithinBonderies(this.tearDrop.getBeam().getPolygon())){
            String response = "Il y a un conflit entre le coffre et la poutre arrière.";
            return response;
        } else if(thickness.compareTo(curveRadius) > 0) {
            String response = "L'épaisseur du hayon doit être supérieure au rayon de courbure.";
            return response;
        }
        for(PointMeasurement hatchVertex : this.tearDrop.getHatch().getPolygon().getVertices()){
            EllipseCurvedProfile ellipseCurvedProfile = (EllipseCurvedProfile) this.tearDrop.getCurvedProfile();
            for(PointMeasurement profileVertex : ellipseCurvedProfile.getEllispeMap().get(Quadrant.BOTTOM_LEFT).getPolygon().getVertices()){
                if(hatchVertex.x.compareTo(profileVertex.x) == -1 && hatchVertex.y.compareTo(profileVertex.y) == 1){
                    String response = "Une partie du coffre sort de la roulotte!";
                    return response;
                }
            }
        }
        return null;
    }
    
    private String validSideWall(){
        double height = this.tearDrop.getRawProfile().getHeight().getRoundedInches();
        double width = this.tearDrop.getRawProfile().getWidth().getRoundedInches();
        if(height > 48.0f || width > 96.0f){
            String response = "La hauteur maximale possible est de 48 pouces et la longeur 96 pouces";
            return response;
        }
        return null;
    }
    
    private String validFloor(){
        double thickness = this.tearDrop.getFloor().getThickness().getRoundedInches();
        double sideWallHeight = this.tearDrop.getRawProfile().getHeight().getRoundedInches();
        if(this.tearDrop.getFloor().getPolygon().isWithinBonderies(this.tearDrop.getHatch().getPolygon())){
            String response = "Il y a un conflit entre le coffre et le plancher.";
            return response;
        } else if(this.tearDrop.getFloor().getPolygon().isOutsideBonderies(this.tearDrop.getCurvedProfile().getPolygon())){
            String response = "Une partie du plancher sort de la roulotte!";
            return response;
        } else if(thickness >= (sideWallHeight/2)){
            String response = "L'épaisseur du plancher est trop grande";
            return response;
        }
        return null;
    }
    
    private String validBeam()
    {
        Measurement beam_height = tearDrop.getBeam().getHeight();
        Measurement beam_width = tearDrop.getBeam().getWidth();
        Measurement sidewall_height = tearDrop.getRawProfile().getHeight();
        Measurement sidewall_width = tearDrop.getRawProfile().getWidth();
        Measurement topleftposition = tearDrop.getBeam().getTopLeftPosition().getX();
        Measurement beam_height_max = new Measurement(300);
        Measurement beam_width_max = new Measurement(300);
        String reponse = null;
        if ((beam_height.compareTo(beam_height_max) == 1 ) || (beam_width.compareTo(beam_width_max) == 1))
        {
            reponse = "La poutre arriere ne doit pas depasser les dimensions 300*300mm";
        }
        else if(topleftposition.compareTo(sidewall_width.div(-2))==-1)
        {
            reponse = "La poutre arriere ne doit pas sortir du Mur lateral";
        }
        else if((topleftposition.add(beam_width)).compareTo(sidewall_width.div(2))==1)
        {
            reponse = "La poutre arriere ne doit pas sortir du Mur lateral";
        }
        return reponse;
    }
    
    private String validateFloor()
    {
        String reponse = null;
        Measurement back_margin = tearDrop.getFloor().getBackmargin();
        Measurement front_margin = tearDrop.getFloor().getFrontmargin();
        Measurement thickness = tearDrop.getFloor().getThickness();
        Measurement sidewall_height = tearDrop.getRawProfile().getHeight();
        Measurement sidewall_width = tearDrop.getRawProfile().getWidth();
        
        if(back_margin.compareTo(sidewall_width) >0)
        {
            reponse = "La marge arriere ne doit pas depasser la largeur du mur latéral";
        }
        else if(front_margin.compareTo(sidewall_width)> 0)
        {
            reponse = "La marge avant ne doit pas depasser la largeur du mur latéral";
        }
        else if(thickness.compareTo(sidewall_height)>0)
        {
            reponse = "L'épaisseur ne peut pas etre superieur à la hauteur du mur Laetral";
        }
        else if ((front_margin.add(back_margin)).compareTo(sidewall_width)>0)
        {
            reponse = "Le plancher sera inexistant";
        }
        
        return reponse;
        
    }
}

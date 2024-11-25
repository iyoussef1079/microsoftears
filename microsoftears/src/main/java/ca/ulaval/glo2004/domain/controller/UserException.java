/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca.ulaval.glo2004.domain.controller;

/**
 *
 * @author justi
 */
public class UserException extends RuntimeException {
    public UserException(String errorMessage) {
        super(errorMessage);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2021_p2si;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Raúl
 */
public class ClasificadorDebil {
    private int pixel;//Pixel de la imagen
    private int umbral;//Umbral del pixels
    private int direccion;//Dirección del clasificador
    private int fallos;
    private int[] prediccion;//Vector de -1 y 1 
    private double error;//Cuanto mayor error, peor clasificador debil
    private double confianza;
    
    public ClasificadorDebil() {
        this.pixel= this.umbral= this.direccion= this.fallos= 0;
        this.error= this.confianza= 0.0;
    }
    
    public ClasificadorDebil(int pixel, int umbral, int direccion, int size) {
        this.pixel= pixel;
        this.umbral= umbral;
        this.direccion= direccion;
        this.prediccion= new int[size];
        this.fallos= 0;
        this.error= 0;
        this.confianza= 0;
    }
    
    public static ClasificadorDebil generarClasifAzar(int dimension, int size) {
        Random rng= new Random();
        int pxl= rng.nextInt(dimension);
        int umb= rng.nextInt(255);
        int direc= rng.nextInt(2);
        if(direc == 0)
            direc= -1;
        return new ClasificadorDebil(pxl, umb, direc, size);
    }
    
    public void aplicarClasifDebil(ArrayList<Imagen> X) {
        
        for(int i=0; i < X.size(); i++) {
            if(this.direccion == 1) {//Pertenece a la clase                
                if(Adaboost.Byte2Unsigned(X.get(i).getImageData()[this.pixel]) > this.umbral)
                    this.prediccion[i]= 1;
                else
                    this.prediccion[i]= -1;
            }
            else {//No pertenece a la clase
                if(Adaboost.Byte2Unsigned(X.get(i).getImageData()[this.pixel]) > this.umbral)
                    this.prediccion[i]= -1;
                else
                    this.prediccion[i]= 1;
            }
        }
    }
    public int checkImagen(Imagen X) {
        if(this.direccion == 1) {//Pertenece a la clase
                if(Adaboost.Byte2Unsigned(X.getImageData()[this.pixel]) > this.umbral)
                    return 1;
                else
                    return -1;
            }
            else {//No pertenece a la clase
                if(Adaboost.Byte2Unsigned(X.getImageData()[this.pixel]) > this.umbral)
                    return -1;
                else
                    return 1;
            }
    }
    public void obtenerErrorClasif (ArrayList<Imagen> X, int[] Y, double[] D) {
            
        double aux= 0;
        int fallos=0;
        for(int i=0; i < X.size(); i++)
        {
            if(this.prediccion[i] != Y[i]) {//El Clasificador ha fallado
                aux += D[i];//Le sumamos el peso de esta imagen
                fallos++;
            }
        }
        this.error= aux;//Guardamos el error de este clasificador
        this.fallos= fallos;
    }
    
    @Override
    public String toString() {
        String s= this.pixel + "," + this.umbral + ",";
        if(this.direccion > 0)
            s+= "+,";
        else
            s+= "-,";
        s+= this.confianza;
        return s;
    }
    
    public int[] getPrediccion() {
        return prediccion;
    }
    public double getError() {
        return error;
    }
    public int getFallos() {
        return fallos;
    }
    public void setConfianza(double confianza){
        this.confianza=confianza;
    }
    public double getConfianza() {
        return confianza;
    }
    public void setPixel(int pixel){
        this.pixel=pixel;
    }
    public void setUmbral(int umbral){
        this.umbral=umbral;
    }
    public void setDireccion(int direccion){
        this.direccion=direccion;
    }
}

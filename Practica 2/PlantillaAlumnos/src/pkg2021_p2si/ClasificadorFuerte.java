/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2021_p2si;

import java.util.ArrayList;

/**
 *
 * @author Raúl
 */
public class ClasificadorFuerte {
    private ArrayList<ClasificadorDebil> debiles;
    private int[] prediccion;
    private int fallos;
    private double confianza;
    
    public ClasificadorFuerte(ArrayList<ClasificadorDebil> debiles) {
        this.debiles= debiles;
        this.fallos= 0;
        this.confianza= 0;
    }
    public ClasificadorFuerte(ArrayList<ClasificadorDebil> debiles, double confianza) {
        this.debiles= debiles;
        this.fallos= 0;
        this.confianza= confianza;
    }
    
    public void aplicarClasifFuerte(ArrayList<Imagen> X) {
        this.resize(X.size());
        for(int i=0; i < X.size(); i++) {
            double H= 0.0;
            for(int j=0; j < this.debiles.size(); j++)  //Es -1 o +1
                H += this.debiles.get(j).getConfianza()*this.debiles.get(j).checkImagen(X.get(i));
            if(H > 0.0)
                this.prediccion[i]= 1;//Es un avión
            else
                this.prediccion[i]= -1;//No es avión
        }  
    }
    public double checkImagen(Imagen X) {
        double H= 0.0;
        for(int j=0; j < this.debiles.size(); j++)//Es -1 o +1
            H += this.debiles.get(j).getConfianza()*this.debiles.get(j).checkImagen(X);
        return H;
    }
    
    public void obtenerErrorClasif(ArrayList<Imagen> X, int[] Y) {
        int fails=0;
        for(int i=0; i < X.size(); i++) {
            if(this.prediccion[i] != Y[i]) //El Clasificador ha fallado
                fails++;
        }
        this.fallos= fails;
    }
    private void resize(int size) {
        this.prediccion= new int[size];
    }
    
    public ArrayList<ClasificadorDebil> getDebiles(){
        return this.debiles;
    }
    
    public int getFallos() {
        return fallos;
    }
    public void setConfianza(double confianza) {
        this.confianza= confianza;
    }
    public double getConfianza() {
        return confianza;
    }
}

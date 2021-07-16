/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AEstrella;
/**
 *
 * @author Raul Beltran Marco
 * DNI: 23900664F
 */

public class Nodo {
    Coordenada coordenada;
    Nodo padre;
    Mundo mundo;
    int g;
    int f;
    int h;
    public class Cubo {
        int x;
        int y;
        int z;
        
        public Cubo()
        {
            this.x= this.y= this.z= 0;
        }
        public Cubo(int x, int y, int z)
        {
            this.x= x;
            this.y= y;
            this.z= z;
        }
        public int getX()
        {
            return x;
        }
        public int getY()
        {
            return y;
        }
        public int getZ()
        {
            return z;
        }
        public int distancia(Cubo b)
        {
            return (Math.abs(this.x - b.x) + Math.abs(this.y - b.y) + Math.abs(this.z - b.z)) / 2;
        }
    }

    public Nodo(Mundo mundo, Coordenada coordenada, Nodo padre,int g, int h)
    {
        this.mundo= mundo;
        this.coordenada = coordenada;
        if(padre == null)
            this.g = g;
        else
            this.g = g + padre.getG();
        //this.h = h + Manhattan(coordenada, mundo.getDragon());//Heuristica Manhattan
        //this.h = h + Euclidea(coordenada, mundo.getDragon());//Heuristica Euclidea
        this.h = h + mapaHexagonal(coordenada, mundo.getDragon());//Heuristica MapaHexagonal
        this.f = this.g + this.h;//Funcion de evaluacion
        this.padre= padre;
    }
    private int Manhattan(Coordenada actual, Coordenada meta)
    {
        int x= Math.abs(meta.getX() - actual.getX());
        int y= Math.abs(meta.getY() - actual.getY());
        return x + y;
    }
    private int Euclidea(Coordenada actual, Coordenada meta)
    {
        int x= (int)Math.pow(meta.getX() - actual.getX(), 2);
        int y= (int)Math.pow(meta.getY() - actual.getY(), 2);
        return (int)Math.sqrt(x + y);
    }
    private int mapaHexagonal(Coordenada actual, Coordenada meta)
    {
        Cubo origen= axial_to_cube(actual);
        Cubo dragon= axial_to_cube(meta);
        return origen.distancia(dragon);
    }
    public Cubo axial_to_cube(Coordenada coordenada)
    {
        int x,y,z;
        x= coordenada.getY() - (coordenada.getX() + (coordenada.getX()&1)) / 2;
        z= coordenada.getX();
        y= -x - z;
        return new Cubo(x, y, z);
    }
    //Setters
    public void setG(int g) {
        this.g = g;
    }
    //Getters
    public Coordenada getCoordenada() {
        return coordenada;
    }

    public int getG() {
        return g;
    }

    public int getF() {
        return f;
    }
    
    public int getH() {
        return h;
    }
    public Nodo getPadre(){
        return padre;
    }

    @Override
    public String toString() {
        return "Nodo(" + coordenada.getX() + ", " + coordenada.getY() + ") \n" +
                "G= " + g + "\n" +
                "H= " + h + "\n" +
                "F= " + f;
    }
}

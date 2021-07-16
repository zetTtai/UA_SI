/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AEstrella;

import java.util.ArrayList;
/**
 *
 * @author mirse
 */
public class AEstrella {
 
    //Mundo sobre el que se debe calcular A*
    Mundo mundo;
    
    //Camino
    public char camino[][];
    
    //Casillas expandidas
    int camino_expandido[][];
    
    //Número de nodos expandidos
    int expandidos;
    
    //Coste del camino
    float coste_total;
    
    public AEstrella(){
        expandidos = 0;
        mundo = new Mundo();
    }
    
    public AEstrella(Mundo m){
        //Copia el mundo que le llega por parámetro
        mundo = new Mundo(m);
        camino = new char[m.tamanyo_y][m.tamanyo_x];
        camino_expandido = new int[m.tamanyo_y][m.tamanyo_x];
        expandidos = 0;
        
        //Inicializa las variables camino y camino_expandidos donde el A* debe incluir el resultado
            for(int i=0;i<m.tamanyo_x;i++)
                for(int j=0;j<m.tamanyo_y;j++){
                    camino[j][i] = '.';
                    camino_expandido[j][i] = -1;
                }
    }
    
    private boolean nodoRepetido(Coordenada coordenada, ArrayList listaFrontera)//Si devuelve true es que el nodo ya esta incluido.
    {
        if(!listaFrontera.isEmpty())
        {
            for(int i=0; i < listaFrontera.size(); i++)
            {
                Nodo nodo= (Nodo)listaFrontera.get(i);
                if(nodo.getCoordenada().getX() == coordenada.getX() && nodo.getCoordenada().getY() == coordenada.getY())
                    return true;
            }
        }
        return false;
    }
    
    public ArrayList getNeighborhood(Nodo padre, int h)
    {
        ArrayList vecinos= new ArrayList();
        if(padre.getCoordenada().getY()%2 != 0)
        {
            for(int i=-1; i < 2; i++)//Filas(Y) IMPAR
            {
                for(int j=-1; j < 1; j++)//Columnas(X)
                {
                    if(i == 0 && j == 0)
                    {
                        Coordenada aux= new Coordenada(padre.getCoordenada().getX() + 1, padre.getCoordenada().getY() + i);
                        if(mundo.getCelda(aux.getX(), aux.getY()) != 'b' && mundo.getCelda(aux.getX(), aux.getY()) != 'p' && !nodoRepetido(aux, vecinos))
                        {
                            switch(mundo.getCelda(aux.getX(), aux.getY()))
                            {
                                case 'c':
                                    vecinos.add(new Nodo(this.mundo, aux, padre, 1, h));
                                    break;
                                case 'h':
                                    vecinos.add(new Nodo(this.mundo, aux, padre, 2, h));
                                    break;
                                case 'a':
                                    vecinos.add(new Nodo(this.mundo, aux, padre, 3, h));
                                    break;
                                default:
                                    vecinos.add(new Nodo(this.mundo, aux, padre, 3, h));
                                    break;
                            }
                        }
                    }
                    else
                    {                       
                        Coordenada aux= new Coordenada(padre.getCoordenada().getX() + j, padre.getCoordenada().getY() + i);
                        if(mundo.getCelda(aux.getX(), aux.getY()) != 'b' && mundo.getCelda(aux.getX(), aux.getY()) != 'p' && !nodoRepetido(aux, vecinos))
                        {
                            switch(mundo.getCelda(aux.getX(), aux.getY()))
                            {
                                case 'c':
                                    vecinos.add(new Nodo(this.mundo, aux, padre, 1, h));
                                    break;
                                case 'h':
                                    vecinos.add(new Nodo(this.mundo, aux, padre, 2, h));
                                    break;
                                case 'a':
                                    vecinos.add(new Nodo(this.mundo, aux, padre, 3, h));
                                    break;
                                default:
                                    vecinos.add(new Nodo(this.mundo, aux, padre, 0, h));
                                    break;                                    
                            }
                        }
                    }
                }
            }
        }
        else
        {
            for(int i=-1; i < 2; i++)//Filas(Y) PAR
            {
                for(int j=0; j < 2; j++)//Columnas(X)
                {
                    if(i == 0 && j == 0)
                    {
                        Coordenada aux= new Coordenada(padre.getCoordenada().getX() - 1 , padre.getCoordenada().getY() + i);
                        if(mundo.getCelda(aux.getX(), aux.getY()) != 'b' && mundo.getCelda(aux.getX(), aux.getY()) != 'p' && !nodoRepetido(aux, vecinos))
                        {
                            switch(mundo.getCelda(aux.getX(), aux.getY()))
                            {
                                case 'c':
                                    vecinos.add(new Nodo(this.mundo, aux, padre, 1, h));
                                    break;
                                case 'h':
                                    vecinos.add(new Nodo(this.mundo, aux, padre, 2, h));
                                    break;
                                case 'a':
                                    vecinos.add(new Nodo(this.mundo, aux, padre, 3, h));
                                    break;
                                default:
                                    vecinos.add(new Nodo(this.mundo, aux, padre, 0, h));
                                    break;                                    
                            }
                            
                        }
                    }
                    else
                    {
                        Coordenada aux= new Coordenada(padre.getCoordenada().getX() + j, padre.getCoordenada().getY() + i);
                        if(mundo.getCelda(aux.getX(), aux.getY()) != 'b' && mundo.getCelda(aux.getX(), aux.getY()) != 'p' && !nodoRepetido(aux, vecinos))
                        {
                            switch(mundo.getCelda(aux.getX(), aux.getY()))
                            {
                                case 'c':
                                    vecinos.add(new Nodo(this.mundo, aux, padre, 1, h));
                                    break;
                                case 'h':
                                    vecinos.add(new Nodo(this.mundo, aux, padre, 2, h));
                                    break;
                                case 'a':
                                    vecinos.add(new Nodo(this.mundo, aux, padre, 3, h));
                                    break;
                                default:
                                    vecinos.add(new Nodo(this.mundo, aux, padre, 0, h));
                                    break;                                    
                            }
                            
                        }
                    }
                }
            }
        }
        return vecinos;
    }
    public void filtraPiedras(ArrayList listaFrontera)
    {
        if(!listaFrontera.isEmpty())
        {
            for(int i=0; i < listaFrontera.size(); i++)
            {
                Nodo aux= (Nodo)listaFrontera.get(i);
                if(mundo.getCelda(aux.getCoordenada().getX(), aux.getCoordenada().getY()) == 'p')
                {
                    listaFrontera.remove(i);
                    i=0;
                }
            }
        }
    }
    private boolean contiene(ArrayList lista, Nodo nodo)//Devuelve true si si que contiene el nodo
    {
        if(!lista.isEmpty())
        {
            for(int i=0; i < lista.size(); i++)
            {
                Nodo aux= (Nodo)lista.get(i);
                if(nodo.getCoordenada().getX() == aux.getCoordenada().getX() && nodo.getCoordenada().getY() == aux.getCoordenada().getY())
                    return true;
            }
        }
        return false;
    }
    //Calcula el A*
    public int CalcularAEstrella(){
        boolean encontrado = false;
        int result = -1;
        
        //AQ ES DONDE SE DEBE IMPLEMENTAR A*
        int h= 0;
        expandidos= 0;
        ArrayList listaInterior= new ArrayList();
        ArrayList listaFrontera= new ArrayList();
        
        Nodo nodoActual= new Nodo(this.mundo, mundo.getCaballero(),null, 0, h);//Casilla de salida
        listaInterior.add(nodoActual);
        listaFrontera= getNeighborhood(nodoActual, h);
        while(!listaFrontera.isEmpty() && !encontrado)
        {
            if(mundo.getCaballero().getY() == mundo.getDragon().getY() && mundo.getCaballero().getX() == mundo.getDragon().getX()){
                return result;
            }
            //Obtenemos el nodo con menor F que no este en listaInterior
            Nodo menor= (Nodo)listaFrontera.get(0);
            for(int i=0; i < listaFrontera.size(); i++)
            {
                Nodo aux= (Nodo)listaFrontera.get(i);
                if(menor.getF() >= aux.getF() && !nodoRepetido(aux.getCoordenada(),listaInterior))
                    menor= aux;
            }
            camino_expandido[menor.getCoordenada().getY()][menor.getCoordenada().getX()]= expandidos;
            expandidos++;
            nodoActual= menor;
            //Nodo Actual ahora es el que tiene menor f(El primero en tener menor f siempre es el caballero)
            boolean dragon= false;
            for(int i=0; i < listaFrontera.size(); i++)
            {
                Nodo aux= (Nodo)listaFrontera.get(i);
                if(aux.getCoordenada().getX() == mundo.getDragon().getX() && aux.getCoordenada().getY() == mundo.getDragon().getY())
                {
                    dragon= true;
                    break;
                }
            }
            if(dragon)
            {
                encontrado= true;
                coste_total= nodoActual.getG();
                result= (int)coste_total;
                while(nodoActual != null)
                {
                    camino[nodoActual.getCoordenada().getY()][nodoActual.getCoordenada().getX()]= 'X';
                    nodoActual= nodoActual.getPadre();
                }
            }
            else
            {
                //Sacamos el nodo actual de la listaFrontera
                listaFrontera.remove(nodoActual);
                listaInterior.add(nodoActual);
                ArrayList hijosNodoActual= getNeighborhood(nodoActual, h);//generamos los hijos del nodo actual
                for(int i=0; i < hijosNodoActual.size(); i++)
                {
                    Nodo hijo= (Nodo)hijosNodoActual.get(i);
                    if(!contiene(listaInterior, hijo))//False si no lo contiene
                    {
                        int G= hijo.getPadre().getG() + hijo.getG();

                        if(!contiene(listaFrontera, hijo))
                        {
                            hijo.padre= nodoActual;
                            listaFrontera.add(hijo);
                        }
                        else if(G <= hijo.getG())
                        {
                            hijo.padre= nodoActual;
                            hijo.setG(hijo.g + hijo.getPadre().getG());
                            hijo.f= hijo.getG() + hijo.getH();
                        }
                    }
                }
            }
            filtraPiedras(listaFrontera);
        }
        //Si ha encontrado la solución, es decir, el camino, muestra las matrices camino y camino_expandidos y el número de nodos expandidos
        if(encontrado){
            //Mostrar las soluciones
            System.out.println("Camino");
            mostrarCamino();

            System.out.println("Camino explorado");
            mostrarCaminoExpandido();
            
            System.out.println("Nodos expandidos: "+expandidos);
        }
        return result;
    }
    
    
    //Muestra la matriz que contendrá el camino después de calcular A*
    public void mostrarCamino(){
        for (int i=0; i<mundo.tamanyo_y; i++){
            if(i%2==0)
                System.out.print(" ");
            for(int j=0;j<mundo.tamanyo_x; j++){
                System.out.print(camino[i][j]+" ");
            }
            System.out.println();   
        }
    }
    
    //Muestra la matriz que contendrá el orden de los nodos expandidos después de calcular A*
    public void mostrarCaminoExpandido(){
        for (int i=0; i<mundo.tamanyo_y; i++){
            if(i%2==0)
                    System.out.print(" ");
            for(int j=0;j<mundo.tamanyo_x; j++){
                if(camino_expandido[i][j]>-1 && camino_expandido[i][j]<10)
                    System.out.print(" ");
                System.out.print(camino_expandido[i][j]+" ");
            }
            System.out.println();   
        }
    }
    
    public void reiniciarAEstrella(Mundo m){
        //Copia el mundo que le llega por parámetro
        mundo = new Mundo(m);
        camino = new char[m.tamanyo_y][m.tamanyo_x];
        camino_expandido = new int[m.tamanyo_y][m.tamanyo_x];
        expandidos = 0;
        
        //Inicializa las variables camino y camino_expandidos donde el A* debe incluir el resultado
            for(int i=0;i<m.tamanyo_x;i++)
                for(int j=0;j<m.tamanyo_y;j++){
                    camino[j][i] = '.';
                    camino_expandido[j][i] = -1;
                }
    }
    
    public float getCosteTotal(){
        return coste_total;
    }
}



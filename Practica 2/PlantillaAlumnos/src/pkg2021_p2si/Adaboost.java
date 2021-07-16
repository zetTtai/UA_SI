/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2021_p2si;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;



//    T= 500; A= 1000; P=80
/**
 *
 * @author fidel
 */
public class Adaboost {
    
    private static final int T= 200;//Número de clasificadores dbiles a usar 50
    private static final long A= 1000;//Número de pruebas aleatorias 5000
    private static final int M= (32*32*3) - 1;//Dimensión de la imagen
    private static final int P= 80;//Porcentaje de imagenes de avion
    private static final int numClases= 10;
    
    public static int Byte2Unsigned(byte b) {
        return b & 0xFF;
    }
    
    public static ClasificadorDebil Entrenamiento(ArrayList<Imagen> X, int[] Y, double[] D) {
        
        ArrayList<ClasificadorDebil> vector= new ArrayList<>();
        ClasificadorDebil ganador= null;
        int j=0;
        for(int i=0; i < A; i++) {
            ClasificadorDebil actual= ClasificadorDebil.generarClasifAzar(M, X.size());
            actual.aplicarClasifDebil(X);//Obtenemos su predicción
            actual.obtenerErrorClasif(X, Y, D);//Obtenemos el error
            vector.add(actual);//Guardamos el actual en el vector
        }
        double min= 0.0;
        for(int i=0; i < vector.size(); i++) {
            if(i == 0) {
                min= vector.get(i).getError();
                ganador= vector.get(i);
            }
            else if(vector.get(i).getError() < min) {
                    min= vector.get(i).getError();
                    ganador= vector.get(i);
            }
        }
        return ganador;
    }
    
    public static ClasificadorFuerte Adaboost(ArrayList<Imagen> X, int[] Y, ArrayList<Imagen> Xtest, int[] Ytest)
    {
        ArrayList<ClasificadorDebil> ganadores= new ArrayList<>();
        ClasificadorFuerte best= new ClasificadorFuerte(ganadores);
        boolean first= true;
        double[] D= new double[X.size()];
        for(int i=0; i < X.size(); i++)
            D[i]= (double)1/X.size();
        for(int i=0; i < T; i++) {
            //Comienza entrenamiento
            ClasificadorDebil ganador= Entrenamiento(X, Y, D);
            //Comprobamos su porcentaje
//            double porcentaje= (((double)X.size() - (double)ganador.getFallos())/(double)X.size()) *100;
//            DecimalFormat df= new DecimalFormat("#.00");
//            System.out.println(df.format(porcentaje));
            //Obtenemos la confianza del ganador
            double confianza= 0.5*Math.log((1 - ganador.getError())/ganador.getError())/Math.log(2);
            ganador.setConfianza(confianza);
            //Actualizar los pesos
            double Z= 0;       
            double[] numeradores= new double[X.size()];
            for(int j=0; j < X.size(); j++) {
                numeradores[j]= D[j]*Math.pow(Math.E, (-1*ganador.getConfianza())*Y[j]*ganador.getPrediccion()[j]);
                Z+= numeradores[j];//Sumatorio de los numeradores/Constante de normalización
            }
            for(int j=0; j < X.size(); j++)
                D[j]= numeradores[j]/Z;
            ganadores.add(ganador);
//            ClasificadorFuerte cf= new ClasificadorFuerte(ganadores);
//            cf.aplicarClasifFuerte(Xtest);
//            cf.obtenerErrorClasif(Xtest, Ytest);
//            double porcentajeTest2= (((double)Xtest.size() - (double)cf.getFallos())/(double)Xtest.size()) *100;
//            DecimalFormat df= new DecimalFormat("#.00");
//            cf.aplicarClasifFuerte(X);
//            cf.obtenerErrorClasif(X, Y);
//            double porcentaje= (((double)X.size() - (double)cf.getFallos())/(double)X.size()) *100;
//            System.out.println(df.format(porcentajeTest2) + "\t" + df.format(porcentaje));
            if(i >= 38) {
                ClasificadorFuerte aux= new ClasificadorFuerte(ganadores);
                aux.aplicarClasifFuerte(Xtest);
                aux.obtenerErrorClasif(Xtest, Ytest);
                double porcentajeTest= (((double)Xtest.size() - (double)aux.getFallos())/(double)Xtest.size()) *100;
                aux.setConfianza(porcentajeTest);
                if(first == true) {
                    best= aux;
                    first= false;
                }
                else if(aux.getConfianza() < best.getConfianza())
                        return best;//Devolvemos el actual antes de que empeore por el sobre entrenamiento.
            }
        }
        return new ClasificadorFuerte(ganadores);
    }
        
    public static void generaEntrenamiento(ArrayList<Imagen> X, int[] Y, int num, int conjunto) {
        CIFAR10Loader ml = new CIFAR10Loader();
        ml.loadDBFromPath("./cifar10_2000");
        int indice= 0;
        File carpeta = new File("./cifar10_2000/" + conjunto);
        File[] lista= carpeta.listFiles();//Obtenemos el número de elementos del conjunto
        for(int i=0; i < numClases; i++) {
            ArrayList d1imgs = ml.getImageDatabaseForDigit(i);
            if(i == conjunto) {
                for(int j= 0; j < num; j++) {
                    Imagen img = (Imagen) d1imgs.get(j);
                    X.add(img);
                    Y[indice]= 1;
                    indice++;                    
                }
            }
            else {
                for(int j= 0; j < (lista.length - num)/(numClases - 1); j++) {
                    Imagen img = (Imagen) d1imgs.get(j);
                    X.add(img);
                    Y[indice]= -1;
                    indice++;  
                }
            }
        }
    }
    public static void generaTest(ArrayList<Imagen> X, int[] Y, int num, int conjunto) {
        CIFAR10Loader ml = new CIFAR10Loader();
        ml.loadDBFromPath("./cifar10_2000");
        int indice= 0;
        File carpeta = new File("./cifar10_2000/" + conjunto);
        File[] lista= carpeta.listFiles();//Obtenemos el número de elementos del conjunto
        for(int i=0; i < numClases; i++) {
            ArrayList d1imgs = ml.getImageDatabaseForDigit(i);
            if(i == conjunto) {
                for(int j= num; j < lista.length; j++) {
                    Imagen img = (Imagen) d1imgs.get(j);
                    X.add(img);
                    Y[indice]= 1;
                    indice++;                    
                }
            }
            else {
                for(int j= 0; j < (lista.length - num)/(numClases - 1); j++) {
                    Imagen img = (Imagen) d1imgs.get(j);
                    X.add(img);
                    Y[indice]= -1;
                    indice++;  
                }
            }  
        }
    }
    
    public static void generaFichero(ArrayList<ClasificadorFuerte> clasificadores, String nombre) throws IOException {
        FileWriter fichero= new FileWriter(nombre);
        PrintWriter pw= new PrintWriter(fichero);
        String aux= "";
        
        for(int i=0; i < clasificadores.size(); i++) {//tantas líneas como clasificadores
            for(int j=0; j < clasificadores.get(i).getDebiles().size(); j++)//Guardamos sus clasificadores débiles
                aux += clasificadores.get(i).getDebiles().get(j).toString() + "|";
            aux+= ":" + clasificadores.get(i).getConfianza();
            pw.println(aux);
            aux= "";//limpiamos el buffer
        }
        if(fichero != null)
            fichero.close();//Al cerrar se escribe todo
    }
    
    public static void leerFichero(ArrayList<ClasificadorFuerte> clasificadores, String nombre) throws FileNotFoundException, IOException{
        File fichero= new File("./" + nombre);
        FileReader fr= new FileReader(fichero);
        BufferedReader br= new BufferedReader(fr);
        String linea;
        while((linea=br.readLine()) != null){
            ArrayList<ClasificadorDebil> debiles= new ArrayList<>();
            int i=0;
            String confianza= "";
            for(;i < linea.length();) {//Aumentaremos i a medida que leamos caracteres
                ClasificadorDebil debil= new ClasificadorDebil();
                for(int j=0; j < 4; j++) {
                    String aux= "";
                    while(linea.charAt(i) != ',' && linea.charAt(i) != '|') {
                        aux += linea.charAt(i);
                        i++;
                    }
                    i++;
                    if(linea.charAt(i) == ':') {
                        i++;
                        while(i < linea.length()) {
                            confianza+= linea.charAt(i);
                            i++;
                        }
                    }
                    switch(j) {
                        case 0://pixel
                            debil.setPixel(Integer.parseInt(aux));
                            break;
                        case 1://Umbral
                            debil.setUmbral(Integer.parseInt(aux));
                            break;
                        case 2://Dirección (+ ó -) no hace falta convertir a int
                            if(!"+".equals(aux))
                                debil.setDireccion(-1);
                            else
                                debil.setDireccion(1);
                            break;
                        case 3://Confianza
                            debil.setConfianza(Double.parseDouble(aux));
                            break;
                        default:
                            break;
                    }
                }
                debiles.add(debil);
            }
            clasificadores.add(new ClasificadorFuerte(debiles, Double.parseDouble(confianza)));
        }
        if(fr != null)
            fr.close();
    }
    
    public static int clasificarImagen(ArrayList<ClasificadorFuerte> clasificadores, String path){
        File ruta= new File(path);
        Imagen img= new Imagen(ruta);
        int aciertos = 0;
        double[] prediccion= new double[numClases];
        for(int i=0; i < clasificadores.size(); i++) {
            prediccion[i]= clasificadores.get(i).checkImagen(img);
            if(prediccion[i] > 0.0)
                aciertos++;
        }
        switch (aciertos) {
            case 1:
                //Caso ideal
                for(int i=0; i<prediccion.length; i++)
                    if(prediccion[i] > 0.0)
                        return i;
                break;
            case 0:
                //Peor caso
                double min= clasificadores.get(0).getConfianza();
                int result= 0;
                for(int i=1; i<clasificadores.size(); i++) {
                    if(clasificadores.get(i).getConfianza() < min) {//peor porcentaje de acierto en la prueba test, más probable que haya fallado
                        min= clasificadores.get(i).getConfianza();
                        result= i;
                    }
                }
                return result;
            default:
                //Indecisión
                int primero= 0;
                double maximo= 0.0;
                int resultado= 0;
                for(int i=0; i<prediccion.length; i++) {
                    if(primero == 0) {//Guardamos el primer max
                        if(prediccion[i] > 0.0){
                            maximo= prediccion[i];
                            resultado= i;
                            primero++;
                        }
                    }
                    else {
                        if(prediccion[i] > 0.0 && prediccion[i] > maximo){
                                maximo= prediccion[i];
                                resultado= i;
                            }
                        }
                    }
                return resultado;
        }
        return -1;
    }
    public static void main(String[] args) throws InterruptedException {
        
        //Son necesarios dos argumentos 
        if (args.length == 2) {
            //Se ejecuta la práctica como entrenamiento
            if (args[0].equals("-t")) {
                //La posición 0 corresponde al clasifFuerte de los aviones, la posición 1 corresponde a coches,...
                ArrayList<ClasificadorFuerte> clasificadores= new ArrayList<>();
                System.out.println("DATOS: P= " + P + "% | A= " + A + " | T= " + T);
                double porcentajeAcumulado= 0.0;
                double porcentajeAcumuladoTest= 0.0;
                for(int i=0; i < numClases; i++) {
                    //int cero= 0;//probamos 10 veces los resultados de aviones
                    //File carpeta = new File("./cifar10_2000/" + cero);
                    File carpeta = new File("./cifar10_2000/" + i);
                    File[] lista= carpeta.listFiles();//Obtenemos el número de elementos de la clase
                    int aux= lista.length * P/100;
                    //Entrenamiento
                    ArrayList<Imagen> X= new ArrayList<>();
                    int[] Y =  new int[lista.length*2];
                    //Test
                    ArrayList<Imagen> Xtest= new ArrayList<>(); 
                    int[] Ytest =  new int[lista.length*2];
                    
                    //generaConjuntos(X, Y, Xtest, Ytest,aux, resto, i);
                    generaEntrenamiento(X, Y, aux, i);
                    generaTest(Xtest, Ytest, aux, i);
                    //generaConjuntos(X, Y, Xtest, Ytest,aux, resto, cero);//Crea,ps un conjunto X e Y para i conjuntos de imágenes
                    //long start= System.currentTimeMillis();                
                    ClasificadorFuerte clasificador= Adaboost(X, Y, Xtest, Ytest);
                    //long end= System.currentTimeMillis();              
//                    double time = (double) ((end - start)/1000.0);
//                    DecimalFormat df= new DecimalFormat("#.0000");
//                    System.out.println(df.format(time));
                    //Aplicamos el clasificador para test
                    //Hay que modificar el tamaño del int[] para evitar ArrayIndexOutOfBoundException función resize() es llamada en aplicarClasifFuerte
                    clasificador.aplicarClasifFuerte(X);
                    clasificador.obtenerErrorClasif(X, Y);          
                    int aciertos= X.size()-clasificador.getFallos();
                    double porcentaje= (((double)X.size() - (double)clasificador.getFallos())/(double)X.size()) *100;
                    DecimalFormat df= new DecimalFormat("#.00");
                    System.out.println("Estadísticas - Conjunto de Entrenamiento");
                    System.out.println("-----------------------------------------------------------");
                    System.out.println("El número de aciertos sobre " + X.size() + " imágenes es: " + aciertos);
                    System.out.println("% de acierto del clasificador fuerte: " + df.format(porcentaje) + '%');
                    System.out.println("");
                    porcentajeAcumulado += porcentaje;
                    //System.out.println(df.format(porcentaje));
                    clasificador.aplicarClasifFuerte(Xtest);
                    clasificador.obtenerErrorClasif(Xtest, Ytest);          
                    int aciertosTest= Xtest.size()-clasificador.getFallos();
                    double porcentajeTest= (((double)Xtest.size() - (double)clasificador.getFallos())/(double)Xtest.size()) *100;
                    //System.out.println(df.format(porcentajeTest));
                    System.out.println("Estadísticas - Conjunto de Test ");
                    System.out.println("-----------------------------------------------------------");
                    System.out.println("El número de aciertos sobre " + Xtest.size() + " imágenes es: " + aciertosTest);
                    System.out.println("% de acierto del clasificador fuerte: " + df.format(porcentajeTest) + '%');
                    System.out.println("###########################################################");
                    
                    clasificador.setConfianza(porcentajeTest);
                    clasificadores.add(clasificador);//Añadimos el clasificador fuerte al ArrayList
                    porcentajeAcumuladoTest += porcentajeTest;
                }
                System.out.println("Media de aciertos ENTRENAMIENTO: " + (porcentajeAcumulado/10));
                System.out.println("Media de aciertos TEST: " + (porcentajeAcumuladoTest/10));
                try {
                    //Añadimos a un fichero
                    generaFichero(clasificadores, args[1]);
                } catch (IOException ex) {
                    System.out.println("No se ha podido encontrar la ruta para crear el archivo");
                    System.out.println("Recuerda ejecutar el programa en modo Entrenamiento antes");
                }

            } else {
                //Se ejecuta la práctica como test
                ArrayList<ClasificadorFuerte> clasificadores= new ArrayList<>();
                try {
                    leerFichero(clasificadores, args[0]);//Obtenemos los clasificadores fuertes del fichero
                } catch (IOException ex) {
                    System.out.println("El fichero no se ha podido leer");
                }
                System.out.println("Clasificación de la imagen");
                System.out.println("-----------------------------------------------------------");
                switch(clasificarImagen(clasificadores, args[1])) {
                    case 0:
                        System.out.println("La imagen es un avión.(0)");
                        break;
                    case 1:
                        System.out.println("La imagen es un automóvil.(1)");
                        break;
                    case 2:
                        System.out.println("La imagen es un pájaro.(2)");
                        break;
                    case 3:
                        System.out.println("La imagen es un gato.(3)");
                        break;
                    case 4:
                        System.out.println("La imagen es un ciervo.(4)");
                        break;
                    case 5:
                        System.out.println("La imagen es un perro.(5)");
                        break;
                    case 6:
                        System.out.println("La imagen es una rana.(6)");
                        break;
                    case 7:
                        System.out.println("La imagen es un caballo.(7)");
                        break;
                    case 8:
                        System.out.println("La imagen es un barco.(8)");
                        break;  
                    case 9:
                        System.out.println("La imagen es un camión.(9)");
                        break;
                    default:
                        System.out.println("La imagen no se ha podido reconocer.");
                        break;
                }
                
                
                
            }
        } else {
            System.out.println("El número de parámetros es incorrecto");
            System.out.println("Las posibilidades son:");
            System.out.println("Adaboost –t <fichero_almacenamiento_clasificador_fuerte>");
            System.out.println("Adaboost <fichero_origen_clasificador_fuerte> <imagen_prueba>");
        }
}
    
}

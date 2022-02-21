/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto02;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

/**
 *
 * @author Estudillo Carranza Jael Alejandro
 */
public class Solution {
            
    //Evaluacion de la funcion de costo
    protected double costo;
    
    //Solcuion de ciudades
    protected ArrayList<Integer>[] sol;
    
    /**
     * Metodo que nos regresa el costo de la solucion 
     * @return costo Es el costo de la solucion
     */
    public double getCosto() {
        return costo;
    }

    /**
     * Metodo que nos permite asignar el costo de una solucion
     * @param costo Es el costo a asignar a la solucion
     */
    public void setCosto(double costo) {
        this.costo = costo;
    }

    /**
     * Metodo que nos regresa el recorrido de las ciudades de la solucion
     * @return sol Es el arreglo de ciudades de la solucion
     */
    public ArrayList<Integer>[] getSol() {
        return sol;
    }

    /**
     * Metodo que nos permite asignar el recorrido de ciudades a la solucion 
     * @param sl Es el recorrido de ciudades a asignar
     * @param cam Es el Id del camion al que se le asignara la ruta
     * @param deposito Es el Id del deposito de origen
     * @param rnd Random para generar el orden de la ruta
     */
    public void setSol(int[] sl, int cam, int deposito, Random rnd) {
        this.sol = new ArrayList[cam];
        for(int i = 0; i < sol.length; i++){
            sol[i] = new ArrayList<Integer>();
            sol[i].add(deposito);
        }
        int[] ver = new int[sl.length];
        int cont = 0;
        int tempCa = 0;
        int tempC = 0;
        int posC = 0;
        while(cont < sl.length){
            tempCa = rnd.nextInt(cam);
            posC = rnd.nextInt(sl.length);
            tempC = sl[posC];
            if(ver[posC] == 0){
                ver[posC]++;
                cont++;
                sol[tempCa].add(tempC);
            }
        }        
    }
    
    /**
     * Metodo que nos permite generar un vecion de la solucion dada
     * @param a Es el primer indice a cambiar
     * @param b Es el segundo indica a cambiar
     * @param op Indica la operacion o tipo de movimiento a realizar
     * @param rnd Random para elegir las posiciones 
     * @param indicator Parametro auxiliar para indicar si se pueden nulificar rutas
     * @return sl Es la solucion vecina a la solucion actual
     */
    public Solution getVecino(int a, int b, int op, Random rnd,int indicator){
        int mode = 2;
        if(indicator == 3)
            mode = 1;
        Solution aux = new Solution();
        aux.sol = new ArrayList[sol.length];
        for(int i = 0; i < sol.length; i++)
            aux.sol[i] = new ArrayList<Integer>(sol[i]);        
        int e1 = rnd.nextInt(sol[a].size())+1;
        int e2 = rnd.nextInt(sol[b].size())+1;
        if(op == 0){// cambio en una lista
            if(aux.sol[a].size() > 2){
                int a1 = rnd.nextInt(aux.sol[a].size());
                int a2 = rnd.nextInt(aux.sol[a].size());
                if(a1 == 0)
                    a1++;
                if(a2 == 0)
                    a2++;
                int t1 = aux.sol[a].get(a1);
                int t2 = aux.sol[a].get(a2);
                aux.sol[a].set(a1, t2);
                aux.sol[a].set(a2, t1);
                return aux;
            }
            if(aux.sol[b].size() > 2){
                int a1 = rnd.nextInt(aux.sol[b].size());
                int a2 = rnd.nextInt(aux.sol[b].size());
                if(a1 == 0)
                    a1++;
                if(a2 == 0)
                    a2++;
                int t1 = aux.sol[b].get(a1);
                int t2 = aux.sol[b].get(a2);
                aux.sol[b].set(a1, t2);
                aux.sol[b].set(a2, t1);
                return aux;
            }
        }
        if(op == 1){//eliminar de una lista y agregar a otra
            if(aux.sol[a].size() > mode){                
                int a1 = rnd.nextInt(aux.sol[a].size());
                if(a1 == 0)
                    a1++;
                int t1 = aux.sol[a].remove(a1);
                aux.sol[b].add(t1);
                return aux;
            }
            if(aux.sol[b].size() > mode){
                int a1 = rnd.nextInt(aux.sol[b].size());
                if(a1 == 0)
                    a1++;
                int t1 = aux.sol[b].remove(a1);
                aux.sol[a].add(t1);                
                return aux;
            }
        }
        if(e1 == sol[a].size())
            e1--;
        if(e2 == sol[b].size())
            e2--;
        
        if(e1 == 0){            
            return this;
        }
        if(e2 == 0){
            return this;
        }        
        int n1 = aux.sol[a].get(e1);
        int n2 = aux.sol[b].get(e2);
        aux.sol[a].set(e1,n2);
        aux.sol[b].set(e2,n1);       
        return aux;
    }

    /**
     * Metodo que nos permite generar un vecion de la solucion dada
     * @param a Es el primer indice a cambiar
     * @param b Es el segundo indica a cambiar
     * @param op Es el indicador de operacion a realizar
     * @param rnd Random para elegir las posiciones a cambiar
     * @return sl Es la solucion vecina a la solucion actual
     */
    public Solution getVecino(int a, int b, int op, Random rnd){
        Solution aux = new Solution();
        aux.sol = new ArrayList[sol.length];
        for(int i = 0; i < sol.length; i++)
            aux.sol[i] = new ArrayList<Integer>(sol[i]);        
        int e1 = rnd.nextInt(sol[a].size())+1;
        int e2 = rnd.nextInt(sol[b].size())+1;
        if(op == 0){// cambio en una lista
            if(aux.sol[a].size() > 2){
                int a1 = rnd.nextInt(aux.sol[a].size());
                int a2 = rnd.nextInt(aux.sol[a].size());
                if(a1 == 0)
                    a1++;
                if(a2 == 0)
                    a2++;
                int t1 = aux.sol[a].get(a1);
                int t2 = aux.sol[a].get(a2);
                aux.sol[a].set(a1, t2);
                aux.sol[a].set(a2, t1);
                return aux;
            }
            if(aux.sol[b].size() > 2){
                int a1 = rnd.nextInt(aux.sol[b].size());
                int a2 = rnd.nextInt(aux.sol[b].size());
                if(a1 == 0)
                    a1++;
                if(a2 == 0)
                    a2++;
                int t1 = aux.sol[b].get(a1);
                int t2 = aux.sol[b].get(a2);
                aux.sol[b].set(a1, t2);
                aux.sol[b].set(a2, t1);
                return aux;
            }
        }
        if(op == 1){//eliminar de una lista y agregar a otra
            if(aux.sol[a].size() > 2){
                int a1 = rnd.nextInt(aux.sol[a].size());
                if(a1 == 0)
                    a1++;
                int t1 = aux.sol[a].remove(a1);
                aux.sol[b].add(t1);
                return aux;
            }
            if(aux.sol[b].size() > 2){
                int a1 = rnd.nextInt(aux.sol[b].size());
                if(a1 == 0)
                    a1++;
                int t1 = aux.sol[b].remove(a1);
                aux.sol[a].add(t1);                
                return aux;
            }
        }
        if(e1 == sol[a].size())
            e1--;
        if(e2 == sol[b].size())
            e2--;
        
        if(e1 == 0){            
            return this;
        }
        if(e2 == 0){
            return this;
        }        
        int n1 = aux.sol[a].get(e1);
        int n2 = aux.sol[b].get(e2);
        aux.sol[a].set(e1,n2);
        aux.sol[b].set(e2,n1);       
        return aux;
    }    
    
    /**
     * Metodo que se encarga de generar el recorrido de las ciudades en formato tsp 
     * @return r Es el recorrido de las ciudades para guardar en un archivo tsp
     */
    public String getRecor(){
        String r = "";
        for(int i = 0; i < sol.length; i++){
            for(int a : sol[i])
                r += a + ",";
        r += r.substring(0, r.length()-1) + "\n";
        }
        return r;
    }
    
    /**
     * Metodo que genera la solucion inicial de la semilla en una cadena para guardarla en un archivo
     * @return Una cadena que representa la solucion inicial de la cadena
     */
    public String getSolIn(){
        String sl = "";
        for(int i = 0; i < sol.length; i++){
            sl += sol[i].toString();
            if(i != sol.length-1)
                sl += "\n";
        }
        return sl;
    }
    
    /**
     * Metodo que nos permite mostrar en cadena el objeto solucion
     * @return El objeto solucion como cadena
     */
    @Override
    public String toString() {        
        String sl = "";
        for(int i = 0; i < sol.length; i++)
            sl += "Camion " + i + ": " + sol[i].toString() + "\n";                
        String res =  "Solucion{" + "costo=" + costo + ", sol=" + sl + '}';
        return res;
    }
    
    /**
     * Metodo que carga la solucion inicial de una semilla en especifico, indicando la ruta del archivo Solution-n.txt
     * @param ruta Es la ruta del archivo Solution-n.txt
     */
    public void cargaSemilla(String ruta){
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        BufferedReader b = null;
        try {
            archivo = new File(ruta);            
            fr = new FileReader (archivo);
            br = new BufferedReader(fr);
            String linea;
            int tam = 0;
            while((linea=br.readLine())!=null)
                tam++;
            if(tam == 0)
                System.err.println("No se proporcionaron los parametros");        
            b = new BufferedReader(new FileReader (archivo));
            ArrayList<Integer>[] in = new ArrayList[tam];
            for(int i = 0; i < tam; i++)
                in[i] = new ArrayList<Integer>(); 
            String ln;
            String[] vl;
            tam = 0;
            while((ln=b.readLine())!=null){
                ln = ln.substring(1, ln.length()-1);
                vl = ln.split(",");
                for(String a : vl)
                    in[tam].add(Integer.parseInt(a.trim()));
                tam++;
            }
            this.sol = in;
        }
        catch(Exception e){
            e.printStackTrace();
        }finally{
            try{                    
                if(null != fr ){
                    fr.close();
                }
            }catch (Exception e2){ 
                e2.printStackTrace();
            }
        }
    }
        
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto02;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

/**
 *
 * @author Estudillo Carranza Jael Alejandro
 */
public class SolutionTW extends Solution{
    
    // Fecha de inicio de proceso
    protected Calendar fechaInicio;
        
    // Contador de tiempo 
    protected Timer tempo;
    
    /**
     * Contructor por defecto
     */
    public SolutionTW(){
        super();
        fechaInicio = Calendar.getInstance();
        tempo = new Timer();
    }
    
    /**
     * Constructor a partir de una fecha
     * @param fecha 
     */
    public SolutionTW(Calendar fecha){
        super();
        fechaInicio = fecha;
        tempo = new Timer();
    }
    
    /**
     * COnstructor a partir de una fecha y el numero de contadores (timers)
     * @param fecha
     * @param size 
     */
    public SolutionTW(Calendar fecha, int size){
        super();
        fechaInicio = fecha;
        tempo = new Timer(fecha,size);
    }
        
    /**
     * Metodo que nos permite consultar la fecha de inicio del proceso de solución
     * @return fechaInicion Es la fecha de inicio de la solución.
     */
    public Calendar getFechaInicio() {
        return fechaInicio;
    }
    
    /**
     * Metodo que nos permite asignar una fecha de inicio de proceso a la solución.
     * @param fechaInicio Es la fecha de inicio de proceso que se le asignara a la
     * solución
     */
    public void setFechaInicio(Calendar fechaInicio) {
        this.fechaInicio = fechaInicio;
    }       
    
    /**
     * Metodo que nos permite obtener el timer de un camion
     * @param i Id del camion a obtener el timer
     * @return Nos regresa el timer que representa a ese camion
     */
    public int[] getTime(int i){
        return this.tempo.getTime(i);
    }
    
    /**
     * Metodo que nos permite asignar un timer a un camion
     * @param time Es el arreglo que representa el timer de uncamion
     * @param i Es el Id del camion a asignar
     */
    public void setTime(int[] time, int i){
        this.tempo.setTime(time, i);
    }
    
    /**
     * Metodo que nos regresa el Timer de la solucion
     * @return EL timer que representa los tiempos de los camiones de la solucion
     */
    public Timer getTempo(){
        return tempo;
    }
    
    /**
     * Metodo que nos permite asignar un Timer a la solucion
     * @param tempo Es el Timer que representa los tiempos a asginar a la solucion
     */
    public void setTempo(Timer tempo) {
        this.tempo = tempo;
    }
    
    /**
     * Metodo que nos permite obtener el campo representativo del dia del Timer 
     * de un camion
     * @param i Es el Id del camion a consultar el Timer
     * @return El numero de dias indicados en el Timer
     */
    public int getDay(int i){
        return this.tempo.getDay(i);
    }
    
    /**
     * Metodo que nos permite asignar el numero de dias al Timer de un camion
     * @param day Es el numero de dias a asignar al timer del camion
     * @param i Es el Id del camion al que corresponde el Timer
     */
    public void setDay(int day, int i){
        this.tempo.setDay(day, i);
    }  
    
    /**
     * Metodo que nos permite obtener el campo representativo de la hora del Timer
     * de un camion
     * @param i Es el Id del camion a consultar el Timer
     * @return El numero de horas indicados en el Timer
     */
    public int getHour(int i){
        return this.tempo.getHour(i);
    }
    
    /**
     * Metodo que nos permite asignar el numero de horas al Timer de un camion
     * @param hour Es el numero de horas a asignar al Timer del camion
     * @param i Es el Id del camion al que corresponde el Timer
     */
    public void setHour(int hour, int i){
        this.tempo.setHour(hour, i);
    }
    
    /**
     * Metodo que nos permite obtener el campo representativo de los minutos del Timer
     * de un camion
     * @param i Es el id del camion a consultar el Timer
     * @return El numero de minutos indicados en el Timer
     */
    public int getMinute(int i){
        return this.tempo.getMinute(i);
    }    
    
    /**
     * Metodo que nos permite asignar el numero de minutos al Timer de un camion
     * @param minute Es el numero de minutos a asignar al Timer del camion
     * @param i Es el Id del camion al que corresponde el Timer
     */
    public void setMinute(int minute, int i){
        this.tempo.setMinute(minute, i);
    }
    
    /**
     * Metodo que nos permite obtener el campo representativo de los segundos del Timer
     * de un camion
     * @param i Es el Id del camion a consultar el Timer
     * @return El numero de segundos indicados en el Timer
     */
    public int getSecond(int i){
        return this.tempo.getSecond(i);
    }
    
    /**
     * Metodo que nos permite asignar el numero de segundos al Timer de un camion
     * @param second Es el numero de segundos a asignar al Timer del camion
     * @param i Es el Id del camion al que corresponde el Timer
     */
    public void setSecond(int second, int i){
        this.tempo.setSecond(second, i);
    }

    /**
     * Metodo que nos permite agregar tiempo al Timer
     * @param hours Numero de horas a agrergar al Timer
     * @param minutes Numero de minutos a agregar al Timer
     * @param i  Numero de segundos a agregar al Timer
     */
    public void addTIme(int hours, int minutes, int i){
        this.tempo.addTIme(hours, minutes, i);
    }
    
    /**
     * Metodo que nos regresa la representacion del objeto como un string
     * @return El string que representa el objeto
     */
    @Override
    public String toString() {        
        return super.toString() + "\n" + "SolucionTW{" + "fechaInicio=" + fechaInicio.getTime() + ", tempo=" + tempo.toString() + '}';
    }
    
    /**
     * Metodo que nos permite asignar el recorrido de ciudades a la solucion
     * @param sl Es el recorrido de ciudades a asignar
     * @param cam Es el Id del camion al que se le asignara la ruta
     * @param deposito Es el Id del deposito de origen
     * @param rnd Random para generar el orden de la ruta
     */
    public void setSol(int[] sl, int cam, int deposito, Random rnd) {
        super.setSol(sl, cam, deposito, rnd);
        this.tempo = new Timer(this.fechaInicio,sol.length);
    }
    
    /**
     * Metodo que nos permite calcular el tiempo maximo de recorrido
     */
    public void calculateMaxTime(){
        this.tempo.calculateMaxTime();
    }
    
    /**
     * Metodo que nos regresa el tiempo maximo de recorrido en segundos
     * @return El mayor tiempo de recorrido de la solucion en segundos
     */
    public int getMaxTime(){
        return this.tempo.getMaxTime();
    }
    
    /**
     * Metodo que nos permite generar un vecion de la solucion dada
     * @param a Es el primer indice a cambiar
     * @param b Es el segundo indica a cambiar
     * @param op Es el indicador de la operacion a realizar
     * @param rnd Random para elegir las posiciones
     * @return sl Es la solucion vecina a la solucion actual
     */
    public SolutionTW getVecino(int a, int b, int op, Random rnd) {
        SolutionTW aux = new SolutionTW(this.getFechaInicio(),sol.length);        
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
     * Metodo que nos permite generar un vecion de la solucion dada
     * @param a Es el primer indice a cambiar
     * @param b Es el segundo indica a cambiar
     * @param op Es el indicador de la operacion a realizar
     * @param rnd Random para elegir las posiciones
     * @param indicator Parametro auxiliar para indicar si se pueden nulificar rutas
     * @return sl Es la solucion vecina a la solucion actual
     */
    public SolutionTW getVecino(int a, int b, int op, Random rnd,int indicator) {
        int mode = 2;
        if(indicator == 3)
            mode = 1;
        SolutionTW aux = new SolutionTW(this.getFechaInicio(),sol.length);        
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
            //if(aux.sol[a].size() > 2){
            if(aux.sol[a].size() > mode){            
                int a1 = rnd.nextInt(aux.sol[a].size());
                if(a1 == 0)
                    a1++;
                int t1 = aux.sol[a].remove(a1);
                aux.sol[b].add(t1);
                return aux;
            }
            //if(aux.sol[b].size() > 2){
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
    
    
}

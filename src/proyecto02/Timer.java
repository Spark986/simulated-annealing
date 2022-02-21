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
public class Timer {
   
    // Fecha de inicio de entregas
    private Calendar fechaInicio;
    
    // Fecha de mayor Tiempo
    private Calendar fechaMaxima;
    
    // Tiempo maximo de recorrido (Segundos)
    private int maxTime;
    
    // Contador de tiempos de camiones
    protected int[][] times;
    
    /**
     * Constructor por defecto
     */
    public Timer(){        
    }    
    
    /**
     * COnstructor de Timer a partir de fecha y tamaño
     * @param date Es la fecha a partir de la cual registra el Timer
     * @param size El numero de camiones a registrar el Timer
     */
    public Timer(Calendar date, int size){
        this.fechaInicio = date;
        this.times = new int[size][4];
        for(int i = 0; i < size; i++){
            times[i][0] = 0;
            times[i][1] = fechaInicio.get(Calendar.HOUR_OF_DAY);
            times[i][2] = fechaInicio.get(Calendar.MINUTE);
            times[i][3] = fechaInicio.get(Calendar.SECOND);
        }              
    }
    
    /**
     * Metodo que nos permite obtener la fecha de inicio
     * @return fechaInicio Es la fecha de inicio del Timer
     */
    public Calendar getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Metodo que nos permite asignar la fecha de inicio del Timer
     * @param fechaInicio Es la fecha de inicio a asignar
     */
    public void setFechaInicio(Calendar fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    
    /**
     * Metodo que nos regresa el arreglo representativo de los Timer de los camiones
     * @return timer Es el arreglo de los TImer de los camiones
     */
    public int[][] getTimes() {
        return times;
    }
    
    /**
     * Metodo que nos permite calcula el tiempo maximo del Timer en segundos
     */
    public void calculateMaxTime(){
        int maxTimes = 0;
        int maxIndex = 0;
        int[] timesCopy = originalTime();
        for(int i = 0; i < times.length; i++){
            try{
                times[i] = balancerTime(timesCopy,times[i]);                
            }catch(Exception e){
                e.printStackTrace();
            }
            int time = (times[i][0] * 24 * 60 *60);
            time += (times[i][1] * 60 * 60);
            time += (times[i][2] * 60);
            time += times[i][3];
            if(time > maxTimes){
                maxTimes = time;                
                maxIndex = i;
            }
        }
        Calendar copy = (Calendar)fechaInicio.clone();

        // Asignamos los valores del calendar Timer
        copy.add(Calendar.DAY_OF_YEAR, times[maxIndex][0]);
        copy.add(Calendar.HOUR,times[maxIndex][1]);
        copy.add(Calendar.MINUTE,times[maxIndex][2]);
        copy.add(Calendar.SECOND,times[maxIndex][3]);
        
        this.fechaMaxima = copy;        
        
        this.maxTime = maxTimes;
    }
    
    /**
     * Metodo que nos permite asignar un arreglo de Timers
     * @param times Es el arreglo de Timers de los camiones
     */
    public void setTimes(int[][] times) {
        this.times = times;
    }
    
    /**
     * Metodo que nos permite obtener el Timer de un camion 
     * @param i Es el Id del camion del cual se obtendra el timer
     * @return El arreglo que representa el Timer de ese camion
     */
    public int[] getTime(int i){
        return times[i];
    }
    
    /**
     * Metodo que permite asignar un timer a un camion
     * @param time Es el timer que se va a asignar
     * @param i Es el Id del camion al que se le asignara el Timer
     */
    public void setTime(int[] time, int i){
        times[i] = time;
    }
    
    /**
     * Metodo que nos permite obtener el campo representativo del dia del TImer
     * de un camion
     * @param i Es el Id del camion a consultar el Timer
     * @return El numero de dias indicados en el Timer
     */
    public int getDay(int i){
        return times[i][0];
    }
    
    /**
     * Metodo que nos permite asignar el numero de dias al Timer de un camion
     * @param day Es el numero de dias a asignar al timer del camion
     * @param i Es el Id del camion al que corresponde el Timer
     */
    public void setDay(int day, int i){
        this.times[i][1] = day;
    }

    /**
     * Metodo que nos permite obtener el campo representativo de la hora del Timer
     * de un camion
     * @param i Es el Id del camion a consultar el Timer
     * @return El numero de horas indicados en el Timer
     */
    public int getHour(int i){
        return times[i][1];
    }
    
    /**
     * Metodo que nos permite asignar el numero de horas al Timer de un camion
     * @param hour Es el numero de horas a asignar al Timer del camion
     * @param i Es el Id del camion al que corresponde el Timer
     */
    public void setHour(int hour, int i){
        this.times[i][1] = hour;
    }

    /**
     * Metodo que nos permite obtener el campo representativo de los minutos del Timer
     * de un camion
     * @param i Es el id del camion a consultar el Timer
     * @return El numero de minutos indicados en el Timer
     */
    public int getMinute(int i){
        return times[i][2];
    }
    
    /**
     * Metodo que nos permite asignar el numero de minutos al Timer de un camion
     * @param minute Es el numero de minutos a asignar al Timer del camion
     * @param i Es el Id del camion al que corresponde el Timer
     */
    public void setMinute(int minute, int i){
        this.times[i][2] = minute;
    }

    /**
     * Metodo que nos permite obtener el campo representativo de los segundos del Timer
     * de un camion
     * @param i Es el Id del camion a consultar el Timer
     * @return El numero de segundos indicados en el Timer
     */
    public int getSecond(int i){
        return times[i][3];
    }
    
    /**
     * Metodo que nos permite asignar el numero de segundos al Timer de un camion
     * @param second Es el numero de segundos a asignar al Timer del camion
     * @param i Es el Id del camion al que corresponde el Timer
     */
    public void setSecond(int second, int i){
        this.times[i][3] = second;
    }

    /**
     * Metodo que permite obtener la fecha maxima de recorrido del Timer
     * @return LA fecha maxima que temo en completarse el recorrido
     */
    public Calendar getFechaMaxima() {
        return fechaMaxima;
    }

    /**
     * Metodo que permite asignar la fecha maxima en la que se completa el recorrido
     * @param fechaMaxima Es la fecha maxima a asignar
     */
    public void setFechaMaxima(Calendar fechaMaxima) {
        this.fechaMaxima = fechaMaxima;
    }
    
    /**
     * Metodo que regresa el tiempo maximo de recorrido en segundos
     * @return El mayor tiempo de recorrido de la solucion en segundos
     */
    public int getMaxTime() {
        return maxTime;
    }
    
    /**
     * Metodo que permite asginar el tiempo maximo de recorrido de una solucion
     * @param maxTime Es el tiempo a asignar como el maximo del recorrido
     */
    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }    
    
    /**
     * Metodo que nos permite agregar tiempo al Timer
     * @param hours Numero de horas a agrergar al Timer
     * @param minutes Numero de minutos a agregar al Timer
     * @param i  Numero de segundos a agregar al Timer
     */
    public void addTIme(int hours, int minutes, int i){
        times[i][2] += minutes;
        times[i][1] += hours;
        // Ajustamos el campo de segundos
        while(times[i][3] >= 60){
            times[i][3] -= 60;
            times[i][2] += 1;
        }
        // Ajustamos el campo de minutos
        while(times[i][2] >= 60){
            times[i][2] -= 60;
            times[i][1] += 1;
        }
        // Ajustamos el campo de horas
        while(times[i][1] >= 24){
            times[i][1] -= 24;
            times[i][0] += 1;
        }
    } 

    /**
     * Metodo que nos regresa la representacion del objeto como un string
     * @return El string que representa el objeto
     */
    @Override
    public String toString() {
        String tempo = "{ fecha de Inicio = " + fechaInicio.getTime() + "\n";
        tempo += "Tiempo Maximo = " + maxTime + "\n";        
        tempo += "fecha Maxima = " + fechaMaxima.getTime() + "\n";
         
        for(int i = 0; i < times.length; i++){            
            Calendar copy = (Calendar)fechaInicio.clone();
            tempo += "[" + i + "] Dias = " + times[i][0] + " Horas = " + 
                    times[i][1] + " Minutos = " + times[i][2] + " Segundos = " + 
                    times[i][3] + "\n";

            // Asignamos los valores del calendar Timer
            copy.add(Calendar.DAY_OF_YEAR, times[i][0]);
            copy.add(Calendar.HOUR,times[i][1]);
            copy.add(Calendar.MINUTE,times[i][2]);
            copy.add(Calendar.SECOND,times[i][3]);
            tempo += ("Fecha de fin: " + copy.getTime() + "\n");
        }        
        tempo += "} \n";
        return tempo;
    }
   
    /**
     * Metodo que nos permite generar una copia del timer de la fecha de inicio
     * @return Un array que representa el timer de la fecha de inicio
     */
    public int[] originalTime(){
        int[] timesCopy = new int[4];
        timesCopy[1] = fechaInicio.get(Calendar.HOUR_OF_DAY);
        timesCopy[2] = fechaInicio.get(Calendar.MINUTE);
        timesCopy[3] = fechaInicio.get(Calendar.SECOND);
        return timesCopy;
    }
    
    /**
     * Metodo que nos permite obtener el timer resultante a partir de la fecha de 
     * inicio. De esta forma podemos obtener le fecha de termino
     * @param timeInicial Es el timer de la fecha de inicio
     * @param timeCalculate Es el timer correspondiente al recorrido de un camion
     * @return El timer resultante para calcular la fecha de fin de un camion
     * @throws Exception En caso de no tener timer configurados correctamente
     */
    public int[] balancerTime(int[] timeInicial, int[] timeCalculate) throws Exception{
        
        // Corregimos SEGUNDOS
        // Si hay mas Segundos en el tiempo Inicial que en el calculado
        if(timeInicial[3] > timeCalculate[3]){
            // Verificamos si hay minutos disponibles
            if(timeCalculate[2] > 0){
               timeCalculate[2]--;
               timeCalculate[3] = (timeCalculate[3] + 60) - timeInicial[3];
            }else{
                // Verificamos si hay HORAS DISPONIBLES
                if(timeCalculate[1] > 0){
                    timeCalculate[1]--;
                    timeCalculate[2] += 59;
                    timeCalculate[3] = (timeCalculate[3] + 60) - timeInicial[3];                    
                }else{
                    //Veficiamos si hay DIAS DISPONIBLES
                    if(timeCalculate[0] > 0){
                        timeCalculate[0]--;
                        timeCalculate[1] += 23;
                        timeCalculate[2] += 59;
                        timeCalculate[3] = (timeCalculate[3] + 60) - timeInicial[3];                                                                
                    }else{
                        throw new Exception("No hay tiempo para rebalancear Segundos");
                    }
                }
            }
        }else{
            // Si hay SEGUNDOS SUFICIENTES 
            timeCalculate[3] -= timeInicial[3];
        }
        
        // Corregimos MINUTOS

        // Si hay mas MINUTOS en el tiempo Inicial que en el calculado
        if(timeInicial[2] > timeCalculate[2]){
            // Verificamos si hay HORAS DISPONIBLES
            if(timeCalculate[1] > 0){
                timeCalculate[1]--;
                timeCalculate[2] = (timeCalculate[2] + 60) - timeInicial[2];                    
            }else{
                //Veficiamos si hay DIAS DISPONIBLES
                if(timeCalculate[0] > 0){
                    timeCalculate[0]--;
                    timeCalculate[1] += 23;
                    timeCalculate[2] = (timeCalculate[2] + 60) - timeInicial[2];
                }else{
                    throw new Exception("No hay tiempo para rebalancear minutos");
                }
            }
        }else{
            // Si hay MINUTOS SUFICIENTES 
            timeCalculate[2] -= timeInicial[2];
        }        

        // Corregimos HORAS

        // Si hay mas HORAS en el tiempo Inicial que en el calculado
        if(timeInicial[1] > timeCalculate[1]){
            //Veficiamos si hay DIAS DISPONIBLES
            if(timeCalculate[0] > 0){
                timeCalculate[0]--;
                timeCalculate[1] = (timeCalculate[1] + 24) - timeInicial[1];
            }else{
                throw new Exception("No hay tiempo para rebalancear HORAS");
            }
        }else{
            // Si hay HORAS SUFICIENTES 
            timeCalculate[1] -= timeInicial[1];
        }        

        return timeCalculate;
        
    }
    
    /**
     * Metodo que nos permite obtener la fecha de termino de un camion en particular
     * @param index Es el Id del camion a consultar fecha de termino
     * @return Un string que indica la fecha de termino de cada camion
     */
    public String getTimeString(int index){
        int[] time = times[index];
        Calendar copy = (Calendar)fechaInicio.clone();
        //Reiniciamos el contador del dia
        copy.set(Calendar.HOUR_OF_DAY, 0);
        copy.set(Calendar.MINUTE, 0);
        copy.set(Calendar.SECOND, 0);
        // Asignamos los valores del calendar Timer
        copy.add(Calendar.DAY_OF_YEAR, time[0]);
        copy.add(Calendar.HOUR,time[1]);
        copy.add(Calendar.MINUTE,time[2]);
        copy.add(Calendar.SECOND,time[3]);
        return copy.getTime().toString();
    }
   
    
    
}

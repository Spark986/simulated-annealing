 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto02;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.Iterator;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.concurrent.*;
import java.time.*;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Estudillo Carranza Jael Alejandro
 */
public class VRP {

    
    /**
     *  Metodo principal del proyecto
     */
    public static void main(String[] args) {
        // Modalidad (Que variacion se usara en la evaluacion)
        int mode = 1;
        // version (Que versionde VRP es la que ejecutara el sistema)
        int version = 1;
        // NUmero de Threads
        int threads = 1;
        // Fecha con la que trabajara el sistema  
        String date = "";
        
        // Ruta de validacion de semilla
        String validation = "";
        //Indicador de validacion
        boolean validate = false;
        
        Calendar calendar = Calendar.getInstance();
        
        for(int i = 0; i < args.length; i++){
            switch(args[i]){
                case "-v":
                    try{
                        version = Integer.parseInt(args[i+1]);
                        i++;
                    } catch(Exception e){
                        System.err.println("No se proporcion el identificador de version");
                        System.exit(0);
                    }
                    break;
                case "-p":
                    try{
                        threads = Integer.parseInt(args[i+1]);
                        i++;
                    } catch(Exception e){
                        System.err.println("No se proporcion el numero de Threads a ejecutar");
                        System.exit(0);
                    }
                    break;
                case "-d":
                    try{
                        date = args[i+1];
                        System.out.println(date);
                        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyy-HH:mm:ss");
                        Date dateObj = df.parse(date);
                        calendar.setTime(dateObj);
                        System.out.println(calendar.getTime());
                        i++;
                    } catch(Exception e){
                        System.err.println("No se proporciono una fecha valida");
                        System.exit(0);
                    }
                    break;
                case "-r":
                    try{
                        validation = args[i+1];
                        System.out.println(validation);
                        threads = 1;
                        validate = true;
                        i++;
                    } catch(Exception e){
                        System.err.println("No se proporciono una fecha valida");
                        System.exit(0);
                    }
                    break;                    
            }
        }        
        
        switch(version){
            case 1:
                if(validate){
                    SimulatedAnnealingParallel CVRP= new SimulatedAnnealingParallel(validate,validation);                                    
                }else{                    
                    SimulatedAnnealingParallel CVRP= new SimulatedAnnealingParallel(threads);                
                }
                break;
            case 2:
                if(validate){
                    SimulatedAnnealingTWParallel VRPTW = new SimulatedAnnealingTWParallel(calendar,validate,validation);                                    
                }else{                
                    SimulatedAnnealingTWParallel VRPTW = new SimulatedAnnealingTWParallel(threads,calendar);                
                }
                break;
        }
        
        
        /*
        RecocidoSimulado principal = new RecocidoSimulado();
        if(args.length == 1)
            principal.verificaSistema(args[0]);
        else
            principal.iniciaSistema();
       */
        
        // Proceso Paralelo
        //new SimulatedAnnealingParallel();
       
        /*
        // Ventanas de Tiempo
        RecocidoSimuladoTW principal = new RecocidoSimuladoTW();
        if(args.length == 1)
            principal.verificaSistema(args[0]);
        else
            principal.iniciaSistema();
        //*/
        
        // Ventanas de Tiempo Paralelo
        //new SimulatedAnnealingTWParallel();
        
        // Ejecutar Codigo
        //-v  2 -p 1 -d 12/05/2010-16:05:40 -r /home/alexander/Documentos/Resultados_TESIS/MEJORES_SOLUCIONES/VRPTW/20-CIUDADES/metodo-1/solucion1323/Semilla-1323.txt
        
        // Probar una semilla
        //-v  2 -p 1 -d 12/05/2010-16:05:40 -r /home/alexander/Documentos/Resultados_TESIS/MEJORES_SOLUCIONES/VRPTW/20-CIUDADES/metodo-1/solucion1323/Semilla-1323.txt
        
    }
    
}

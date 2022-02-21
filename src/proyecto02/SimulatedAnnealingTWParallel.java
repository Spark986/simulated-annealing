/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto02;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.Semaphore;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Estudillo Carranza Jael Alejandro
 */

public class SimulatedAnnealingTWParallel {

    private volatile int contador = 1;
    
    private volatile int iteraciones = 10000;
    
    // Mejor solucion numero
    private double mejor = Double.POSITIVE_INFINITY;

    /// mejor sol valor
    private double vmejor = Double.POSITIVE_INFINITY;
    
    // Mejor solucion en tiempo (ID)
    private double timeSolucion = Double.POSITIVE_INFINITY;
    
    // Mejor solucion en Tiempo (Valor)
    private double timeValue = Double.POSITIVE_INFINITY;
    
    // Numero de hilos
    private int numThreads;
    
    // Semaforo de acceso a la semilla
    private final Semaphore disponibilidad = new Semaphore(1);

    // Semaforo de acceso a la semilla
    private final Semaphore genArchivos = new Semaphore(1);              
           
    private class RSTWParalelo implements Runnable {

        //Instancia de ciudades sobre la cual trabajar
        private int[] ins;

        // Tamaño del lote
        private int tamLote = 500;

        // Numero de iteraciones
        private int itera = 10000;

        // Factor de enfriamiento
        private double factorEnfriamiento= 0.8;

        //Coneccion a la base de datos
        private final Connector cn = new Connector();

        // Porcentaje de distritacion electoral
        private double porcentajeVecinos = 0.89;

        // Temperatura inicial
        private double Temp;

        // Valor de epsilon para la temperatura-promedio
        private double epsilon = 0.001;

        // epsilon para la temperatura inicial
        private double epsilonP = 0.001;

        // epsilon para el promedio-busqueda binari
        private double epsilonT = 0.001;

        //Valor que se pasa inicialmente al algoritmo de temperatura inicial
        private double temperaturaInicial = 8.0;

        // Solution inicial-final
        private SolutionTW s;

        // Promedio de carga/camion
        private double promC = 0;

        // Promedio de soluciones aceptadas
        private double promedioSolAcep;

        //Matriz de valores
        private double[][] matriz;

        // Matriz de valores de tiempo
        private int[][][] matrizTimes;

        // Matriz de CIudades
        private String[] matrizCities;
        
        //Peso promedio
        private double pesoPromedio;

        // distancia maxima del conjunto
        private double distanciaMaxima;

        //Factor libre de castigo
        private double factL = 2.0;

        // Numero de camiones
        private int numCam;

        // Capacidad por camion
        private int capacidad;

        //castigo 
        private double castigo;

        //Generador de numero aleatorios
        private Random rnd = new Random(3999L);

        //Variables para generar archivos de soluciones
        private int numSolucion = 1;

        //Solucion incial
        private SolutionTW in;

        //Solucion Maestra
        private SolutionTW master;

        //Lista de evaluaciones
        private ArrayList<String> eval = new ArrayList<String>();

        //Ruta del archivo de configuracion
        private String ruta = "info.properties";

        //Ruta de registro de pedidos
        private String rpedidos;

        // Identificador de la ciudad donde esta el deposito
        private int deposito;

        // Objeto mail para enviar por correo las soluciones que se obtengan
        private Mail m = null;

        // correo destino
        private String to = null;

        // Semilla de la solucion
        private int seed;

        // Indica el modo de evaluacion del sistema
        // 1 - Tomando en cuenta solo distancias
        // 2 - Castigo de carga
        // 3 - Solo distancias y nulificar rutas
        private int mode = 1;        
        
        // Id del Hilo
        private int Id;

        // Indicador de accion del sistema
        private boolean validate = false;

        // Ruta de Validacion
        private String validateRoute;

        // Fecha de inicio
        private Calendar FechaMaster = Calendar.getInstance();    
        
        
        
        /**
         * Metodo constructor que nos permite generar un objeto para ejecutar 
         * el sistema con un numero determinado de threads tomando como fecha 
         * de incio el momento de ejecucion del sistema
         * @param Id Es el numero de threads que ejecutara el sistema en paralelo
         */
        public RSTWParalelo(int Id){
            this.Id = Id;
        }

        /**
         * Metodo constructor que nos permite generar un objeto para ejecutar 
         * el sistema con un numero determinado de threads y una fecha dada
         * @param Id Es el numero de threads que ejecutara el sistema en paralelo
         * @param FechaMaster Es la fecha desde la cual se inicia el sistema
         */
        public RSTWParalelo(int Id, Calendar FechaMaster){
            this.Id = Id;
            this.FechaMaster = FechaMaster;
        }

        /**
         * Metodo constructor que nos permite generar un objeto para ejecutar 
         * el sistema con un numero determinado de threads y una fecha dada
         * @param Id Es el numero de threads que ejecutara el sistema en paralelo
         * @param FechaMaster Es la fecha desde la cual se inicia el sistema
         */
        public RSTWParalelo(int Id, Calendar FechaMaster, boolean validate, String validateRoute){
            this.Id = Id;
            this.FechaMaster = FechaMaster;
            this.validate = validate;
            this.validateRoute = validateRoute;
        }
        
        
        /**
         * Metodo que regresa la temperatura del sistema.
         * @return Temp La temperatura del sistema.
         */
        public double getTemp() {
            return Temp;
        }

        /**
         * Metodo que nos permite asignar la temperatura al sistema
         * @param Temp La temperatura con la que trabajara el sistema
         */
        public void setTemp(double Temp) {
            this.Temp = Temp;
        }

        /**
         * Metodo que nos regresa la instancia con la que el sistema va a trabajar
         * @return ins Es la instancia con la que el sistema esta trabajando
         */
        public int[] getIns() {
            return ins;
        }

        /**
         * Metodo que nos permite asingar una instancia con la que trabajar al sistema.
         * @param ins Es la instancia con la que el sistema va a trabajar
         */
        public void setIns(int[] ins) {
            this.ins = ins;
        }

        /**
         * Metodo que nos regresa el tamaño de lote del sistema
         * @return tamLote Es el tamaño del lote del sistema
         */
        public int getTamLote() {
            return tamLote;
        }

        /**
         * Metodo que regresa el numero de iteraciones o semillas con las que trabajara el sistema
         * @return itera Es el numero de iteraciones o semillas del sistema
         */
        public int getItera() {
            return itera;
        }

        /**
         * Metod que permite asignar el numero de iteraciones o semillas que hara el sistema
         * @param itera Es el numero de semillas para el sistema
         */
        public void setItera(int itera) {
            this.itera = itera;
        }

        /**
         * Metodo que nos permite asignar un tamaño de lote al sistema 
         * @param tamLote Es el tamaño del lote con el que trabajara el sistema
         */
        public void setTamLote(int tamLote) {
            this.tamLote = tamLote;
        }

        /**
         * Metodo que nos regresa el factor de enfriamiento con el que el sistema esta trabajando
         * @return factorEnfriamiento Es el factor de enfriamiento del sistema.
         */
        public double getFactorEnfriamiento() {
            return factorEnfriamiento;
        }

        /**
         * Metodo que nos permite asignar un factor de enfriamiento al sistema
         * @param factorEnfriamiento Es el factor de enfriamiento con el que trabajara el sistema
         */
        public void setFactorEnfriamiento(double factorEnfriamiento) {
            this.factorEnfriamiento = factorEnfriamiento;
        }

        /**
         * Metodo que nos regresa el porcentaje de vecinos aceptados del sistema.
         * @return Es el porcentaje de vecinos con el que trabaja el sistema.
         */
        public double getPorcentajeVecinos() {
            return porcentajeVecinos;
        }

        /**
         * Metodo que permite asignar un porcentaje de vecinos aceptados al sistema
         * @param porcentajeVecinos Es el porcentaje de vecinos que se la asignara al sistema
         */
        public void setPorcentajeVecinos(double porcentajeVecinos) {
            this.porcentajeVecinos = porcentajeVecinos;
        }

        /**
         * Metodo que permite regresa la epsilon de temperatura del sistema.
         * @return epsilon Es la epsilon del sistema
         */
        public double getEpsilon() {
            return epsilon;
        }

        /**
         * Metodo que permite asignar una epsilon de temperatura al sistema
         * @param epsilon Es la epsilon de temperatura con la que trabajara el sistema
         */
        public void setEpsilon(double epsilon) {
            this.epsilon = epsilon;
        }

        /**
         * Metodo que regresa el numero de solucion actual del sistema
         * @return numSolucion Es el identificador de la solucion actual
         */
        public int getNumSolucion() {
            return numSolucion;
        }

        /**
         * Metodo que permite asignar un identificador a la solucion actual del sistema
         * @param numSolucion Es el identificador de la solucion actual del sistema
         */
        public void setNumSolucion(int numSolucion) {
            this.numSolucion = numSolucion;
        }

        /**
         * Metodo que regresa la solucion inicial del sistema en cada iteracion
         * @return in Es la solucion inicial del sistema en cada iteracion
         */
        public Solution getIn() {
            return in;
        }

        /**
         * Metodo que permite asingar una solucion inicial al sistema en cada iteracion
         * @param in Es la solucion inicial del sistema en cada iteracion
         */
        public void setIn(SolutionTW in) {
            this.in = in;
        }

        /**
         * Metodo que regresa la lista de evaluaciones de cada solucion encontrada
         * @return eval Es la lista de evalucion de soluciones aceptadas por el sistema en una solucion
         */
        public ArrayList<String> getEval() {
            return eval;
        }

        /**
         * Metodo que permite asignar una lista de evaluaciones al sistema 
         * @param eval Es la lista de evaluaciones del sistema 
         */
        public void setEval(ArrayList<String> eval) {
            this.eval = eval;
        }

        /**
         * Metod que regresa la ruta donde se encuentra el archivo con la configuracion para el sistema
         * @return ruta Es la ruta del archivo de configuracion del sistema
         */
        public String getRuta() {
            return ruta;
        }

        /**
         * Metodo que permite asignar la ruta donde se encuentra el archivo de configuracion del sistema
         * @param ruta Es la ruta del archivo de configuracion
         */
        public void setRuta(String ruta) {
            this.ruta = ruta;
        }

        /**
         * Metodo que nos regresa la epsilonP para la generacion de la temperatura inicial
         * @return epsilonP Es la epsilonP para el sistema 
         */
        public double getEpsilonP() {
            return epsilonP;
        }

        /**
         * Metodo que nos permite asignar una epsilonP para la generacion de la temperatura inicial
         * @param epsilonP Es la epsilonP para generar la temperatura inicial
         */
        public void setEpsilonP(double epsilonP) {
            this.epsilonP = epsilonP;
        }

        /**
         * Metodo que nos regresa el numero promedio de soluciones aceptadas
         * @return promedioSolAcep Que es el promedio de soluciones aceptadas
         */
        public double getPromedioSolAcep() {
            return promedioSolAcep;
        }

        /**
         * Metodo que nos permite asignar un promedio de soluciones aceptadas al sistema
         * @param promedioSolAcep Es el Promedio de soluciones aceptadas a asignar al sistema
         */
        public void setPromedioSolAcep(double promedioSolAcep) {
            this.promedioSolAcep = promedioSolAcep;
        }

        /**
         * Metodo que nos regresa el generador el generador de numeros aleatorios del sistema
         * @return rnd Es el generador de numeros aleatorios del sistema
         */
        public Random getRnd() {
            return rnd;
        }

        /**
         * Metodo que nos permite asignar un generador de numeros aleatorios al sistema
         * @param rnd Es el generador de numeros aleatorios a asignar
         */
        public void setRnd(Random rnd) {
            this.rnd = rnd;
        }   

        /**
         * Metodo que nos regresa la epsilonT para el algoritmo de temperatura inicial
         * @return epsilonT Es la epsilonT para el algoritmo de temperatura inicial
         */
        public double getEpsilonT() {
            return epsilonT;
        }

        /**
         * Metodo que nos permite asignar una epsilonT para el algoritmo de temperatura inicial
         * @param epsilonT Es la epsilon T para el algoritmo de temperetura inicial
         */
        public void setEpsilonT(double epsilonT) {
            this.epsilonT = epsilonT;
        }

        /**
         * Metodo que nos regresa la temperatura inicial del sistema
         * @return temperaturaInicial Es la temperatura inicial del sistema que se le dara al algoritmo
         * de temperatura inicial
         */
        public double getTemperaturaInicial() {
            return temperaturaInicial;
        }

        /**
         * Metodo que nos permite asignar una temperatura incial al sistema 
         * @param temperaturaInicial Es la temperatura inicial a asignar
         */
        public void setTemperaturaInicial(double temperaturaInicial) {
            this.temperaturaInicial = temperaturaInicial;
        }

        /**
         * Metodo que nos regresa la solucion inicial o "semilla" para el sistema
         * @return s Es la solucion inicial del sistema
         */
        public SolutionTW getS() {
            return s;
        }

        /**
         * Metodo que nos permite asignar la solucion incial o "semilla" al sistema
         * @param s Es la solucion o semilla que se asigna al sistema
         */
        public void setS(SolutionTW s) {
            this.s = s;
        }

        /**
         * Metodo que nos regresa la matriz de conexiones de las ciudades
         * @return matriz Es la matriz de conexiones de las ciudades
         */
        public double[][] getMatriz() {
            return matriz;
        }

        /**
         * Metodo que permite asignar una matriz de conexiones de las ciudades
         * @param matriz Es la matriz de conexiones para el sistema
         */
        public void setMatriz(double[][] matriz) {
            this.matriz = matriz;
        }

        /**
         * Metodo que nos regresa la matriz de tiempos de servicios por ciudad
         * @return matrizTimes Es la matriz de tiempos de las ciudades.
         */
        public int[][][] getMatrizTimes() {
            return matrizTimes;
        }

        /**
         * Metodo que permite asignar una matriz de tiempos de las ciudades
         * @param matrizTimes Es la matriz de conexiones para el sistema.
         */
        public void setMatrizTimes(int[][][] matrizTimes) {
            this.matrizTimes = matrizTimes;
        }        

                /**
         * Metodo que nos regresa la matriz de Ciudades 
         * @return matrizTimes Es la matriz de Ciudades.
         */
        public String[] getMatrizCities() {
            return matrizCities;
        }

        /**
         * Metodo que permite asignar una matriz de las ciudades
         * @param matrizCities Es la matriz de ciudades para el sistema.
         */
        public void setMatrizCities(String[] matrizCities) {
            this.matrizCities = matrizCities;
        }        
        
        /**
         * Metodo que nos regresa el peso promedio de la instancia del sistema
         * @return pesoPromedio Es el peso promeido de la instancia del sistema
         */
        public double getPesoPromedio() {
            return pesoPromedio;
        }

        /**
         * Metodo que nos permite asignar un peso promedio de la instancia al sistema
         * @param pesoPromedio Es el peso promedio a asignar
         */
        public void setPesoPromedio(double pesoPromedio) {
            this.pesoPromedio = pesoPromedio;
        }

        /**
         * Metodo que nos regresa la distancia maxima de la instancia 
         * @return distanciaMaxima Que es la distancia maxima entre ciudades de la instancia
         */
        public double getDistanciaMaxima() {
            return distanciaMaxima;
        }

        /**
         * Metodo que nos permite asignar la distancia maxima de la instancia al sistema
         * @param distanciaMaxima Es la distancia maxima de la instancia a asignar
         */
        public void setDistanciaMaxima(double distanciaMaxima) {
            this.distanciaMaxima = distanciaMaxima;
        }

        /**
         * Metodo que nos regresa el factor libre de castigo del sistema
         * @return factL Es el factor libre de castigo del sistema
         */
        public double getFactL() {
            return factL;
        }

        /**
         * Metodo que nos permite asignar un factor libre de castigo al sistema
         * @param factL Es el factor libre de castigo a asignar al sistema
         */
        public void setFactL(double factL) {
            this.factL = factL;
        }

        /**
         * Metodo que nos regresa el castigo asignado al sistema
         * @return castigo Es el castigo del sistema
         */
        public double getCastigo() {
            return castigo;
        }

        /**
         * Metodo que nos permite asignar un castigo al sistema
         * @param castigo Es el castigo a asignar al sistema
         */
        public void setCastigo(double castigo) {
            this.castigo = castigo;
        }

        /**
         * Metodo que nos regresa el Promedio de Carga de los camiones
         * @return PromC Es el promC por camion del sistema
         */        
        public double getPromC() {
            return promC;
        }

        /**
         * Metodo que nos permite asingar un promedio de carga de camiones al sistema
         * @param promC Es el nuevo promedio de carga que se le va asignar al sistema
         */        
        public void setPromC(double promC) {
            this.promC = promC;
        }
        
        /**
         * Medoto que nos regresa el numero de camiones en el sistema
         * @return numCam Es el numero de camiones en el sistema
         */        
        public int getNumCam() {
            return numCam;
        }

        /**
         * Metodo que nos permite asignar un nuevo numero de camiones al sistema
         * @param numCam Es el nuevo numero de camiones para asignar al sistema
         */
        public void setNumCam(int numCam) {
            this.numCam = numCam;
        }
        
        /**
         * Metodo que nos permite obtener la capacidad para cada camion en el sistema
         * @return capacidad Es la capacidad que tiene cada camion en el sitema
         */
        public int getCapacidad() {
            return capacidad;
        }

        /**
         * Metodo que nos permite asignar una nueva capacidad para los camiones 
         * del sistema
         * @param capacidad Es la nueva capacidad de los camiones en el sistema
         */        
        public void setCapacidad(int capacidad) {
            this.capacidad = capacidad;
        }

        /**
         * Metodo que nos permite obtener la Solution maestra u origen del 
 sistema.
         * @return mastes Es la solucion origen de todas las iteraciones
         */        
        public SolutionTW getMaster() {
            return master;
        }
        
        /**
         * Metodo que nos permite asignar una nueva solucion origen para el sistema
         * @param master Es la nueva solucion de origen del sistema
         */
        public void setMaster(SolutionTW master) {
            this.master = master;
        }

        /**
         * Metodo que nos permite obtener la ruta del archivo de 
         * relacion de pedidos (cliente - cantidad) del sistema
         * @return rpedidos Es la ruta actual del archivos de pedidos del sistema
         */        
        public String getRpedidos() {
            return rpedidos;
        }

        /**
         * Metodo que nos permite asignar la ruta de un nuevo archivo de relacion
         * de pedidos al sistema 
         * @param rpedidos Es la ruta del nuevo archivo de relaciones de pedidas 
         * para el sistema
         */        
        public void setRpedidos(String rpedidos) {
            this.rpedidos = rpedidos;
        }

        /**
         * Metodo que nos permite obtener el ID de la ciudad que servicar como 
         * deposito u origen del sistema
         * @return deposito Es el ID de la ciudad de deposito para el sistema
         */        
        public int getDeposito() {
            return deposito;
        }
        
        /**
         * Metodo que nos permite asignar una nueva ubicacion de deposito al 
         * sistema
         * @param deposito Es el ID de la nueva ubicacion del deposito
         */
        public void setDeposito(int deposito) {
            this.deposito = deposito;
        }

        /**
         * Metodo que nos permite obtener el objeto Mail que se encarga de 
         * realizar el envio de soluciones al destino asignado en el archivo 
         * properties
         * @return m Es el objeto Mail que usa el sistema para envio de correos
         */
        public Mail getM() {
            return m;
        }

        /**
         * Metodo que nos permite asignar un nuevo objeto Mail para el envio de
         * correos del sistema
         * @param m Es el nuevo objeto Mail que se asignara al sistema
         */        
        public void setM(Mail m) {
            this.m = m;
        }

        /**
         * Metodo que nos permite obtener la direccion de correo electronico a 
         * la que se enviaran los correos del sitema
         * @return to Es el destino de los correo enciados por el sistema
         */        
        public String getTo() {
            return to;
        }

        /**
         * Metodo que nos permite asignar un nuevo destinatario para los correos 
         * generados por el sistema
         * @param to Es el nuevo destinatario de los correos generados por el 
         * sistema
         */        
        public void setTo(String to) {
            this.to = to;
        }
        
        /**
         * Metodo que nos permite obtener la semilla para el generador de numeros 
         * pseudoaleatorios de una iteracion en el sistema.
         * @return seed Es la semilla para el generador de numeros pseudoaletorios
         */        
        public int getSeed() {
            return seed;
        }

        /**
         * Metodo que nos permite asignar una nueva semilla para el generador de
         * numeros pseudoaleatorios del sistema
         * @param seed Es la nueva semilla para el generador de numeros
         */        
        public void setSeed(int seed) {
            this.seed = seed;
        }

        /**
         * Metodo que nos permite obtener que variante se esta ejecuntando 
         * en el sistema y de esta forma sepa las evaluaciones que 
         * debe realizar
         * @return mode Es el Indicador de variante que esta ejecutando el sistema
         */
        public int getMode() {
            return mode;
        }
        
        /**
         * Metodo que nos permite asignar la variante que va a ejecutar el 
         * sistema y de esta forma sepa las evaluaciones de debe realizar
         * @param mode Es el ID de la nueva variante que va a ejecutar el sistema
         */
        public void setMode(int mode) {
            this.mode = mode;
        }

        /**
         * Metodo que nos permite obtener el ID del thread que se esta ejecutando
         * @return Id Identificador del thread que se esta ejecutando
         */        
        public int getId() {
            return Id;
        }

        /**
         * Metodo que nos permite asignar un nuevo ID al thread que se esta 
         * ejecutando
         * @param Id Es el nuevo ID que se le asignara al thread 
         */        
        public void setId(int Id) {
            this.Id = Id;
        }

        /**
         * Metodo que nos permite obtener la fecha de inicio del sistema para 
         * determinar el tiempo de rutas y tiempo de fin
         * @return FechaMaster Es la fecha de inicio del sistema
         */
        public Calendar getFechaMaster() {
            return FechaMaster;
        }
        
        /**
         * Metodo que nos permite asignar una nueva fecha de inicio al sistema 
         * para determinar el tiempo de rutas y tiempo de fin
         * @param FechaMaster Es la nueva fecha de inicio para el sistema
         */
        public void setFechaMaster(Calendar FechaMaster) {
            this.FechaMaster = FechaMaster;
        }
                       
        /**
         * Metodo que se encarga de generar todo el procedimiento en cada semilla
         */
        public void iniciaSistema(){
            iniciarValores();
            genValores(); 
            genSolInicial();
            for(int i = 0; i < itera; i++){
                System.out.println("Semilla " + (i+1));
                iniciarValores();
                genValores(); 
                genSemilla();
                temperaturaInicial();
                aceptacionPorUmbrales();
                if(vel()){
                    System.out.println(in.toString());
                    System.out.println(getS().toString());
                    genFiles();                
                    if(s.getCosto() < vmejor){
                        vmejor = s.getCosto();
                        mejor = numSolucion;
                        saveSolution(0);
                    } 
                }
                numSolucion++;
                System.out.println("---> " + mejor + " <-> " +  vmejor + " <---");
            }        
        }

        /**
         * Metodo que realiza el trabajo para una semilla en especifico
         * @param ruta Es la ruta del archivo Semilla-n.txt
         */
        public void verificaSistema(String ruta){
            try{                
                iniciarValores();
                genValores();        
                genValSem(ruta); 
                temperaturaInicial();
                aceptacionPorUmbrales();
                if(vel()){
                    //System.out.println(s.toString());
                    System.out.println("<<<<-------->>>>");
                    s.calculateMaxTime();
                    funcionCostoVal(s);                    
                    System.out.println(getS().toString());
                    genSVG(ruta);
                    System.out.println("Distancia Recorrida: " + distanciaRecorrida(getS()));
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }


        /**
         * Metodo que nos permite generar los valores iniciales del sistema
         */
        public void genValores(){        
            double sum = 0.0;
            int contE = 0;
            double peso = 0.0;
            double max = 0.0;
            for(int i = 0; i < ins.length; i++){
                for(int j = i+1; j < ins.length; j++){
                    peso = matriz[ins[i]][ins[j]];                
                    if(peso != 0.0){
                        sum += peso;
                        contE++;
                        if(peso > max)
                            max = peso;
                    }                    
                }
            }
            distanciaMaxima = max;        
            pesoPromedio = sum/contE;
            castigo = factL*distanciaMaxima;
        }

        /**
         * Metodo que nos permite inicializar los valores del sistema, apartir del archivo de configuracion
         */
        public void iniciarValores(){
            File archivo = null;
            FileReader fr = null;
            BufferedReader br = null;
            BufferedReader b = null;
            try {
                archivo = new File (ruta);            
                fr = new FileReader (archivo);
                br = new BufferedReader(fr);
                String linea;
                int tam = 0;
                //m = new Mail("","");
                while((linea=br.readLine())!=null)
                    tam++;
                if(tam != 14 && tam != 15)
                    System.err.println("No se proporcionaron todos los parametros");        
                b = new BufferedReader(new FileReader (archivo));
                String ln;
                String[] vl;
                tam = 0;
                while((ln=b.readLine())!=null){
                    vl = ln.split("=");
                    ln = vl[1];
                    switch(tam){
                        case(0):
                            //Tamaño de lote
                            int tamLot = Integer.parseInt(ln);
                            break;
                        case(1):
                            //factor de enfriamiento
                            double fe = Double.parseDouble(ln);
                            factorEnfriamiento = fe;
                            break;
                        case(2):
                            //Porcentaje de vecinos
                            double pv = Double.parseDouble(ln);
                            porcentajeVecinos = pv;
                            break;
                        case(3):
                            // epsilon
                            double eps = Double.parseDouble(ln);
                            epsilon = eps;
                            break;
                        case(4):
                            // epsilonP
                            double ep = Double.parseDouble(ln);
                            epsilonP = ep;                       
                            break;
                        case(5):
                            // epsilonT
                            double et = Double.parseDouble(ln);
                            epsilonT = et;
                            break;
                        case(6):
                            // Temperatura inicial
                            double ti = Double.parseDouble(ln);
                            temperaturaInicial = ti;
                            break;
                        case(7):
                            // factor de castigo
                            double fc = Double.parseDouble(ln);
                            factL = fc;
                            break;
                        case(8):
                            // Numero de camiones
                            int cam = Integer.parseInt(ln);
                            numCam = cam;
                            break;
                        case(9):
                            // Numero de camiones
                            int cap = Integer.parseInt(ln);
                            capacidad = cap;
                            break;
                        case(10):
                            // Ruta de listado de pedidos
                            rpedidos = ln;
                            break;
                        case(11):
                            // factor de castigo
                            int ite = Integer.parseInt(ln);
                            itera = ite;
                            break;
                        case(12):
                            //base de datos
                            cn.setRuta(ln);
                            break;
                        case(13):
                            // Variante a ejecutar
                            mode = Integer.parseInt(ln);
                            if(mode < 0 || mode > 3)
                                mode = 1;
                            //System.out.println("Mode = " + mode);
                            break;
                        case(14):
                            // Correo electronico
                            to = ln;
                            break;                        
                    }
                    tam++;
                }
                matriz = cn.genMatriz();
                matrizTimes = cn.genMatrizTimes();
                iniciaPedidos();
            }
            catch(Exception e){
                e.printStackTrace();
            }finally{
                try{                    
                    if( null != fr ){   
                        fr.close();     
                    }                  
                }catch (Exception e2){ 
                    e2.printStackTrace();
                }
            }
        }

        /**
         * Metodo que lee el archivo pedidos.txt y la carga en el sistema
         */
        private void iniciaPedidos(){
            promC = 0.0;
            File archivo = null;
            FileReader fr = null;
            BufferedReader br = null;
            BufferedReader b = null;
            try {
                archivo = new File (rpedidos);            
                fr = new FileReader (archivo);
                br = new BufferedReader(fr);
                String linea;
                int tam = 0;
                while((linea=br.readLine())!=null)
                    tam++;     
                b = new BufferedReader(new FileReader (archivo));
                ins = new int[tam-1];
                String ln;
                String[] vl;
                tam = 0;
                int dep = 0;
                while((ln=b.readLine())!=null){
                    if(dep == 0){
                        vl = ln.split("-");
                        deposito = Integer.parseInt(vl[1]);
                        dep++;
                    }else{                                    
                        vl = ln.split("-");
                        ins[tam] = Integer.parseInt(vl[0]);
                        matriz[ins[tam]][0] = Integer.parseInt(vl[1]);
                        matriz[0][ins[tam]] = Integer.parseInt(vl[1]);
                        promC += Integer.parseInt(vl[1]);
                        tam++;
                    }
                }
                promC /= numCam;
            }
            catch(Exception e){
                e.printStackTrace();
            }finally{
                try{                    
                    if( null != fr ){   
                        fr.close();     
                    }                  
                }catch (Exception e2){ 
                    e2.printStackTrace();
                }
            }        
        }

        private void verificaTiempo(SolutionTW sol, int[] Inicio, int[] Final, int Minutes, int index){   
            int[] timeInicio = Inicio;
            int[] timeFinal = Final;        

            int[] timeActual = sol.getTime(index);

            if(afterTempo(timeActual,timeInicio)){
                sol.setHour(timeInicio[0],index);
                sol.setMinute(timeInicio[1],index);
                sol.setSecond(timeInicio[2],index);
                sol.addTIme(0, Minutes,index);
            }else{
                if(beforeTempo(timeActual,timeFinal)){
                    sol.addTIme(24, 0,index);
                    sol.setHour(timeInicio[0],index);
                    sol.setMinute(timeInicio[1],index);
                    sol.setSecond(timeInicio[2],index);
                    sol.addTIme(0, Minutes,index);                
                }else{
                    sol.addTIme(0, Minutes,index);
                }

            }

        }

        private void verificaTiempoVal(Timer sol, int[] Inicio, int[] Final, int Minutes, int index){

            int[] timeInicio = Inicio;
            int[] timeFinal = Final;        

            int[] timeActual = sol.getTime(index);
            if(afterTempo(timeActual,timeInicio)){
                sol.setHour(timeInicio[0],index);
                sol.setMinute(timeInicio[1],index);
                sol.setSecond(timeInicio[2],index);
                sol.addTIme(0, Minutes,index);
            }else{
                if(beforeTempo(timeActual,timeFinal)){
                    System.out.println("Cambio de Dia");
                    sol.addTIme(24, 0,index);
                    sol.setHour(timeInicio[0],index);
                    sol.setMinute(timeInicio[1],index);
                    sol.setSecond(timeInicio[2],index);
                    sol.addTIme(0, Minutes,index);                
                }else{
                    sol.addTIme(0, Minutes,index);
                }

            }
        }        
        

        private boolean afterTempo(int[] tempo, int[] time){
            if(tempo[1] < time[0]){
                return true;
            }else{
                if(tempo[1] > time[0]){
                    return false;
                }else{
                    // Verificacion de minutos
                    if(tempo[2] < time[1]){
                        return true;
                    }else{
                        if(tempo[2] > time[1]){
                            return false;
                        }else{
                            // Verificacion de segundos
                            if(tempo[3] < time[2]){
                                return true;
                            }else{
                                if(tempo[3] > time[2]){
                                    return false;
                                }else{
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }

        private boolean beforeTempo(int[] tempo, int[] time){
            if(tempo[1] > time[0]){
                return true;
            }else{
                if(tempo[1] < time[0]){
                    return false;
                }else{
                    // Verificacion de minutos
                    if(tempo[2] > time[1]){
                        return true;
                    }else{
                        if(tempo[2] < time[1]){
                            return false;
                        }else{
                            // Verificacion de segundos
                            if(tempo[3] > time[2]){
                                return true;
                            }else{
                                if(tempo[3] < time[2]){
                                    return false;
                                }else{
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }

        private boolean compareCalendar(Calendar inicio, Calendar fin){
            int hourIn = inicio.get(Calendar.HOUR_OF_DAY);
            int minIn = inicio.get(Calendar.MINUTE);
            int secIn = inicio.get(Calendar.SECOND);

            int hourFn = fin.get(Calendar.HOUR_OF_DAY);
            int minFn = fin.get(Calendar.MINUTE);
            int secFn = fin.get(Calendar.SECOND);

            if(hourIn < hourFn){
                return true;
            }else{
                if(hourIn > hourFn){
                    return false;
                }else{
                    // Verificacion de minutos
                    if(minIn < minFn){
                        return true;
                    }else{
                        if(minIn > minFn){
                            return false;
                        }else{
                            // Verificacion de segundos
                            if(secIn < secFn){
                                return true;
                            }else{
                                if(secIn > secFn){
                                    return false;
                                }else{
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }

        /**
         * Metodo que nos permite calcular la funcion de costo de una solucion del sistema
         * @param s Es la solucion a evaluar
         */    
        public void funcionCosto(SolutionTW s){
            double sum = 0.0;          
            switch(mode){
                case 1:               
                    for(int i = 0; i < s.getSol().length; i++){
                        if(s.tempo.times[i][0] != 0){
                            //System.out.println("Dias distintos de 0 - Mode 1");
                            s.tempo.times[i][0] = 0;
                        }
                        Iterator<Integer> it = s.getSol()[i].iterator();
                        int p1 = 0;
                        int p2 = 0;            
                        if(it.hasNext()){
                            p1 = it.next();
                            while(it.hasNext()){
                                p2 = it.next();

                                sum += matriz[p1][p2];  
                                //Obtencion de tiempo por cliente
                                int[] Inicio = matrizTimes[p2][0];
                                int[] Final = matrizTimes[p2][1];
                                int minutoServicio = 45;
                                int velocidadPromedio = 120;
                                double distancia = matriz[p1][p2];
                                double tiempo = distancia/velocidadPromedio;
                                tiempo *= 60;
                                int addTime = (int) Math.round(tiempo);
                                s.addTIme(0, addTime,i);
                                verificaTiempo(s, Inicio, Final, minutoServicio,i);
                                p1 = p2;                    
                            }
                        }
                    }   
                    s.setCosto(sum);
                    break;
                case 2:
                    double sumP = 0.0;
                    for(int i = 0; i < s.getSol().length; i++){
                        if(s.tempo.times[i][0] != 0){
                            //System.out.println("Dias distintos de 0 - Mode 2");
                            s.tempo.times[i][0] = 0;
                        }
                        Iterator<Integer> it = s.getSol()[i].iterator();
                        int p1 = 0;
                        int p2 = 0;            
                        if(it.hasNext()){
                            p1 = it.next();
                            while(it.hasNext()){
                                p2 = it.next();

                                sum += matriz[p1][p2];  
                                sumP += matriz[0][p2];
                                //Obtencion de tiempo por cliente
                                int[] Inicio = matrizTimes[p2][0];
                                int[] Final = matrizTimes[p2][1];
                                int minutoServicio = 45;
                                int velocidadPromedio = 120;
                                double distancia = matriz[p1][p2];
                                double tiempo = distancia/velocidadPromedio;
                                tiempo *= 60;
                                int addTime = (int) Math.round(tiempo);
                                s.addTIme(0, addTime,i);
                                verificaTiempo(s, Inicio, Final, minutoServicio,i);
                                p1 = p2;                    
                            }
                        }
                        if((sumP >= (1.2)*promC)){
                            sum += (sumP*90000);
                        }else{
                            if((sumP < (0.8)*promC)){
                                sum += ((2-sumP)*90000);
                            }
                        }          
                        sumP = 0.0;            
                    }     
                    s.setCosto(sum);
                    break;
                case 3:
                    int[] original = s.tempo.originalTime();
                    for(int i = 0; i < s.getSol().length; i++){
                        if(s.tempo.times[i][0] != 0){
                            //System.out.println("Dias distintos de 0 - Mode 3");
                            s.tempo.times[i][0] = original[0];
                            s.tempo.times[i][1] = original[1];
                            s.tempo.times[i][2] = original[2];
                            s.tempo.times[i][3] = original[3];
                        }
                        Iterator<Integer> it = s.getSol()[i].iterator();
                        int p1 = 0;
                        int p2 = 0;
                        if(it.hasNext()){
                            p1 = it.next();
                            while(it.hasNext()){
                                p2 = it.next();

                                sum += matriz[p1][p2];  
                                //Obtencion de tiempo por cliente
                                int[] Inicio = matrizTimes[p2][0];
                                int[] Final = matrizTimes[p2][1];
                                int minutoServicio = 45;
                                int velocidadPromedio = 120;
                                double distancia = matriz[p1][p2];
                                double tiempo = distancia/velocidadPromedio;
                                tiempo *= 60;
                                int addTime = (int) Math.round(tiempo);
                                s.addTIme(0, addTime,i);
                                verificaTiempo(s, Inicio, Final, minutoServicio,i);
                                p1 = p2;                    
                            }
                        }
                    }
                    s.setCosto(sum);
                    break;
            }            
        }


        /**
         * Metodo que nos permite calcular la funcion de costo de una solucion del sistema
         * @param s Es la solucion a evaluar
         */    
        public void funcionCostoVal(SolutionTW s) throws Exception{
            matrizCities = cn.genMatrizCities();
            Timer temp = new Timer(s.tempo.getFechaInicio(),s.tempo.getTimes().length);
            int sumP = 0;
            for(int i = 0; i < s.getSol().length; i++){
                System.out.println("Camion " + i);
                Iterator<Integer> it = s.getSol()[i].iterator();
                int p1 = 0;
                int p2 = 0;            
                if(it.hasNext()){
                    p1 = it.next();
                    while(it.hasNext()){
                        p2 = it.next();
                        if((sumP + matriz[0][p2]) > capacidad){
                            throw new Exception("La carga es mayor a la capacidad");
                        }else{
                            sumP += matriz[0][p2];
                        }
                        //Obtencion de tiempo por cliente
                        int[] Inicio = matrizTimes[p2][0];
                        int[] Final = matrizTimes[p2][1];
                        int minutoServicio = 45;
                        int velocidadPromedio = 120;
                        double distancia = matriz[p1][p2];
                        double tiempo = distancia/velocidadPromedio;
                        //System.out.println("Horas: " + tiempo);
                        tiempo *= 60;
                        int addTime = (int) Math.round(tiempo);
                        System.out.println("De " + matrizCities[p1] + " a " + matrizCities[p2]);
                        System.out.println("Minutos: " + addTime);                        
                        System.out.println("Tiempo de partida: " + temp.getTimeString(i));
                        temp.addTIme(0, addTime,i);
                        System.out.println("Tiempo de llegada: " + temp.getTimeString(i));
                        verificaTiempoVal(temp, Inicio, Final, minutoServicio,i);
                        System.out.println("Fin de Servicio: " + temp.getTimeString(i));
                        p1 = p2;                    
                    }
                }
                try{
                    int[] aux = s.getTime(i).clone();
                    System.out.println("-----------------------------");
                    System.out.println("[" + i + "] Dias = " + aux[0] + " Horas = " + 
                        aux[1] + " Minutos = " + aux[2] + " Segundos = " + 
                        aux[3]);
                    int[] porte = temp.balancerTime(temp.originalTime(),temp.getTime(i));                    
                    //int[] porte = temp.getTime(i);
                    System.out.println("[" + i + "] Dias = " + porte[0] + " Horas = " + 
                        porte[1] + " Minutos = " + porte[2] + " Segundos = " + 
                        porte[3]);
                    temp.setTime(aux, i);
                    System.out.println(temp.getTimeString(i));
                }catch(Exception e){
                    e.printStackTrace();
                }
                System.out.println("Camion " + i + ": Carga: " + sumP + "\n");
                sumP = 0;
            }
        }       
        
        /**
         * Metodo que nos permite calcular la distancia recorrida de una solucion del sistema
         * @param s Es la solucion a evaluar
         */    
        public double distanciaRecorrida(SolutionTW s){
            double sum = 0.0;                 
            for(int i = 0; i < s.getSol().length; i++){
                Iterator<Integer> it = s.getSol()[i].iterator();
                int p1 = 0;
                int p2 = 0;            
                if(it.hasNext()){
                    p1 = it.next();
                    while(it.hasNext()){
                        p2 = it.next();
                        sum += matriz[p1][p2];  
                        p1 = p2;                    
                    }
                }
            }
            return sum;
        }        
        
        /**
         * Metodo que nos pemrite generar la solucion inicial al sistema
        */
        public void genSolInicial(){
            rnd = new Random(3999L);
            s = new SolutionTW(FechaMaster);   
            s.setSol(ins,numCam,deposito, rnd);
            funcionCosto(s);
            in = s;
            master = s; 
        }

        /**
         * Metodo que nos pemrite generar la solucion inicial al sistema
        */
        public void genValSem(String ruta){
            String[] rt = ruta.split("/");
            String ln = rt[rt.length-1];
            rt = ln.split("-");
            ln = rt[1];
            System.out.println(ln);
            ln = ln.substring(0, ln.length()-4);
            System.out.println(ln);
            rnd.setSeed(Integer.parseInt(ln));
            s = new SolutionTW(FechaMaster);
            s.cargaSemilla(ruta);
            s.setTempo(new Timer(FechaMaster,s.sol.length));
            funcionCosto(s);       
            s.calculateMaxTime();
            in = s;
            System.out.println(s);        
        }    


        /**
         * Metodo que nos permite generar una nueva semilla para la siguiente ejecucion del sistema
        */
        public void genSemilla(){
            rnd.setSeed(numSolucion);        
            s = master.getVecino(rnd.nextInt(s.getSol().length), rnd.nextInt(s.getSol().length),rnd.nextInt(3),rnd,mode); 
            rnd.setSeed(numSolucion);
            funcionCosto(s);                        
            in = s;
            eval.clear();
        }

        /**
         * Metodo que nos permite obtener el porcentaje de soluciones acaptadas
         * @param t Es la temperatura para obtener los porcentajes aceptados
         * @return El porcentaje de soluciones aceptadas
         */
        public double porcentajesAceptados(double t){
            double c = 0;
            double N = tamLote;
            SolutionTW sp = null;
            int sol = s.getSol().length;
            for(int i = 0; i < N; i++){
                sp = s.getVecino(rnd.nextInt(sol),rnd.nextInt(sol),rnd.nextInt(3),rnd,mode);
                funcionCosto(sp);
                if(sp.getCosto() <= (s.getCosto()+t)){
                    c++;
                    s = sp;
                }
            }
            return c/N;
        }    

        /**
         * Metodo que nos permite realizar la busqueda binaria de las temperaturs
         * @param t1 Es el valor incial de temperaturas
         * @param t2 Es el valor final de temperaturas
         * @return la temperatura adecuada
         */
        public double busquedaBinaria(double t1, double t2){
            double tm = (t1+t2)/2;
            if((t2-t1) < epsilonT)
                 return tm;
            double p = porcentajesAceptados(tm);
            if(Math.abs(porcentajeVecinos-p) < epsilonP){
                return tm;
            }
            if(p > porcentajeVecinos)
                return busquedaBinaria(t1,tm);
            else
                return busquedaBinaria(tm,t2);
        }

        /**
         * Metodo que nos permite generar la temperatura incial para el sistema
         */
        public void temperaturaInicial(){
            double p = porcentajesAceptados(temperaturaInicial);
            if(Math.abs(porcentajeVecinos - p) <= epsilonP){
                Temp = temperaturaInicial;
            }else{
                double t1 = 0.0;
                double t2 = 0.0;
                if(p < porcentajeVecinos){
                    while(p < porcentajeVecinos){
                        temperaturaInicial = 2*temperaturaInicial;
                        p = porcentajesAceptados(temperaturaInicial);                    
                    }
                    t1 = temperaturaInicial/2;
                    t2 = temperaturaInicial;
                }else{
                    while(p > porcentajeVecinos){
                        temperaturaInicial = temperaturaInicial/2;
                        p = porcentajesAceptados(temperaturaInicial);
                    }
                    t1 = temperaturaInicial;
                    t2 = 2*temperaturaInicial;
                }        
                Temp = busquedaBinaria(t1, t2);
                //System.out.println("Temperatura Inicial: "+  Temp);
            }
        }

        /**
         * Metodo que nos permite calcular lotes
         */
        public void calculaLotes(){
            int c = 0;
            double r = 0.0;
            int limit = tamLote*100;
            int count = 0;
            SolutionTW sp = null;   
            int sol = s.getSol().length;
            while(c < tamLote && count < limit){
                sp = s.getVecino(rnd.nextInt(sol),rnd.nextInt(sol),rnd.nextInt(3), rnd,mode);
                funcionCosto(sp);
                if(sp.getCosto() <= (s.getCosto() + Temp)){
                    eval.add("E:" + sp.getCosto());
                    s = sp;
                    c++;
                    r += sp.getCosto();
                }
                count++;            
            }
            promedioSolAcep = r/tamLote;
        }

        /**
         * Metodo de aceptacion por umbrales
        */
        public void aceptacionPorUmbrales(){
            double p = 0.0;
            double q = 0.0;
            while(Temp > epsilon){
                q = Double.POSITIVE_INFINITY;
                while(p < q){
                    q = p;
                    calculaLotes();
                    p = promedioSolAcep;
                }
                Temp = factorEnfriamiento*Temp;
            }
        }

        /**
         * Metodo que nos permite evaluar si la solucion del sistema es factible
         * @return Si la solucion es factible o no
        */
        public boolean vel(){
            int sumT = 0;    
            int sumP = 0;
            switch(mode){
                case 1:
                    for(int i = 0; i < s.getSol().length; i++){            
                        Iterator<Integer> it = s.getSol()[i].iterator();
                        it.next();
                        while(it.hasNext()){
                            int next = it.next();
                            if((sumP + matriz[0][next]) > capacidad)
                                return false;
                            else{
                                sumP += matriz[0][next];
                            }
                        }            
                        sumT += sumP;
                        sumP = 0;            
                    }     
                    if(sumT > s.getSol().length*capacidad)
                        return false;
                    return true;        
                case 2:
                    for(int i = 0; i < s.getSol().length; i++){            
                        Iterator<Integer> it = s.getSol()[i].iterator();
                        it.next();
                        while(it.hasNext()){
                            int next = it.next();
                            if((sumP + matriz[0][next]) > capacidad)
                                return false;
                            else{
                                sumP += matriz[0][next];
                            }
                        }            
                        sumT += sumP;
                        sumP = 0;            
                    }     
                    if(sumT > s.getSol().length*capacidad)
                        return false;
                    return true;        
                case 3:
                    for(int i = 0; i < s.getSol().length; i++){            
                        Iterator<Integer> it = s.getSol()[i].iterator();
                        it.next();
                        while(it.hasNext()){
                            int next = it.next();
                            if((sumP + matriz[0][next]) > capacidad)
                                return false;
                            else{
                                sumP += matriz[0][next];
                            }
                        }            
                        sumT += sumP;
                        sumP = 0;            
                    }     
                    if(sumT > s.getSol().length*capacidad)
                        return false;
                    return true;                            
            }
            
            System.err.println("No se configuro el parametro mode");
            return false;            
        }    

        /**
         * Metodo que se encarga de generar los archivos cuando se encuentra una solucion factible 
         */
        public void genFiles(){
            File dirg = new File("report/report-TW-" + numThreads + "p-" + mode + "m-" + ins.length + numCam + capacidad);
            dirg.mkdirs();
            //String t = "report/report-TW-P-" + ins.length + numCam + capacidad;
            String t = "report/report-TW-" + numThreads + "p-" + mode + "m-" + ins.length + numCam + capacidad;
            String nm = t + "/solution" + numSolucion;
            File dir = new File(nm);
            dir.mkdirs();
            // Archivo de evaluaciones
            File dir0 = new File(nm+"/eval.dat");
            FileWriter eva = null;
            PrintWriter pw = null;
            try{
                eva = new FileWriter(nm+"/eval.dat");
                pw = new PrintWriter(eva);
                for(String s : eval)
                    pw.println(s);
            }catch (Exception e) {
                e.printStackTrace();
            }finally{
                try {
                if(null != eva)
                    eva.close();
                }catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            // Archivo de solucion
            File dir1 = new File(nm+"/progress-"+ (ins.length) + ".tsp");
            FileWriter tsp = null;
            PrintWriter prw = null;
            try{
                tsp = new FileWriter(nm+"/progress-"+ (ins.length) + ".tsp");
                pw = new PrintWriter(tsp);
                pw.println(s.getRecor());
            }catch (Exception e) {
                e.printStackTrace();
            }finally{
                try {
                if(null != tsp)
                    tsp.close();
                }catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            // Archivo de solucion y evaluacion
            File dir2 = new File(nm+"/result-"+ (ins.length) + ".txt");
            FileWriter gen = null;
            PrintWriter rw = null;
            try{
                gen = new FileWriter(nm+"/result-"+ (ins.length) + ".txt");
                rw = new PrintWriter(gen);
                rw.println(in.getRecor());
                rw.println(s.toString());
                rw.println("Distancia Recorrida: " + distanciaRecorrida(getS()));
                if(m != null && to != null)
                    m.sendMessage(to,s.toString(),"result-"+ (ins.length) + ".txt");            
            }catch (Exception e) {
                e.printStackTrace();
            }finally{
                try {
                if(null != gen)
                    gen.close();
                }catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            // Archivo de solucion inicial
            File dir3 = new File(nm+"/Seed"+ (numSolucion) + ".txt");
            FileWriter tsl = null;
            PrintWriter psl = null;
            try{
                tsl = new FileWriter(nm+"/Seed-"+ (numSolucion) + ".txt");
                psl = new PrintWriter(tsl);
                psl.println(in.getSolIn());
            }catch (Exception e) {
                e.printStackTrace();
            }finally{
                try {
                if(null != tsl)
                    tsl.close();
                }catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
            // Archivo de visualizacion
            File dir4 = new File(nm+"/map"+ ".csv");
            FileWriter map = null;
            PrintWriter mapsl = null;
            try{
                map = new FileWriter(nm+"/map"+ ".csv");
                mapsl = new PrintWriter(map);
                for(int i = 0; i < s.getSol().length; i++){
                    mapsl.println("Camion " + i + ":");
                    for(int a : s.getSol()[i])
                        mapsl.println(cn.getCity(a));
                }
            }catch (Exception e) {
                e.printStackTrace();
            }finally{
                try {
                if(null != mapsl)
                    mapsl.close();
                }catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
        }

        /**
         * Metodo que genera un archivo SVG donde se pueden ver una simulacion de las rutas de una solucion
         * @param rta Es la ruta del archivo Semilla-n.txt
         */
        public void genSVG(String rta){
            String [] rut = rta.split("/");
            String nm = "";
            for(int a = 0; a < rut.length-1; a++)
                nm += "/" + rut[a];
            String ruta = nm +"/mapa.csv";
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
                String ln,co;
                String[] vl;
                tam = 0;
                int cl = 10;
                double x1 = 0.0;
                double y1 = 0.0;
                double x2 = 0.0;
                double y2 = 0.0;
                boolean t = false;
                int cr = 0;
                int cg = 0;
                int cb = 0;
                double xi = 0;
                double yi = 0;
                File dir4 = null;
                FileWriter map = null;
                PrintWriter mapsl = null;
                dir4 = new File(nm+"/mapa.html");
                map = new FileWriter(nm+"/mapa.html");
                mapsl = new PrintWriter(map);
                mapsl.println("<?xml version=\"1.0\"?>");
                mapsl.println("<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"900\" height=\"900\">");
                Random dr = new Random(2358);
                cr = dr.nextInt(256);
                cg = dr.nextInt(256);
                cb = dr.nextInt(256);  
                while((ln=b.readLine())!=null){
                    vl = ln.split(",");
                    if(vl.length == 1){   
                        if(t == true){
                            co = "<line x1=\"" + xi + "\" y1=\"" + yi + "\" x2=\"" + x2 + "\" y2=\"" + y2 + "\" stroke=\"rgb(" + cr + "," + cg + "," + cb + ")\" stroke-width=\"1\"/>";
                            mapsl.println(co);
                        }
                        cr = dr.nextInt(256);
                        cg = dr.nextInt(256);
                        cb = dr.nextInt(256); 
                        t = false;
                    }else{
                        if(t == false){
                            x1 = ((Double.parseDouble(vl[0])+500)*3) - 1000;
                            y1 = ((Double.parseDouble(vl[1])+500)*3) - 1000;
                            xi = x1;
                            yi = y1;
                            t = true;
                        }else{
                            x2 = ((Double.parseDouble(vl[0])+500)*3) - 1000;
                            y2 = ((Double.parseDouble(vl[1])+500)*3) - 1000;
                            co = "<line x1=\"" + x1 + "\" y1=\"" + y1 + "\" x2=\"" + x2 + "\" y2=\"" + y2 + "\" stroke=\"rgb(" + cr + "," + cg + "," + cb + ")\" stroke-width=\"1\"/>";
                            mapsl.println(co);
                            x1 = x2;
                            y1 = y2;
                        }
                    }
                    tam++;
                }
                mapsl.println("</svg>");
                mapsl.close();
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

        /**
         * Metodo que se encarga de escribir el archivo de mejor solucion durante la ejecucion del sistema
         * y durante la misma se va actualizando el archivo para que encaso de interrupcion se sepa cual fue la mejor
         */
        public void saveSolution(int option){
            //String t = "report/report-TW-P-" + ins.length + numCam + capacidad;
            String t = "report/report-TW-" + numThreads + "p-" + mode + "m-" + ins.length + numCam + capacidad;
            String fileSave = "";
            switch(option){
                case 0:
                    fileSave = "/bestSolution.txt";
                    break;
                case 1:
                    fileSave = "/bestTimeSolution.txt";
                    break;
            }
            // Archivo de evaluaciones
            File dir0 = new File(t+fileSave);
            FileWriter eva = null;
            PrintWriter pw = null;
            try{
                eva = new FileWriter(t+fileSave);
                pw = new PrintWriter(eva);
                String ln = "";
                switch(option){
                case 0:
                    ln = "---> " + mejor + " <-> " +  vmejor + " <---";
                    break;
                case 1:
                    ln = "---> " + timeSolucion + " <-> " +  timeValue + " <---";
                    break;
            }
                pw.println(ln);
            }catch (Exception e) {
                e.printStackTrace();
            }finally{
                try {
                if(null != eva)
                    eva.close();
                }catch (Exception e2) {
                    e2.printStackTrace();
                }
            }        
        }

        public void run(){
            try{                                
                if(validate){
                    verificaSistema(validateRoute);                    
                }else{                
                    iniciarValores();
                    genValores();
                    genSolInicial();
                    while(contador <= iteraciones){
                        System.out.println("Semilla " + (contador));
                        iniciarValores();
                        genValores();

                        disponibilidad.acquire();
                        System.out.println(Id + " <-> " + contador);
                        seed = contador;
                        numSolucion = seed;

                        contador++;
                        System.out.println(Id + " - " + seed);
                        disponibilidad.release();
                        ////
                        genSemilla();
                        temperaturaInicial();
                        aceptacionPorUmbrales();
                        if(vel()){
                            s.calculateMaxTime();
                            genArchivos.acquire();
                            genFiles();
                            if(s.getCosto() < vmejor){
                                vmejor = s.getCosto();
                                mejor = numSolucion;
                                saveSolution(0);
                            }
                            if(s.getMaxTime() < timeValue){
                                timeValue = s.getMaxTime();
                                timeSolucion = numSolucion;
                                saveSolution(1);                            
                            }
                            genArchivos.release();
                        }
                        System.out.println("---> " + mejor + " <-> " +  vmejor + " <---");
                        System.out.println("---> " + timeSolucion + " <-> " +  timeValue + " <---");
                    }
                }
            }catch(Exception e){
                System.err.println(e.toString());
            }
        }           

        /*
        * Clase interna que maneja el envio de mensajes de correo
        */
        public class Mail {

            // correo desde donde se van a enviar los correos
            String user;
            // pass del correo desde donde se van a enviar los correos
            String pass;
            // Propiedades del sistema
            Properties properties;
            // Sesion para enviar correo
            javax.mail.Session sesion;

            /**
             * Constructor para enviar los correos desde un correo de gmail
             * @param user Correo desde donde se envian los correos
             * @param pass Pass del correo desde donde se envian los correos
             */
            public Mail(String user, String pass){
                properties = System.getProperties();
                properties.setProperty("mail.smtp.host","smtp.googlemail.com");
                properties.setProperty("mail.defaultEncoding","UTF-8"); 
                properties.setProperty("mail.smtp.auth","true");
                properties.setProperty("mail.smtp.starttls.required","true"); 
                properties.setProperty("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory"); 
                properties.setProperty("mail.smtp.socketFactory.fallback","false"); 
                properties.setProperty("mail.smtp.port","465"); 
                properties.setProperty("mail.smtp.socketFactory.port","465");        
                sesion = javax.mail.Session.getDefaultInstance(properties);
                this.user = user;
                this.pass = pass;
            }

            /**
             * Metodo para enviar una solucion obtenida por correo
             * @param to El correo de destino
             * @param message Informacion de la solucion obtenida
             * @param nameFile Nombre del archivo donde estara la informacion
             */
            public void sendMessage(String to, String message, String nameFile){
                try{
                    // Creamos un objeto mensaje tipo MimeMessage por defecto.
                    MimeMessage mensaje = new MimeMessage(sesion);
                    // Asignamos el “de o from” al header del correo.
                    mensaje.setFrom(new InternetAddress(user));
                    // Asignamos el “para o to” al header del correo.
                    mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                    // Asignamos el asunto
                    mensaje.setSubject("Solucion" + numSolucion);
                    String dirin;
                    mensaje.setText(message);
                    mensaje.setFileName(nameFile);
                    // Enviamos el correo                
                    Transport t = sesion.getTransport("smtp");
                    t.connect(user,pass);
                    t.sendMessage(mensaje,mensaje.getAllRecipients());
                    System.out.println("Mensaje enviado");
                } 
                catch (MessagingException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                    System.exit(0);
                }         
            }
        }        
        // final                
    }

    /**
     * Metodo que nos permite obtener el contador de iteraciones realizadas hasta
     * el momento por el sistema.
     * @return contador Es el numero de iteraciones realizadas por el sistema
     */
    public int getContador() {
        return contador;
    }

    /**
     * Metodo que nos permite asignar un nuevo numero de iteraciones realizadas 
     * al sistema 
     * @param contador Es el nuevo numero de iteraciones realizadas por el sistema
     */
    public void setContador(int contador) {
        this.contador = contador;
    }

    /**
     * Metodo que nos permite obtener el numero de iteraciones que debe realizar 
     * el sistema
     * @return iteraciones Es el numero de itetaciones que debe realizar el 
     * sistema.
     */
    public int getIteraciones() {
        return iteraciones;
    }

    /**
     * Metodo que nos permite asignar el numero de iteraciones que debe 
     * realizar el sistema 
     * @param iteraciones Es el nnuevo numero de itraciones que debe realizar 
     * el sistema
     */    
    public void setIteraciones(int iteraciones) {
        this.iteraciones = iteraciones;
    }

    /**
     * Metodo que nos regresa el ID de la mejor solucion encontrada por el sistema
     * hasta el momento
     * @return mejor Es el valor del ID de la mejor solucion encontrada
     */    
    public double getMejor() {
        return mejor;
    }

    /**
     * Metodo que nos permite asignar el ID de la mejor solucion encontrada hasta 
     * el momento
     * @param mejor Es el ID de la mejor solucion encontrada
    */    
    public void setMejor(double mejor) {
        this.mejor = mejor;
    }

    /**
     * Metodo que nos regresa el costo de la mejor solucion encontrada por el 
     * sistema al momento de solicitarla
     * @return vmejor Es el costo de la mejor solucion encontrada por el sistema
     * hasta el momento
     */
    public double getVmejor() {
        return vmejor;
    }

    /**
     * Metodo que nos permite asignar el costo de la mejor solucion encontrada 
     * por el sistema, hasta el momento.
     * @param vmejor Es el costo de la nueva solucion encontrada hasta el momento
     */    
    public void setVmejor(double vmejor) {
        this.vmejor = vmejor;
    }

    /**
     * Metodo que nos regresa el numero de threads con los que esta trabajando el 
     * sistema en paralelo
     * @return numThreads Es el numero de threads con los que esta trabajando el
     * sistema
     */    
    public int getNumThreads() {
        return numThreads;
    }

    /**
     * Metodo que nos permite asignar el numero de threads con los que trabajara 
     * el sistema
     * @param numThreads Es el numero de threads con los que trabajara el sistema
     */    
    public void setNumThreads(int numThreads) {
        this.numThreads = numThreads;
    }           
    
    /**
     * Metodo constructor por defecto que nos permite generar un objeto para 
     * ejecutar el sistema con un solo thread
     */    
    public SimulatedAnnealingTWParallel(){
        int Nhilos = 1;
        Thread[] thd = new Thread[Nhilos];
        for(int i = 0; i < Nhilos; i++){
            thd[i] = new Thread(new RSTWParalelo(i+1));
            thd[i].start();
        }    
    }

    /**
     * Metodo constructor que nos permite generar un objeto para ejecutar el 
     * sistema con un determinado numero de threads
     * @param Nhilos Es el numero de threads que ejecutara el sistema en paralelo
     * el sistema 
     */    
    public SimulatedAnnealingTWParallel(int Nhilos){
        Thread[] thd = new Thread[Nhilos];
        for(int i = 0; i < Nhilos; i++){
            thd[i] = new Thread(new RSTWParalelo(i+1));
            thd[i].start();
        }    
    }

    /**
     * Metodo constructor que nos permite generar un objeto para ejecutar el 
     * sistema con un determinado numero de threads y una fecha de inicio de
     * sistema dada
     * @param Nhilos Es el numero de threads que ejecutara el sistema en paralelo
     * @param date Es la fecha de inicio del sistema
     * el sistema 
     */    
    public SimulatedAnnealingTWParallel(int Nhilos, Calendar date){
        this.numThreads = Nhilos;
        Thread[] thd = new Thread[Nhilos];
        for(int i = 0; i < Nhilos; i++){
            thd[i] = new Thread(new RSTWParalelo((i+1), date));
            thd[i].start();
        }    
    }

    /**
     * Metodo constructor que nos permite generar un objeto para ejecutar el 
     * sistema con un determinado numero de threads y una fecha de inicio de
     * sistema dada
     * @param date Es la fecha de inicio del sistema
     * el sistema 
     */    
    public SimulatedAnnealingTWParallel(Calendar date,boolean validate, String validateRoute){
        this.numThreads = 1;
        Thread[] thd = new Thread[this.numThreads];
        for(int i = 0; i < this.numThreads; i++){
            thd[i] = new Thread(new RSTWParalelo((i+1), date, validate, validateRoute));
            thd[i].start();
        }    
    }
       
}
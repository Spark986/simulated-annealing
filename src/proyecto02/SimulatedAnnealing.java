/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto02;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Iterator;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

/**
 *
 * @author Estudillo Carranza Jael Alejandro
 */
public class SimulatedAnnealing {
        
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
    private Solution s;
            
    // Promedio de carga/camion
    private double promC = 0;
    
    // Promedio de soluciones aceptadas
    private double promedioSolAcep;
    
    //Matriz de valores
    private double[][] matriz;
    
    //Peso promedio
    private double pesoPromedio;
    
    // disntancia maxima del conjunto
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
    private Solution in;
    
    //Solucion Maestra
    private Solution master;
    
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
    
    // Mejor solucion numero
    private double mejor = Double.POSITIVE_INFINITY;
    
    /// mejor sol valor
    private double vmejor = Double.POSITIVE_INFINITY;
    
    // Semilla de la solucion
    private int seed;
    
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
    public void setIn(Solution in) {
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
    public Solution getS() {
        return s;
    }

    /**
     * Metodo que nos permite asignar la solucion incial o "semilla" al sistema
     * @param s Es la solucion o semilla que se asigna al sistema
     */
    public void setS(Solution s) {
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
                    saveSolution();
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
        iniciarValores();
        genValores();        
        genValSem(ruta); 
        temperaturaInicial();
        aceptacionPorUmbrales();
        if(vel()){
            System.out.println(in.toString());
            System.out.println(getS().toString());
            genSVG(ruta);
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
            if(tam != 13 && tam != 14)
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
                        // Correo electronico
                        to = ln;
                        break;                        
                }
                tam++;
            }
            matriz = cn.genMatriz();
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
    
    
    /**
     * Metodo que nos permite calcular la funcion de costo de una solucion del sistema
     * @param s Es la solucion a evaluar
     */    
    public void funcionCosto(Solution s){
        double sum = 0.0;     
        double sumP = 0.0;
        boolean cs = false;
        for(int i = 0; i < s.getSol().length; i++){
            Iterator<Integer> it = s.getSol()[i].iterator();
            int p1 = 0;
            int p2 = 0;            
            if(it.hasNext()){
                p1 = it.next();
                while(it.hasNext()){
                    p2 = it.next();
                    sum += matriz[p1][p2];  
                    sumP += matriz[0][p2];
                    p1 = p2;                    
                }
            }
            ///*
            if((sumP >= (1.2)*promC)){
                sum += (sumP*90000);
            }else{
                if((sumP < (0.8)*promC)){
                    sum += ((2-sumP)*90000);
                }
            }          
            //*/
            sumP = 0.0;            
            
        }
        s.setCosto(sum);
    }
    
    /**
     * Metodo que nos pemrite generar la solucion inicial al sistema
    */
    public void genSolInicial(){
        rnd = new Random(3999L);
        s = new Solution();   
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
        s = new Solution();
        s.cargaSemilla(ruta);
        funcionCosto(s);        
        in = s;
        System.out.println(s);        
    }    
    
    
    /**
     * Metodo que nos permite generar una nueva semilla para la siguiente ejecucion del sistema
    */
    public void genSemilla(){
        rnd.setSeed(numSolucion);        
        s = master.getVecino(rnd.nextInt(s.getSol().length), rnd.nextInt(s.getSol().length),rnd.nextInt(3), rnd); 
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
        Solution sp = null;
        int sol = s.getSol().length;
        for(int i = 0; i < N; i++){
            sp = s.getVecino(rnd.nextInt(sol),rnd.nextInt(sol),rnd.nextInt(3),rnd);
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
        Solution sp = null;   
        int sol = s.getSol().length;
        while(c < tamLote && count < limit){
            sp = s.getVecino(rnd.nextInt(sol),rnd.nextInt(sol),rnd.nextInt(3), rnd);
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
        for(int i = 0; i < s.getSol().length; i++){            
            Iterator<Integer> it = s.getSol()[i].iterator();
            it.next();
            while(it.hasNext()){
                if(sumP > capacidad)
                    return false;
                else{
                    sumP += matriz[0][it.next()];
                }
            }            
            System.out.println(i + "--" + sumP);
            sumT += sumP;
            sumP = 0;            
        }        
        if(sumT > s.getSol().length*capacidad)
            return false;
        return true;        
    }    
    
    /**
     * Metodo que se encarga de generar los archivos cuando se encuentra una solucion factible 
     */
    public void genFiles(){
        File dirg = new File("report/report" + ins.length + numCam + capacidad);
        dirg.mkdirs();
        String t = "report/report" + ins.length + numCam + capacidad;
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
    public void saveSolution(){
        String t = "report/report" + ins.length + numCam + capacidad;
        // Archivo de evaluaciones
        File dir0 = new File(t+"/mejorSolucion.txt");
        FileWriter eva = null;
        PrintWriter pw = null;
        try{
            eva = new FileWriter(t+"/mejorSolucion.txt");
            pw = new PrintWriter(eva);
            String ln = "---> " + mejor + " <-> " +  vmejor + " <---";
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

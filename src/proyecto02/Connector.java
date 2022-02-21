/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto02;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Estudillo Carranza Jael Alejandro
 */
public class Connector {
    
    // Ruta de la base de datos
    private String ruta;
    
    // Conexion a la base de datos
    private Connection connect;

    /**
     * Metodo que nos regresa la ruta de la  base de datos
     * @return ruta Ruta de la base de datos
     */
    public String getRuta() {
        return ruta;
    }
    
    /**
     * Metodo que nos permite asignar la ruta de la base de datos
     * @param ruta Es la ruta de la base de datos
     */
    public void setRuta(String ruta) {
        this.ruta = ruta;
    }           
    
    /**
     * Constructo que crea un objeto Conector para hacer la conexion a la base de datos
     * Con la ruta establecida
     */
    public Connector(){
        ruta = "";
    }
    
    /**
     * Metodo que inicia la conexion a la base de datos
     */
    public void connect(){
        try{
            Class.forName("org.sqlite.JDBC");                 
            connect = DriverManager.getConnection("jdbc:sqlite:"+ruta);
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        } 
    }    

    /**
     * Metodo que cierra la conexion a la base de datos
     */
    public void close(){
        try{
            connect.close();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    
    }
    
    /**
     * Metodo que genera la matriz de conexiones desde la base de datos
     * @return matriz Es la matriz de conexiones de las ciudades
     */
    public double[][] genMatriz(){
        connect();        
        double[][] matriz = new double[52][52];      
        String query = "SELECT * FROM connections";
        try{                    
            Statement cmd = connect.createStatement();
            ResultSet rs = cmd.executeQuery(query);
            while(rs.next()){
                matriz[rs.getInt(1)][rs.getInt(2)] = rs.getDouble(3);
                matriz[rs.getInt(2)][rs.getInt(1)] = rs.getDouble(3);
            }
            rs.close();
            cmd.close();
            return matriz;
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

        /**
     * Metodo que genera la matriz de nombres de ciudades desde la base de datos
     * @return matriz Es la matriz de nombres de las ciudades
     */
    public String[] genMatrizCities(){
        connect();        
        String[] matrizCities = new String[52];      
        String query = "SELECT * FROM cities";
        try{                    
            Statement cmd = connect.createStatement();
            ResultSet rs = cmd.executeQuery(query);
            while(rs.next()){
                matrizCities[rs.getInt(1)] = rs.getString(2);
            }
            rs.close();
            cmd.close();
            return matrizCities;
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    /**
     * Metodo que genera la matriz de conexiones desde la base de datos
     * @return matriz Es la matriz de conexiones de las ciudades
     */
    public int[][][] genMatrizTimes(){
        connect();        
        int[][][] matrizTimes = new int[52][2][3];      
        String query = "SELECT * FROM times";
        try{                    
            Statement cmd = connect.createStatement();
            ResultSet rs = cmd.executeQuery(query);
            while(rs.next()){
                String[] times = rs.getString(2).split(":");
                for(int i = 0; i < 3; i++)
                    matrizTimes[rs.getInt(1)][0][i] = Integer.parseInt(times[i]);
                times = rs.getString(3).split(":");               
                for(int i = 0; i < 3; i++)
                    matrizTimes[rs.getInt(1)][1][i] = Integer.parseInt(times[i]);                
            }
            rs.close();
            cmd.close();
            return matrizTimes;
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    /**
     * Metodo que nos regresa la información de una ciudad a partir de su id
     * @param id Identificador de la ciudad a buscar
     * @return Las coordenadas y el identificador de la ciudad a buscar
     */
    public String getCity(int id){
        connect();        
        try{                    
            PreparedStatement cmd = connect.prepareStatement("select * from cities WHERE id = ?");
            cmd.setInt(1, id);
            ResultSet rs = cmd.executeQuery();
            String ln = "";
            while(rs.next()){
                ln += rs.getDouble(4) + "," + rs.getDouble(5) + "," + rs.getString(1);
            }
            rs.close();
            cmd.close();
            return ln;
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    
}

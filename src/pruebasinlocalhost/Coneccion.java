/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pruebasinlocalhost;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author MERRY
 */
public class Coneccion {
    
    private Connection con;
    private String BD;
    private String usuario;
    private String contrasena;
    public String mensaje;
    private String hostName;

    public Coneccion() {
        con = null;
        mensaje = "";
        BD = "ganaderoPruebaCreate";
        usuario = "root";
        hostName = "localhost";
        contrasena = "";

    }
    
    public boolean Conectar() {
        System.out.println("CONECTAR------->");
        try {
            System.out.println("jdbc:mysql://" + hostName + ":3306" + "/" + BD);
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://" + hostName + ":3306" + "/" + BD, usuario, contrasena);
            return true;
        } catch (ClassNotFoundException e) {

            mensaje = "Ocurrio un error al tratar de establecer conexión con el servidor.\n"
                    + "Verifique que el servidor se encuentra encendido y funcionando bien."
                    + "_____________________________________________________________________\n" + e.getMessage();
            System.out.println("mensaje-----"+mensaje);
            return false;
        } catch (SQLException ex) {
            System.out.println("ex--"+ex.getErrorCode());
            System.out.println("ERROR CONECTAR -- " + ex.getMessage());

            ex.printStackTrace();
//            boolean ret = PING(""+hostName);
            return false;
        }
    }
    
    public int ConectarInicial() {
        System.out.println("CONECTAR------->");
        try {
            System.out.println("jdbc:mys ql://" + hostName + ":3306" + "/" + BD);
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://" + hostName + ":3306" + "/" + BD, usuario, contrasena);
            return 0;
        } catch (ClassNotFoundException e) {

            mensaje = "Ocurrio un error al tratar de establecer conexión con el servidor.\n"
                    + "Verifique que el servidor se encuentra encendido y funcionando bien."
                    + "_____________________________________________________________________\n" + e.getMessage();
            System.out.println("mensaje-----"+mensaje);
            return 2;
        } catch (SQLException ex) {
            System.out.println("ex--"+ex.getErrorCode());
            System.out.println("ERROR CONECTAR -- " + ex.getMessage());
            if(ex.getErrorCode()==0){
                return 10; // EST>A APAGADO EL SERVICIO MYSQL
            }else if(ex.getErrorCode()==1045){
                return 11; // ACCESO DENEGADO USER Y PASS NO SON VALIDOS
            }else if(ex.getErrorCode()==1049){
                return 13; // NO ENCONTRO LA BD
            }
//            ex.printStackTrace();
//            boolean ret = PING(""+hostName);
            return 3;  //NO ENCONTRO BD
        }
    }
    
    public boolean ConectarSinBD(String namebd){
        try {
            System.out.println("***+ConectarSinBD------");
            String drive = "jdbc:mysql://"+hostName+":3306/";
            String bd = "CREATE DATABASE "+namebd +
                        " \n CHARACTER SET = 'latin1'\n" +
                        "  COLLATE = 'latin1_spanish_ci';";
            System.out.println("bd--->"+bd);
            con = (Connection) DriverManager.getConnection(drive,usuario,contrasena);
            PreparedStatement ps = con.prepareStatement(bd);
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        }
    }
    
    private boolean PING(String ip) {
        InetAddress ping;
        //String ip = "192.168.1.10"; // Ip de la máquina remota 
        try {
            System.out.println("ip--->" + ip);
            ping = InetAddress.getByName(ip);
            if (ping.isReachable(5000)) {
                System.out.println(ip + " - responde!");
                return true;
            } else {
                System.out.println(ip + " - no responde!");
                return false;
            }

        } catch (IOException ex) {
            System.out.println(ex);
            return false;
        }

    }
    
    public boolean Conectar(String bd) {
        System.out.println("CONECTAR------->");
        try {
            BD = bd;
            System.out.println("jdbc:mysql://" + hostName + ":3306" + "/" + BD);
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://" + hostName + ":3306" + "/" + BD, usuario, contrasena);
            return true;
        } catch (ClassNotFoundException e) {

            mensaje = "Ocurrio un error al tratar de establecer conexión con el servidor.\n"
                    + "Verifique que el servidor se encuentra encendido y funcionando bien."
                    + "_____________________________________________________________________\n" + e.getMessage();
            System.out.println("mensaje-----"+mensaje);
            return false;
        } catch (SQLException ex) {
            System.out.println("ex--"+ex.getErrorCode());
            System.out.println("ERROR CONECTAR -- " + ex.getMessage());

            ex.printStackTrace();
//            boolean ret = PING(""+hostName);
            return false;
        }
        
        
    }
    
    public boolean EnviarConsultas(ArrayList consultas) throws ClassNotFoundException, SQLException {
        String QuerySQL = null;

        try {
            if (!Conectar()) {
                JOptionPane.showMessageDialog(null, mensaje);
                return false;
            } else {
                if (con == null) {
                    return false;
                }

                if (consultas.size() < 1) {
                    mensaje = "No hay consultas en la cadena enviada.";
                    return false;
                }
                //System.out.println("con--->"+con);
                con.setAutoCommit(false);
                Statement st = con.createStatement();
                System.out.println("consultas.size()---"+consultas.size());
                for (int x = 0; x < consultas.size(); x++) {
                    System.out.println("consultas.get(" + x + ")-->" + consultas.get(x) + "//////////////////////");
                    if (!consultas.get(x).equals("")) {
                        QuerySQL = consultas.get(x).toString();
                        //st.execute(QuerySQL)
                        //System.out.println("EnviarConsultas-("+x+")->"+QuerySQL);
                        boolean s = st.execute(QuerySQL);
                        if (s) {
//                        if (st.execute(QuerySQL)) {
                            con.rollback();
                            con.setAutoCommit(true);
                            con.close();
                            return false;
                        }
                    }
                    //System.out.println("HOLA");
                }
                con.commit();
                con.setAutoCommit(true);
                Desconectar();
                return true;
            }

        } catch (Exception e) {
            System.out.println("eroor--" + e.getMessage());
            con.rollback();
            con.setAutoCommit(true);

            if (mensaje != null && QuerySQL.toUpperCase().indexOf("WHERE") > 0) {
                mensaje += QuerySQL.toUpperCase().split("WHERE")[1];
            }
            Desconectar();
            e.printStackTrace();
            return false;
        }
    }
    
    
    
    private boolean Desconectar() {
        try {
            if (!con.isClosed()) {
                con.close();
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            String mensaje = "Error al tratar de cerrar la conexión.\n"
                    + "___________________________________________________________________\n" + ex.getMessage();
            return false;

        }
    }
}

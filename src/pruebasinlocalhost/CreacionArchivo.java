/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pruebasinlocalhost;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 *
 * @author MERRY
 */
public class CreacionArchivo {
    
    
    public boolean CrearArchivo(String ruta, String Contenido){
        try {
            System.out.println("**********CrearArchivo**********");
            System.out.println("**********ruta**********"+ruta);
            System.out.println("**********Contenido**********"+Contenido);
            File file = new File(ruta);
            // Si el archivo no existe es creado
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(Contenido);
            bw.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
}

package com.app.springapp.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.springapp.entity.Estudiante;
import com.app.springapp.entity.Turno;
import com.app.springapp.model.Mes;
import com.app.springapp.model.TurnosCompletos;
import com.app.springapp.model.TurnosProgramados;

@Repository
public interface TurnoRepository extends CrudRepository<Turno, Long>{

    final String RUTA_ARCHIVOS = "files/";
    final String EXTENSION_ARCHIVOS = ".dat";

    public List<Turno> findByDiaAndMes(int dia, int mes);
    public List<Turno> findByEstudiante(Estudiante estudiante);

    /**
     * Retorna el objeto TurnosProgramados del año especificado. Si el archivo no existe, se crea uno vacío y se retorna.
     * @param anio
     * @return TurnosProgramados del año especificado
     */
    public default TurnosProgramados getTurnosProgramados(int anio){
        String nombreArchivo = "TurnosProgramados" + String.valueOf(anio).concat(EXTENSION_ARCHIVOS);
        File archivo = new File(RUTA_ARCHIVOS+nombreArchivo);

        //Si el archivo no existe se crea un nuevo objeto, se crea un nuevo archivo y se guarda el objeto en él
        if(!archivo.exists()){
            try {
                archivo.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            ObjectOutputStream escritor = null;

            try{
                escritor = new ObjectOutputStream(new FileOutputStream(archivo));
            }catch(IOException e){
                e.printStackTrace();
            }

            TurnosProgramados turnosPro = new TurnosProgramados(anio);

            try{
                escritor.writeObject(turnosPro);
            }catch(IOException e){
                e.printStackTrace();
            }
            try {
                escritor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return turnosPro;
        }

        //Si el archivo sí existe
        ObjectInputStream lector = null;

        try{
            lector = new ObjectInputStream(new FileInputStream(archivo));
        }catch(IOException e){
            e.printStackTrace();
        }

        Object obj = null;
        
        try {
            obj = lector.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        TurnosProgramados turnosPro = null;

        if(obj instanceof TurnosProgramados){
            turnosPro = (TurnosProgramados)obj;
        }else{
            throw new ClassCastException();
        }

        try {
            lector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return turnosPro;
    }

    /**
     * Retorna el objeto TurnosCompletos del año especificado. Si no existe se crea uno vacío y se retorna.
     * @param anio
     * @return TurnosCompletos del año especificado.
     */
    public default TurnosCompletos getTurnosCompletos(int anio){
        String nombreArchivo = "TurnosCompletos" + String.valueOf(anio).concat(EXTENSION_ARCHIVOS);
        File archivo = new File(RUTA_ARCHIVOS + nombreArchivo);

        //Si el archivo no existe se crea un nuevo objeto, se crea un nuevo archivo y se guarda el objeto en él
        if(!archivo.exists()){
            try {
                archivo.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            ObjectOutputStream escritor = null;

            try{
                escritor = new ObjectOutputStream(new FileOutputStream(archivo));
            }catch(IOException e){
                e.printStackTrace();
            }

            TurnosCompletos turnosCom = new TurnosCompletos(anio);

            try{
                escritor.writeObject(turnosCom);
            }catch(IOException e){
                e.printStackTrace();
            }
            try {
                escritor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return turnosCom;
        }

        //Si el archivo sí existe
        ObjectInputStream lector = null;

        try{
            lector = new ObjectInputStream(new FileInputStream(archivo));
        }catch(IOException e){
            e.printStackTrace();
        }

        Object obj = null;
        
        try {
            obj = lector.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        TurnosCompletos turnosPro = null;

        if(obj instanceof TurnosCompletos){
            turnosPro = (TurnosCompletos)obj;
        }else{
            throw new ClassCastException();
        }

        try {
            lector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return turnosPro;
    }

    public default Mes getMesTurnosProgramados(int anio, String mes){
        TurnosProgramados turnosPro = getTurnosProgramados(anio);
        return turnosPro.getMes(mes); 
    }
}

package com.app.springapp.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.app.springapp.entity.Estacion;
import com.app.springapp.entity.Estudiante;
import com.app.springapp.entity.Horario;
import com.app.springapp.entity.Turno;
import com.app.springapp.model.Mes;
import com.app.springapp.model.TurnosCompletos;
import com.app.springapp.model.TurnosProgramados;

@Transactional
@Repository
public interface TurnoRepository extends CrudRepository<Turno, Long> {

    final String RUTA_ARCHIVOS = "files/";
    final String CARPETA_PROGRAMADOS ="turnos_programados/";
    final String CARPETA_COMPLETOS ="turnos_completos/";
    final String EXTENSION_ARCHIVOS = ".dat";

    public List<Turno> findByDiaAndMes(int dia, int mes);

    public List<Turno> findByEstudiante(Estudiante estudiante);

    public List<Turno> findByDiaAndMesAndAnioAndEstacion(int dia, int mes, int anio, Estacion estacion);
    public List<Turno> findByDiaAndMesAndAnioAndEstacionAndHorario(int dia, int mes, int anio, Estacion estacion, Horario horario);

    public long countByDiaAndMesAndAnioAndEstacionAndHorario(int dia, int mes, int anio, Estacion estacion,
            Horario horario);

    public long countByDiaAndMesAndAnioAndHorarioAndEstudiante(int dia, int mes, int anio, Horario horario, Estudiante estudiante);

    public long countByDiaAndMesAndAnioAndHorario(int dia, int mes, int anio, Horario horario);

    @Modifying
    @Query("update Turno t set t.id = :nuevoId where t.id = :actualId")
    public void updateId(@Param("actualId") Long actualId, @Param("nuevoId") Long nuevoId);


    /****************************************
     * ********MÉTODOS PARA ARCHIVOS*********
     ************************************* */
    public default Mes getMesTurnosProgramados(int anio, int mes) {
        TurnosProgramados turnosPro = getTurnosProgramados(anio);
        return turnosPro.getMes(mes);
    }

    /**
     * Actualiza el archivo .dat de TurnosProgramados agregando un nuevo turno
     * programado al día indicado
     * 
     * @param dia
     * @param mes
     * @param anio
     * @return El nuevo número de turnos programados en la fecha indicada.
     */
    public default int agregarTurnoProgramado(int dia, int mes, int anio) {
        String nombreArchivo = "TurnosProgramados" + String.valueOf(anio).concat(EXTENSION_ARCHIVOS);
        File archivo = new File(RUTA_ARCHIVOS + CARPETA_PROGRAMADOS + nombreArchivo);
        TurnosProgramados turnosPro = null;
        /*
         * Si el archivo no existe se crea un nuevo archivo y un nuevo
         * /objeto TurnosProgramados
         */
        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            turnosPro = new TurnosProgramados(anio);
        } else {
            // Si el archivo sí existe
            turnosPro = getTurnosProgramados(anio);
        }

        int cantidadTurnos = turnosPro.agregarTurno(mes, dia);

        /* Se guarda el objeto en el archivo */
        try {
            ObjectOutputStream escritor = new ObjectOutputStream(new FileOutputStream(archivo));
            escritor.writeObject(turnosPro);
            escritor.close();
            
            System.out.println("\n\n\nTurnos programados:\n" +turnosPro.toString() + "\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cantidadTurnos;
    }

    /**
     * Actualiza el archivo .dat TurnosCompletos agregando un nuevo día a los días con turnos completos
     * @param dia
     * @param mes
     * @param anio
     */
    public default void agregarDiaTurnosCompletos(int dia, int mes, int anio) {
        String nombreArchivo = "TurnosCompletos" + String.valueOf(anio).concat(EXTENSION_ARCHIVOS);
        File archivo = new File(RUTA_ARCHIVOS + CARPETA_COMPLETOS + nombreArchivo);
        TurnosCompletos turnosCom = null;
        // Si el archivo no existe se crea un archivo y objeto TurnosCompletos
        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            turnosCom = new TurnosCompletos(anio);
        } else {
            // Si el archivo sí existe
            turnosCom = getTurnosCompletos(anio);
        }

        
        try{
            turnosCom.agregarDiaCompleto(mes, dia);

            ObjectOutputStream escritor = new ObjectOutputStream(new FileOutputStream(archivo));
            escritor.writeObject(turnosCom);
            escritor.close();

            
            System.out.println("\n\n\nTurnos completos:\n" +turnosCom.toString()+ "\n\n");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Actualiza el archivo .dat TurnosCompletos agregando un nuevo día a los días con turnos completos en el
     * horario especificado
     * @param dia
     * @param mes
     * @param anio
     * @param idHorario
     */
    public default void agregarDiaTurnosCompletosHorario(int dia, int mes, int anio, int idHorario) {
        String nombreArchivo = "TurnosCompletos" + String.valueOf(anio).concat(EXTENSION_ARCHIVOS);
        File archivo = new File(RUTA_ARCHIVOS + CARPETA_COMPLETOS + nombreArchivo);
        TurnosCompletos turnosCom = null;
        // Si el archivo no existe se crea un archivo y objeto TurnosCompletos
        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            turnosCom = new TurnosCompletos(anio);
        } else {
            // Si el archivo sí existe
            turnosCom = getTurnosCompletos(anio);
        }

        
        try{
            turnosCom.agregarDiaCompletoHorario(mes, dia, idHorario);

            ObjectOutputStream escritor = new ObjectOutputStream(new FileOutputStream(archivo));
            escritor.writeObject(turnosCom);
            escritor.close();

            
            System.out.println("\n\n\nTurnos completos:\n" +turnosCom.toString()+ "\n\n");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Retorna el objeto TurnosProgramados del año especificado. Si el archivo no
     * existe, se crea uno vacío y se retorna.
     * 
     * @param anio
     * @return TurnosProgramados del año especificado
     */
    public default TurnosProgramados getTurnosProgramados(int anio) {
        String nombreArchivo = "TurnosProgramados" + String.valueOf(anio).concat(EXTENSION_ARCHIVOS);
        File archivo = new File(RUTA_ARCHIVOS + CARPETA_PROGRAMADOS + nombreArchivo);

        // Si el archivo no existe se crea un nuevo objeto y se retorna
        if (!archivo.exists()) {
            return new TurnosProgramados(anio);
        }

        // Si el archivo sí existe
        TurnosProgramados turnosPro = null;

        try {
            ObjectInputStream lector = new ObjectInputStream(new FileInputStream(archivo));
            Object obj = lector.readObject();
            lector.close();

            if (obj instanceof TurnosProgramados) {
                turnosPro = (TurnosProgramados) obj;
            } else {
                throw new ClassCastException();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return turnosPro;
    }

    /**
     * Retorna el objeto TurnosCompletos del año especificado. Si no existe se crea
     * uno vacío y se retorna.
     * 
     * @param anio
     * @return TurnosCompletos del año especificado.
     */
    public default TurnosCompletos getTurnosCompletos(int anio) {
        String nombreArchivo = "TurnosCompletos" + String.valueOf(anio).concat(EXTENSION_ARCHIVOS);
        File archivo = new File(RUTA_ARCHIVOS + CARPETA_COMPLETOS + nombreArchivo);
        TurnosCompletos turnosCom = null;
        // Si el archivo no existe se crea un nuevo objeto y se retorna
        if (!archivo.exists()) {
            return new TurnosCompletos(anio);
        }

        // Si el archivo sí existe
      
        try {
            ObjectInputStream lector = new ObjectInputStream(new FileInputStream(archivo));
            Object obj  = lector.readObject();
            lector.close();

            if (obj instanceof TurnosCompletos) {
                turnosCom = (TurnosCompletos) obj;
            } else {
                throw new ClassCastException();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return turnosCom;
    }

    /**
     * Actualiza el archivo .dat de TurnosProgramados reduciendo un turno
     * programado al día indicado
     * 
     * @param dia
     * @param mes
     * @param anio
     * @return El nuevo número de turnos programados en la fecha indicada.
     */
    public default int eliminarTurnoProgramado(int dia, int mes, int anio) {
        String nombreArchivo = "TurnosProgramados" + String.valueOf(anio).concat(EXTENSION_ARCHIVOS);
        File archivo = new File(RUTA_ARCHIVOS + CARPETA_PROGRAMADOS + nombreArchivo);
        
        TurnosProgramados turnosPro = getTurnosProgramados(anio);
  
        int cantidadTurnos = turnosPro.eliminarTurno(mes, dia);

        /* Se guarda el objeto en el archivo */
        try {
            ObjectOutputStream escritor = new ObjectOutputStream(new FileOutputStream(archivo));
            escritor.writeObject(turnosPro);
            escritor.close();

            
            System.out.println("\n\n\nTurnos programados:\n" +turnosPro.toString()+ "\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cantidadTurnos;
    }

     /**
     * Actualiza el archivo .dat TurnosCompletos eliminando el día a los días con turnos completos.
     * Se hace para turno completo en general y para turno completo por
     * horario
     * Si el día no está completo, no se hace nada.
     * @param dia
     * @param mes
     * @param anio
     * @param idHorario
     */
    public default void eliminarDiaTurnosCompletos(int dia, int mes, int anio, int idHorario) {
        String nombreArchivo = "TurnosCompletos" + String.valueOf(anio).concat(EXTENSION_ARCHIVOS);
        File archivo = new File(RUTA_ARCHIVOS + CARPETA_COMPLETOS + nombreArchivo);

        // Si el archivo no existe no se hace nada
        if (!archivo.exists()) {
            return;
        }

        TurnosCompletos turnosCom = getTurnosCompletos(anio);

        boolean debeGuardar = false;

        if(turnosCom.estaCompleto(mes, dia)){        
            turnosCom.eliminarDiaCompleto(mes, dia);
            debeGuardar = true;
        }
        
        if(turnosCom.estaCompletoHorario(mes, dia, idHorario)){
            turnosCom.eliminarDiaCompletoHorario(mes, dia, idHorario);
            debeGuardar = true;
        }
        
        if(!debeGuardar){
            return;
        }

        try{
            ObjectOutputStream escritor = new ObjectOutputStream(new FileOutputStream(archivo));
            escritor.writeObject(turnosCom);
            escritor.close();

            System.out.println("\n\n\nTurnos completos:\n" +turnosCom.toString()+ "\n\n");
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Retorna todas las fechas posteriores al día actual (incluido) con turnos completos registradas
     * @return lista con las fechas en formato String: "dd-mm-aaa"
     */
    public default List<String> getFechasCompletas(){
        File carpeta = new File(RUTA_ARCHIVOS + CARPETA_COMPLETOS);
        LocalDate fechaActual = LocalDate.now();
        List<File> archivos = Arrays.asList(carpeta.listFiles());

        List<String> fechas = new ArrayList<>();

        for(File archivo: archivos){
            String nombre = archivo.getName();
            String nombre2 = nombre.split("TurnosCompletos")[1];
            int anio= Integer.parseInt(nombre2.split(".dat")[0]);
            if(anio < fechaActual.getYear()){
                continue;
            }
            TurnosCompletos turnosCom = getTurnosCompletos(anio);
            Set<Integer> meses = turnosCom.getMesesRegistrados();

            for(Integer mes: meses){
                if(mes < fechaActual.getMonth().getValue()){
                    continue;
                }
                Set<Integer> diasCompletos = turnosCom.diasCompletosMes(mes);
                for(Integer dia: diasCompletos){
                    if(dia < fechaActual.getDayOfMonth()){
                        continue;
                    }
                    fechas.add(dia + "-" + mes + "-" + anio);
                }
            }
        }

        return fechas;
    }

    /**
     * Retorna las fechas posteriores al día actual (incluido) con turnos completos dado un horario
     * @return lista con las fechas en formato String: "dd-mm-aaa"
     */
    public default List<String> getFechasCompletas(int idHorario){
        File carpeta = new File(RUTA_ARCHIVOS + CARPETA_COMPLETOS);
        LocalDate fechaActual = LocalDate.now();
        List<File> archivos = Arrays.asList(carpeta.listFiles());

        List<String> fechas = new ArrayList<>();

        for(File archivo: archivos){
            String nombre = archivo.getName();
            String nombre2 = nombre.split("TurnosCompletos")[1];
            int anio= Integer.parseInt(nombre2.split(".dat")[0]);
            if(anio < fechaActual.getYear()){
                continue;
            }
            TurnosCompletos turnosCom = getTurnosCompletos(anio);
            Set<Integer> meses = turnosCom.getMesesRegistrados();

            for(Integer mes: meses){
                if(anio < fechaActual.getYear() && mes < fechaActual.getMonth().getValue()){
                    continue;
                }
                Set<Integer> diasCompletos = turnosCom.diasCompletosMesHorario(mes,idHorario);
                for(Integer dia: diasCompletos){
                    if(anio < fechaActual.getYear() && mes < fechaActual.getMonth().getValue() && dia < fechaActual.getDayOfMonth()){
                        continue;
                    }
                    fechas.add(dia + "-" + mes + "-" + anio);
                }
            }
        }

        return fechas;
    }
}

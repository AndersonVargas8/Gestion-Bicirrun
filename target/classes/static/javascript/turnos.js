$(document).ready(function () {
    $("#selMes").change(function () {
        document.getElementById("selDia").setAttribute("disabled","disabled");
        let mes = document.getElementById("selMes").value;
        var url = "/actFormTurnosMes/" + mes;
        $("#carta").load(url);
    });

    $("#selDia").change(function () {
        document.getElementById("selHorario").setAttribute("disabled","disabled");
        let mes = document.getElementById("selMes").value;
        let dia = document.getElementById("selDia").value;
        let idHorario = document.getElementById("selHorario").value;
        var url = "/actFormTurnosDiaMes/" + dia + "/" + mes + "/" + idHorario;
        $("#carta").load(url);
    });

    $("#selHorario").change(function () {
        document.getElementById("selEstacion").setAttribute("disabled","disabled");
        let mes = document.getElementById("selMes").value;
        let dia = document.getElementById("selDia").value;
        if(dia == 0){
            document.getElementById("selDia").setAttribute("disabled","disabled");
        }
        let idHorario = document.getElementById("selHorario").value;

        var url = "/actFormTurnosDiaMesHorario/" + dia + "/" + mes + "/" + idHorario;
        $("#carta").load(url);
    });

    $("#selMesCalendar").change(function () {
        let mes = document.getElementById("selMesCalendar").value;
        
        var url = "/actCalendario/" + mes;
        $("#divCalendario").load(url);
    }); 

    $('[data-toggle="tooltip"]').tooltip();
    
    $("#selMesCalendar").datepicker("setDate",new Date());
    document.getElementById("mesAtras").addEventListener("click",mesAtras);
    document.getElementById("mesAdelante").addEventListener("click",mesAdelante);
 


});

function mesAtras(){
    
    let mes = document.getElementById("selMesCalendar").value.split(" ");
    let nuevoMes = cambiarMes(mes[0],mes[1],false)
    $("#selMesCalendar").datepicker("setDate",new Date(nuevoMes));
}
function mesAdelante(){
    let mes = document.getElementById("selMesCalendar").value.split(" ");
    let nuevoMes = cambiarMes(mes[0], mes[1])
    $("#selMesCalendar").datepicker("setDate",new Date(nuevoMes));
}

function cambiarMes(mes, anio, adelantar = true){
    meses = [
        "Enero",
        "Febrero",
        "Marzo",
        "Abril",
        "Mayo",
        "Junio",
        "Julio",
        "Agosto",
        "Septiembre",
        "Octubre",
        "Noviembre",
        "Diciembre"
    ]
    
    for(let i = 0; i < meses.length; i++){
        if(meses[i] == mes){
            if(adelantar){
                if(i == 11){
                    return "01-01-"+ ++anio;
                }
                return  "" +(++i +1) + "-01-"+ anio;
            }else{
                if(i == 0){
                    return "12-01-" + --anio;
                }
                return "" + i + "-01-"+ anio;
            }
        }
    }

    return mes;
}
function verDia(dia){
    let mes = document.getElementById("selMesCalendar").value;
    var url = "/turnosEstacionesDia/" + dia + "/" + mes;
        $("#divTurnosEstaciones").load(url);
    
    $("#modalTurnosEstaciones").modal();
}

function eliminarTurno(){
    
    let id = $("#turnoIdHiddenInput").val();
    
    $('#deleteModalTurno').modal('hide');
    window.location = "/eliminarTurno/" + id;
    
}

function abrirVerdia(){
    $("#modalTurnosEstaciones").modal();
}

function abrirConfirm(id){
    $('#deleteModalTurno').modal();
    
	$("#turnoIdHiddenInput").val(id);
}
function cerrarConfirm(){
    $('#deleteModalTurno').modal('hide');
}

function abrirNuevoTurno(mes, dia, idHorario, idEstacion){
    var url = "/crearTurnoDefinido/" + mes + "/" + dia + "/" + idHorario + "/" + idEstacion;
    $("#carta").load(url);
    $("#modalTurnosEstaciones").modal('hide');
    $("#modalFormTurnos").modal();
}
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
        var url = "/actFormTurnosDiaMes/" + dia + "/" + mes;
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


});

function mensaje(num,nam){
    alert("Este es un mensaje " +num + " " + nam);
    $("#modalFormTurnos").modal();
}
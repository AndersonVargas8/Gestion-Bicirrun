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

});
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
        var url = "/actFormTurnosDiaMes/" + dia + "/" + mes;
        $("#carta").load(url);
    });

});
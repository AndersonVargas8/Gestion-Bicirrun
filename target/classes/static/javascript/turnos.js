$(document).ready(function () {
    $("#selMes").change(function () {
        document.getElementById("selDia").setAttribute("disabled","disabled");
        let mes = document.getElementById("selMes").value;
        var url = "/actFormTurnos/" + mes;
        $("#carta").load(url);
    });

});
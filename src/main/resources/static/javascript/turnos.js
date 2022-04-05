$(document).ready(function () {
    $("#selMes").change(function () {
        let mes = document.getElementById("selMes").value;
        var url = "/actFormTurnos/" + mes;
        $("#carta").load(url);
    });

});

function eliminar(dato) {
    var url = "eliminarEstudiante";
    $.ajax({
        type: 'POST',
        url: url,
        data: "id=" + dato,
        success: function (data, textStatus, jqXHR) {
        }
    })
    //url="/eliminarEstudiante/"+dato;
    //$("html").load(url);
    location.href = "/estudiantes";
}
function editar(dato) {
    url = "/editarEstudiante/" + dato;
    $("body").load(url);
    $("#modalRegistro").modal();
}
let table = new DataTable('#userList', {
    language:{
        "url":"//cdn.datatables.net/plug-ins/1.10.16/i18n/Spanish.json"
    }
});




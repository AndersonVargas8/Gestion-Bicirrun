
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


function horario(estudiante){
    var url = "/estudiantes/horarioEstudiante/" + estudiante.id;
        $("#divHorarioEstudiante").load(url);
    
    $("#ModalEstudiante").modal();
}

document.addEventListener("DOMContentLoaded",()=>{
    const $boton = document.querySelector("#botonImprimir");
    $boton.addEventListener("click",()=>{
        const $elementoParaConvertir = document.getElementById("tablaHorario");
        html2pdf()
            .set({
                margin:1,
                filename:document.getElementById("nombrePDF").value+"horario.pdf",
                image:{
                    type:'jpeg',
                    quality:0.98
                },
                html2canvas:{
                    scale:3,
                    letterRendering:true,
                },
                jsPDF:{
                    unit:"in",
                    format:"a4",
                    orientation:'landscape'
                },
                
            })
            .from($elementoParaConvertir)
            .save()
            .catch(err => console.log(err));
    });
});
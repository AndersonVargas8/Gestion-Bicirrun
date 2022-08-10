//Activar navbar
document.querySelector("#item-estudiantes").classList.add("active");

//Activar Selectpickers
try {
    let select_box_element = document.querySelector("#inputState");
  
    dselect(select_box_element, {
      search: true,
    });
  } catch (error) {}

 //Activar Selectpickers
try {
    let select_box_element = document.querySelector("#inputStateUpdate");
  
    dselect(select_box_element, {
      search: true,
    });
  } catch (error) {} 

function Guardar(estudiante){
    url="/estudiantes"
}
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
function editarForm(estudiante) {    
    $(".modal-title").text("Modificar información")
    $("#Nombre").val(estudiante.nombres);
    $("#Apellido").val(estudiante.apellidos);
    $("#Documento").val(estudiante.documento);
    $("#telefono").val(estudiante.telefono);
    let carrera = document.querySelector("#inputState option[value='"+estudiante.carrera.id+"']");
    carrera.selected = true;
    document.querySelector(".dselect-wrapper button").innerHTML=carrera.text;
    $("#modalRegistro").modal('show');    
}

function Guardar(){
    let form = document.forms["formularioRegistro"];
    let nombre = form["Nombre"];
    let apellido = form["Apellido"];
    let documento = form["Documento"];
    let telefono = form["telefono"];
    let carrera = form[""];
}

function editarPressed(){
    
}
let table = new DataTable('#userList', {
    language: {
        "url": "//cdn.datatables.net/plug-ins/1.10.16/i18n/Spanish.json"
    }
});


function horario(estudiante) {
    var url = "/estudiantes/horarioEstudiante/" + estudiante.id;
    $("#divHorarioEstudiante").load(url);

    $("#ModalEstudiante").modal('show');
}

document.addEventListener("DOMContentLoaded", () => {
    const $boton = document.querySelector("#botonImprimir");
    $boton.addEventListener("click", () => {
        const $elementoParaConvertir = document.getElementById("tablaHorario");
        html2pdf()
            .set({
                margin: 1,
                filename: document.getElementById("nombrePDF").value + "-horario.pdf",
                image: {
                    type: 'jpeg',
                    quality: 0.98
                },
                html2canvas: {
                    scale: 3,
                    letterRendering: true,
                },
                jsPDF: {
                    unit: "in",
                    format: "a4",
                    orientation: 'landscape'
                },

            })
            .from($elementoParaConvertir)
            .save()
            .catch(err => console.log(err));
    });
});

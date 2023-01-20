//Activar navbar
document.querySelector("#item-estudiantes").classList.add("active");
document.querySelector("#mensajeError").style.display = "none";

let edicion = false;
let id_edicion = 0;
crearTabla()
//Activar Selectpickers
try {
    let select_box_element = document.querySelector("#inputState");

    dselect(select_box_element, {
        search: true,
    });
} catch (error) { }

//Activar Selectpickers
try {
    let select_box_element = document.querySelector("#inputStateUpdate");

    dselect(select_box_element, {
        search: true,
    });
} catch (error) { }

//data Table
function crearTabla() {
    let table = new DataTable('#userList', {
        "order": [[1, "asc"]],
        language: {
            "url": "//cdn.datatables.net/plug-ins/1.10.16/i18n/Spanish.json"
        }
    });
    return table
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
    $("#modalRegistro .modal-title").text("Modificar información")
    $("#Nombre").val(estudiante.nombres);
    $("#Apellido").val(estudiante.apellidos);
    $("#Documento").val(estudiante.documento);
    $("#telefono").val(estudiante.telefono);
    let carrera = document.querySelector("#inputState option[value='" + estudiante.carrera.id + "']");
    carrera.selected = true;
    document.querySelector(".dselect-wrapper button").innerHTML = carrera.text;
    edicion=true;
    id_edicion=estudiante.id
    console.log("el id a editar: "+id_edicion)
    $("#modalRegistro").modal('show');
}
function crearNuevo() {

    $("#modalRegistro .modal-title").text("Nuevo estudiante")
    let form = document.forms["formularioRegistro"];
    form.reset()
    let carrera = document.querySelector("#inputState option[value='" + 1 + "']");
    carrera.selected = true;
    document.querySelector(".dselect-wrapper button").innerHTML = carrera.text;
    edicion=false;
    id_edicion=0

}

function Guardar() {

    let form = document.forms["formularioRegistro"];
    let estudiante = {
        nombres: form["Nombre"].value,
        apellidos: form["Apellido"].value,
        documento: form["Documento"].value,
        telefono: form["telefono"].value,
        carrera: form["inputState"].value
    }
    activarSpinner(document.querySelector("#BtnGuardar"));


    let url = "/estudiantes";
    let type = "POST";
    if (edicion) {
        url = "/estudiantes/" + id_edicion;
        type = "PUT";
        console.log("Editando.. id:"+id_edicion+" "+estudiante)
      }
    $.ajax({
        url: url,
        type: type,
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(estudiante),
        success: function () {
            desactivarSpinner(document.querySelector("#BtnGuardar"));
            defaultSuccessNotify("Estudiante guardado");
            form.reset();
            //resetForm();
            $("#modalRegistro").modal("hide");
            $("#TablaId").load("/estudiantes/TablaEstudiantes", { limit: 25 }, function () {
                crearTabla();
            })
        },
        error: function (jqXHR) {
            desactivarSpinner(document.querySelector("#BtnGuardar"));
            if (jqXHR.status != 400) {
                defaultErrorNotify();
                return;
            }
            console.log("error " + jqXHR.status + " " + jqXHR.responseText);
            document.querySelector("#mensajeError").innerHTML = jqXHR.responseText;
            document.querySelector("#mensajeError").style.display = "block";
        },
    });


}

function editarPressed() {

}



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

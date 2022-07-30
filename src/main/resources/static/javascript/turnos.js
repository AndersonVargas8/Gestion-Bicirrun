//Activar Datepickers
$("#selMesCalendar").datepicker({
  language: "es",
  autoclose: true,
  format: "MM yyyy",
  minViewMode: "months",
  todayHighlight: true,
  weekStart: 0,
});

$("#selMesCalendar").datepicker("setDate", new Date());

//Activar selectores de mes
document.getElementById("mesAtras").addEventListener("click", mesAtras);
document.getElementById("mesAdelante").addEventListener("click", mesAdelante);

//Cambiar el mes del calendario;
$("#selMesCalendar")
  .datepicker()
  .on("changeDate", (e) => {
    activarPlaceholders();
    let date = e.date; //Fecha seleccionada en el datepicker
    let mes = date.getMonth() + 1; //getMonth() retorna 0 para Enero, 1 para Febrero, ...
    let anio = date.getFullYear();
    let url = "/turnos/calendarioTurnos/" + mes + "/" + anio;

    $("#tbody-calendar").load(url);
  });

$("#inputFechaForm").datepicker({
  language: "es",
  format: "dd MM yyyy",
  todayHighlight: true,
  startDate: new Date(),
  autoclose: true,
  daysOfWeekDisabled: [0, 6],
  weekStart: 0,
  orientation: "bottom",
});

//Activar Selectpickers
try{
let select_box_element = document.querySelector("#selectEstudianteForm");

dselect(select_box_element, {
  search: true,
});
}catch(error){}


//Activar tooltips
var tooltipTriggerList = [].slice.call(
  document.querySelectorAll('[data-bs-toggle="tooltip"]')
);
var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
  return new bootstrap.Tooltip(tooltipTriggerEl);
});

//Activar navbar
document.querySelector("#item-turnos").classList.add("active");

//Colocar placeholders en el calendario
function activarPlaceholders() {
  document.querySelector("#tbody-calendar").innerHTML =
    " <tr><td colspan='5' class='p-0'><div class='placeholder-glow' id='placeholders'></div></td></tr>";
  for (let i = 0; i < 25; i++) {
    document.getElementById("placeholders").innerHTML +=
      "<div class='placeholder col calendar-placeholder'></div>";
  }
}

document.querySelector("#selectEstudianteForm").addEventListener("change",() => {
    let selectEstudianteButton = document.querySelector(".dselect-wrapper .form-select");
    selectEstudianteButton.style.borderColor = '#ddd';
})

//GUARDAR TURNO
document.querySelector("#turnosForm").addEventListener("submit", (event) => {
    
    document.querySelector("#mensajeError").style.display = 'none';
    /*Validar estudiante seleccionado*/
    let selectEstudiante = document.querySelector("#selectEstudianteForm");
    let selectEstudianteButton = document.querySelector(".dselect-wrapper .form-select");
    if(selectEstudiante.value == ''){
        selectEstudianteButton.style.borderColor = 'red';
        return;
    }else{
        selectEstudianteButton.style.borderColor = '#198754';
    }

    event.preventDefault();

    //Construir el objeto
    let date = $("#inputFechaForm").data("datepicker").getDate();
    let form = document.forms['turnosForm'];
    turno = {
        idEstudiante: form["selectEstudianteForm"].value,
        fecha: date.getDate() + "-" + (date.getMonth()+1) + "-" + date.getFullYear(),
        idHorario: form["selectHorarioForm"].value,
        idEstacion: form["selectEstacionForm"].value,
        observaciones: form["inputObservacionesForm"].value
    }

    console.log(turno);
    let url="/turnos";
    $.ajax({
        url: url,
        type: "POST",
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify(turno),
        success: function(){
            alert("Guardado");
        },
        error: function(jqXHR){
            console.log("error " + jqXHR.status + " " + jqXHR.responseText);
            document.querySelector("#mensajeError").innerHTML = jqXHR.responseText;
            document.querySelector("#mensajeError").style.display = 'block';
        }
    })

})
$(document).ready(function () {
  $("#selMes").change(function () {
    document.getElementById("selDia").setAttribute("disabled", "disabled");
    let mes = document.getElementById("selMes").value;
    var url = "/actFormTurnosMes/" + mes;
    $("#carta").load(url);
  });

  $("#selDia").change(function () {
    document.getElementById("selHorario").setAttribute("disabled", "disabled");
    let mes = document.getElementById("selMes").value;
    let dia = document.getElementById("selDia").value;
    let idHorario = document.getElementById("selHorario").value;
    var url = "/actFormTurnosDiaMes/" + dia + "/" + mes + "/" + idHorario;
    $("#carta").load(url);
  });

  $("#selHorario").change(function () {
    document.getElementById("selEstacion").setAttribute("disabled", "disabled");
    let mes = document.getElementById("selMes").value;
    let dia = document.getElementById("selDia").value;
    if (dia == 0) {
      document.getElementById("selDia").setAttribute("disabled", "disabled");
    }
    let idHorario = document.getElementById("selHorario").value;

    var url =
      "/actFormTurnosDiaMesHorario/" + dia + "/" + mes + "/" + idHorario;
    $("#carta").load(url);
  });
});

function mesAtras() {
  let nuevaFecha = cambiarMes(false);
  $("#selMesCalendar").datepicker("setDate", new Date(nuevaFecha));
}

function mesAdelante() {
  let nuevaFecha = cambiarMes();
  $("#selMesCalendar").datepicker("setDate", new Date(nuevaFecha));
}

function cambiarMes(adelantar = true) {
  let fecha = document.getElementById("selMesCalendar").value.split(" ");
  let mes = fecha[0];
  let anio = fecha[1];

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
    "Diciembre",
  ];

  for (let i = 0; i < meses.length; i++) {
    if (meses[i] == mes) {
      if (adelantar) {
        if (i == 11) {
          return "01-01-" + ++anio;
        }
        return "" + (++i + 1) + "-01-" + anio;
      } else {
        if (i == 0) {
          return "12-01-" + --anio;
        }
        return "" + i + "-01-" + anio;
      }
    }
  }

  return mes;
}
function verDia(dia) {
  let mes = document.getElementById("selMesCalendar").value;
  var url = "/turnosEstacionesDia/" + dia + "/" + mes;
  $("#divTurnosEstaciones").load(url);

  $("#modalTurnosEstaciones").modal();
}

function eliminarTurno() {
  let id = $("#turnoIdHiddenInput").val();

  $("#deleteModalTurno").modal("hide");
  window.location = "/eliminarTurno/" + id;
}

function abrirVerdia() {
  $("#modalTurnosEstaciones").modal();
}

function abrirConfirm(id) {
  $("#deleteModalTurno").modal();

  $("#turnoIdHiddenInput").val(id);
}
function cerrarConfirm() {
  $("#deleteModalTurno").modal("hide");
}

function abrirNuevoTurno(mes, dia, idHorario, idEstacion) {
  var url =
    "/crearTurnoDefinido/" +
    mes +
    "/" +
    dia +
    "/" +
    idHorario +
    "/" +
    idEstacion;
  $("#carta").load(url);
  $("#modalTurnosEstaciones").modal("hide");
  $("#modalFormTurnos").modal();
}

(function () {
  // Fetch all the forms we want to apply custom Bootstrap validation styles to
  var forms = document.querySelectorAll(".needs-validation");

  // Loop over them and prevent submission
  Array.prototype.slice.call(forms).forEach(function (form) {
    form.addEventListener(
      "submit",
      function (event) {
        if (!form.checkValidity()) {
          event.preventDefault();
          event.stopPropagation();
        }

        form.classList.add("was-validated");
      },
      false
    );
  });
})();

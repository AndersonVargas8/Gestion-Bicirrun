let FECHAS_INICIALES = {
  diasDeshabilitados: [],
  fechasDeshabilitadas: [],
};
let HORARIOS_INICIALES = null;

//Activar navbar
document.querySelector("#item-inicio").classList.add("active");

//Activar Datepickers
$("#selMesCalendar").datepicker({
  language: "es",
  autoclose: true,
  format: "MM yyyy",
  minViewMode: "months",
  todayHighlight: true,
  weekStart: 0,
  disableTouchKeyboard: true,
});

$("#selMesCalendar").datepicker("setDate", new Date());

function setDatepickerFechas(
  diasDeshabilitados = [],
  fechasDeshabilitadas = []
) {
  try {
    $("#inputFechaForm").datepicker("destroy");
  } catch {}

  dias = [0, 6].concat(diasDeshabilitados);

  let opciones = {
    language: "es",
    format: "dd MM yyyy",
    todayHighlight: true,
    startDate: new Date(),
    autoclose: true,
    daysOfWeekDisabled: dias,
    weekStart: 0,
    orientation: "bottom",
    disableTouchKeyboard: true,
    datesDisabled: fechasDeshabilitadas,
  };

  $("#inputFechaForm").datepicker(opciones);

  return opciones;
}

function inicializarFormulario() {
  //Inicializar fechas
  dias = [];
  fechas = [];
  $.ajax({
    url: "/turnos/diasDeshabilitados",
    type: "GET",
    success: (data) => {
      dias = data.diasDeshabilitados;
      fechas = data.fechasDeshabilitadas;
      //Inicializar fechas
      FECHAS_INICIALES.diasDeshabilitados = dias;
      FECHAS_INICIALES.fechasDeshabilitadas = fechas;
      sincronizarFechas(dias, fechas);
    },
    error: () => {
      defaultErrorNotify("");
    },
  });

  //Inicializar horarios
  if (HORARIOS_INICIALES == null) {
    let options = document.querySelector("#selectHorarioForm").options;
    HORARIOS_INICIALES = [];
    for (let i = 1; i < options.length; i++) {
      HORARIOS_INICIALES.push({
        id: options[i].value,
        descripcion: options[i].text,
      });
    }
  }
}

inicializarFormulario();

//Activar selectores de mes
document.getElementById("mesAtras").addEventListener("click", mesAtras);
document.getElementById("mesAdelante").addEventListener("click", mesAdelante);

//Activar Selectpickers
try {
  let select_box_element = document.querySelector("#selectEstudianteForm");

  dselect(select_box_element, {
    search: true,
  });
} catch (error) {}

//Activar tooltips
function activarTooltips(calendar = false) {
  if (calendar) {
    var tooltipTriggerList = [].slice.call(
      document.querySelectorAll('[data-bs-toggle="tooltipCalendar"]')
    );
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
      return new bootstrap.Tooltip(tooltipTriggerEl);
    });
  } else {
    var tooltipTriggerList = [].slice.call(
      document.querySelectorAll('[data-bs-toggle="tooltip"]')
    );
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
      return new bootstrap.Tooltip(tooltipTriggerEl);
    });
  }
}
activarTooltips();
activarTooltips(true);

//Activar validación del form
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

function resetForm() {
  document.querySelector("#mensajeError").style.display = "none";
  let form = document.forms["turnosForm"];
  form.classList.remove("was-validated");
  document.querySelector(".dselect-wrapper .form-select").style.borderColor =
    "#ccc";
  document.querySelector(".dselect-wrapper .form-select").innerHTML =
    "Seleccione";
  try {
    document
      .querySelector(".dselect-items .dropdown-item.active")
      .classList.remove("active");
  } catch {}

  sincronizarFechas();
  sincronizarHorarios();
  document.querySelector("#selectEstacionForm").disabled = true;
  form.reset();
}
//Cambiar el mes del calendario;
$("#selMesCalendar")
  .datepicker()
  .on("changeDate", (e) => {
    activarPlaceholders();
    let date = e.date; //Fecha seleccionada en el datepicker
    let mes = date.getMonth() + 1; //getMonth() retorna 0 para Enero, 1 para Febrero, ...
    let anio = date.getFullYear();
    let url = "/turnos/calendarioTurnos/" + mes + "/" + anio;

    $("#tbody-calendar").load(url, () => {
      activarTooltips(true);
    });
  });

function mesAtras() {
  let nuevaFecha = cambiarMes(false);
  $("#selMesCalendar").datepicker("setDate", nuevaFecha);
}

function mesAdelante() {
  let nuevaFecha = cambiarMes();
  $("#selMesCalendar").datepicker("setDate", nuevaFecha);
}

function cambiarMes(adelantar = true) {
  let date = $("#selMesCalendar").data("datepicker").getDate();

  if (adelantar) date.setMonth(date.getMonth() + 1);
  else date.setMonth(date.getMonth() - 1);

  return date;
}

//Colocar placeholders en el calendario
function activarPlaceholders() {
  document.querySelector("#tbody-calendar").innerHTML =
    " <tr><td colspan='5' class='p-0'><div class='placeholder-glow' id='placeholders'></div></td></tr>";
  for (let i = 0; i < 25; i++) {
    document.getElementById("placeholders").innerHTML +=
      "<div class='placeholder col calendar-placeholder'></div>";
  }
}

//Cambiar color del borde al seleccionar estudiante
document
  .querySelector("#selectEstudianteForm")
  .addEventListener("change", () => {
    let selectEstudianteButton = document.querySelector(
      ".dselect-wrapper .form-select"
    );
    selectEstudianteButton.style.borderColor = "#ccc";
  });

//Sincronizar fechas dado un horario
function sincronizarFechas(horario) {
  //Activar span cargando
  let fecha = document.querySelector("#inputFechaForm");
  let loading = document.querySelector("#loadingFechas");
  let calendar = document.querySelector("#spanCalendar");
  fecha.disabled = true;
  calendar.style.display = "none";
  loading.style.display = "block";

  if (horario == null) {
    //Toma los datos por default (Cargados en la variable local)
    setTimeout(() => {
      //Desactivar span cargando
      fecha.disabled = false;
      loading.style.display = "none";
      calendar.style.display = "block";
      setDatepickerFechas(
        FECHAS_INICIALES.diasDeshabilitados,
        FECHAS_INICIALES.fechasDeshabilitadas
      );
    }, 200);

    return;
  }

  $.ajax({
    url: "/turnos/diasDeshabilitados/" + horario,
    type: "GET",
    success: (data) => {
      setDatepickerFechas(data.diasDeshabilitados, data.fechasDeshabilitadas);

      //Desactivar span cargando
      fecha.disabled = false;
      loading.style.display = "none";
      calendar.style.display = "block";
    },
    error: () => {
      defaultErrorNotify("");
    },
  });
}

//Sincronizar horarios dada una fecha
function sincronizarHorarios(fecha) {
  //Activar span cargando
  let horarios = document.querySelector("#selectHorarioForm");
  let loading = document.querySelector("#loadingHorarios");
  horarios.disabled = true;
  loading.style.display = "block";

  //Eliminar elementos actuales
  let n = horarios.options.length - 1;
  for (let i = 0; i < n; i++) {
    horarios.remove(1);
  }

  if (fecha == null) {
    //Se toman los valores por default (Cargados en las variables globales)
    setTimeout(() => {
      //Agregar los nuevos elementos
      for (let horario of HORARIOS_INICIALES) {
        let option = document.createElement("option");
        option.value = horario.id;
        option.text = horario.descripcion;

        horarios.add(option);
      }
      //Desactivar span cargando
      horarios.disabled = false;
      loading.style.display = "none";
    }, 200);

    return;
  }

  $.ajax({
    url: "/turnos/horariosDisponibles/" + fecha,
    type: "GET",
    success: (data) => {
      for (let horario of data) {
        let option = document.createElement("option");
        option.value = horario.id;
        option.text = horario.descripcion;

        horarios.add(option);
      }

      //Desactivar span cargando
      horarios.disabled = false;
      loading.style.display = "none";
    },
    error: () => {
      defaultErrorNotify("");
    },
  });
}

//Sincronizar estaciones con la fecha y el horario proporcionado
function sincronizarEstaciones(fecha, horario) {
  let estaciones = document.querySelector("#selectEstacionForm");
  let loading = document.querySelector("#loadingEstaciones");

  //Activar span loading
  estaciones.disabled = true;
  loading.style.display = "block";

  //Eliminar elementos actuales
  let n = estaciones.options.length - 1;
  for (let i = 0; i < n; i++) {
    estaciones.remove(1);
  }

  $.ajax({
    url: "/turnos/estacionesDisponibles/" + fecha + "/" + horario,
    type: "GET",
    success: (data) => {
      for (let estacion of data) {
        let option = document.createElement("option");
        option.value = estacion.id;
        option.text = estacion.nombre;

        estaciones.add(option);
      }

      //Desactivar span loading
      estaciones.disabled = false;
      loading.style.display = "none";
    },
    error: () => {
      defaultErrorNotify("");
    },
  });
}

//Selección de fecha para sincronización de horarios y estaciones
$("#inputFechaForm")
  .datepicker()
  .on("changeDate", (e) => {
    let date = e.date; //Fecha seleccionada en el datepicker
    let dia = date.getDate();
    let mes = date.getMonth() + 1; //getMonth() retorna 0 para Enero, 1 para Febrero, ...
    let anio = date.getFullYear();
    let fecha = dia + "-" + mes + "-" + anio;
    let horario = document.querySelector("#selectHorarioForm").value;

    if (horario == "") {
      sincronizarHorarios(fecha);
    } else {
      sincronizarEstaciones(fecha, horario);
    }
  });

//Selección de horario para sincronización de fechas y estaciones
document.querySelector("#selectHorarioForm").addEventListener("change", () => {
  let horario = document.querySelector("#selectHorarioForm").value;
  let fecha = document.querySelector("#inputFechaForm").value;
  
  if (fecha == "") {
    sincronizarFechas(horario);
  } else {
    let date = $("#inputFechaForm").data("datepicker").getDate();
    let fechaFormat = date.getDate() + "-" + (date.getMonth() + 1) + "-" + date.getFullYear();
    sincronizarEstaciones(fechaFormat,horario);
  }
});
//GUARDAR TURNO
document.querySelector("#turnosForm").addEventListener("submit", (event) => {
  document.querySelector("#mensajeError").style.display = "none";
  /*Validar estudiante seleccionado*/
  let selectEstudiante = document.querySelector("#selectEstudianteForm");
  let selectEstudianteButton = document.querySelector(
    ".dselect-wrapper .form-select"
  );
  if (selectEstudiante.value == "") {
    selectEstudianteButton.style.borderColor = "#dc3545";
    return;
  } else {
    selectEstudianteButton.style.borderColor = "#198754";
  }

  event.preventDefault();

  //Construir el objeto
  let date = $("#inputFechaForm").data("datepicker").getDate();
  let form = document.forms["turnosForm"];
  let turno = {
    idEstudiante: form["selectEstudianteForm"].value,
    fecha:
      date.getDate() + "-" + (date.getMonth() + 1) + "-" + date.getFullYear(),
    idHorario: form["selectHorarioForm"].value,
    idEstacion: form["selectEstacionForm"].value,
    observaciones: form["inputObservacionesForm"].value,
  };

  activarSpinner(document.querySelector("#botonGuardar"));

  let url = "/turnos";
  $.ajax({
    url: url,
    type: "POST",
    dataType: "json",
    contentType: "application/json",
    data: JSON.stringify(turno),
    success: function () {
      desactivarSpinner(document.querySelector("#botonGuardar"));
      defaultSuccessNotify("Turno programado");
      inicializarFormulario();
      resetForm();
      $("#modalFormTurnos").modal("hide");
      $("#selMesCalendar").datepicker("setDate", date);
    },
    error: function (jqXHR) {
      desactivarSpinner(document.querySelector("#botonGuardar"));
      if (jqXHR.status == 500) {
        defaultErrorNotify();
        return;
      }
      console.log("error " + jqXHR.status + " " + jqXHR.responseText);
      document.querySelector("#mensajeError").innerHTML = jqXHR.responseText;
      document.querySelector("#mensajeError").style.display = "block";
    },
  });
});

//Redirección a ver dia
function verDia(dia){
  let date = $("#selMesCalendar").data("datepicker").getDate();
  dia = dia.getAttribute('value');
  let mes = date.getMonth() + 1;
  let anio = date.getFullYear();

  window.location.href = "/turnos/dia/"+dia+"-"+mes+"-"+anio;
}

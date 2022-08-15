//Activar navbar
document.querySelector("#item-turnos").classList.add("active");

//Activar datepicker
let fecha = window.location.pathname.split("/");
fecha = fecha[fecha.length - 1];

fecha = fecha.split("-");
fecha = new Date(fecha[1] + "-" + fecha[0] + "-" + fecha[2]);
if (fecha == "Invalid Date") {
  fecha = new Date();
}

let date = new Date();
date.setFullYear(date.getFullYear() + 1);

$("#calendario").datepicker({
  language: "es",
  format: "dd mm yyyy",
  minViewMode: "days",
  todayHighlight: true,
  weekStart: 0,
  disableTouchKeyboard: true,
  daysOfWeekDisabled: [0, 6],
  startDate: new Date("1-1-2022"),
  endDate: date,
});

//Cambiar header dia
function cambiarHeader(date) {
  let meses = [
    "enero",
    "febrero",
    "marzo",
    "abril",
    "mayo",
    "junio",
    "julio",
    "agosto",
    "septiembre",
    "octubre",
    "noviembre",
    "diciembre",
  ];
  let dia = date.getDate();
  let mes = meses[date.getMonth()];
  let anio = date.getFullYear();
  let texto = dia + " de " + mes + " del " + anio;
  document.querySelector("#header-dia").textContent = texto;
}

//Modificar la fecha seleccionada y el header al cargar la página
$("#calendario").datepicker("setDate", fecha);
cambiarHeader(fecha);

function activarPlaceholders() {
  document.querySelector("#contenido").innerHTML =
    "<div class='card-text placeholder-glow p-4' id='placeholder'>" +
    "<span class='placeholder col-7 mb-3 mr-2' style='height: 100px;'></span>" +
    "<span class='placeholder col-4 mb-3' style='height: 100px;'></span>" +
    "<span class='placeholder col-5 mb-3 mr-2' style='height: 100px;'></span>" +
    "<span class='placeholder col-6 mb-3' style='height: 100px;'></span>" +
    "<span class='placeholder col-4 mb-2 mr-2' style='height: 40px;'></span>" +
    "<span class='placeholder col-5 mb-2' style='height: 40px;'></span>" +
    "<span class='placeholder col-6'></span>" +
    "<span class='placeholder col-8'></span>" +
    "<span class='placeholder col-11'></span>" +
    "</div>";
}

$("#calendario")
  .datepicker()
  .on("changeDate", (e) => {
    let date = e.date; //Fecha seleccionada en el datepicker
    let dia = date.getDate();
    let mes = date.getMonth() + 1;
    let anio = date.getFullYear();
    cambiarHeader(date);
    activarPlaceholders();

    let url = "/turnos/diaEstaciones/" + dia + "-" + mes + "-" + anio;
    $("#contenido").load(url);
  });

//Activar selectores de mes
document.getElementById("diaAtras").addEventListener("click", diaAtras);
document.getElementById("diaAdelante").addEventListener("click", diaAdelante);

function diaAtras() {
  let nuevaFecha = cambiarMes(false);
  $("#calendario").datepicker("setDate", nuevaFecha);
}

function diaAdelante() {
  let nuevaFecha = cambiarMes();
  $("#calendario").datepicker("setDate", nuevaFecha);
}

function cambiarMes(adelantar = true) {
  let date = $("#calendario").data("datepicker").getDate();

  let aumento = date.getUTCDay() == 5 ? 3 : 1;
  let decremento = date.getUTCDay() == 1 ? 3 : 1;
  if (adelantar) date.setDate(date.getDate() + aumento);
  else date.setDate(date.getDate() - decremento);

  return date;
}

function cambiarObservacion(id, textarea) {
  toastr.info("Actualizando turno");
  let turno = {
    observaciones: textarea.value,
  };

  $.ajax({
    url: "/turnos/" + id,
    type: "PATCH",
    dataType: "json",
    contentType: "application/json",
    data: JSON.stringify(turno),
    success: function () {
      defaultSuccessNotify("Turno actualizado");
    },
    error: function (jqXHR) {
      defaultErrorNotify();
      console.log("error " + jqXHR.status + " " + jqXHR.responseText);
    },
  });
}

function cambiarEstado(id, idEstado) {
  $("#seleccionarEstado").modal("show");
  let modalBody = document.querySelector(
    "#seleccionarEstado #modalBodyEstados"
  );

  let estados = [
    {
      id: 1,
      descripcion: "Programado",
    },
    {
      id: 2,
      descripcion: "Cumplido",
    },
    {
      id: 3,
      descripcion: "Incumplido",
    },
  ];

  let contenido = "";
  for (let estado of estados) {
    contenido += "<div class='form-check'>";

    if (estado.id == idEstado) {
      contenido +=
        "<input class='form-check-input' type='radio' name='inputEstado' id='flexRadioDefault" +
        estado.id +
        "' value='" +
        estado.id +
        "' checked>";
    } else {
      contenido +=
        "<input class='form-check-input' type='radio' name='inputEstado' id='flexRadioDefault" +
        estado.id +
        "' value='" +
        estado.id +
        "'>";
    }

    contenido +=
      "<label class='form-check-label col-lg-5' for='flexRadioDefault" +
      estado.id +
      "'>";

    if (estado.id == 3) {
      contenido +=
        "<div class='alert alert-danger pt-2 pb-2'>" +
        estado.descripcion +
        "</div>";
    } else if (estado.id == 2) {
      contenido +=
        "<div class='alert alert-success pt-2 pb-2'>" +
        estado.descripcion +
        "</div>";
    } else {
      contenido +=
        "<div class='alert alert-info pt-2 pb-2'>" +
        estado.descripcion +
        "</div>";
    }

    contenido += "</label></div>";
  }

  modalBody.innerHTML = contenido;
  let botonGuardar = document.querySelector("#seleccionarEstado #guardar");
  botonGuardar.addEventListener("click", () => {
    let valor = document.querySelector("[name = 'inputEstado']:checked").value;
    if (valor != idEstado) {
      activarSpinner(botonGuardar);

      let date = $("#calendario").data("datepicker").getDate();

      let turno = {
        idEstado: valor,
      };

      $.ajax({
        url: "/turnos/" + id,
        type: "PATCH",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(turno),
        success: function () {
          botonGuardar.replaceWith(botonGuardar.cloneNode(true));
          desactivarSpinner(
            document.querySelector("#seleccionarEstado #guardar")
          );
          $("#seleccionarEstado").modal("hide");
          defaultSuccessNotify("Turno actualizado");
          $("#calendario").datepicker("setDate", date);
        },
        error: function (jqXHR) {
          desactivarSpinner(botonGuardar);
          defaultErrorNotify();
          console.log("error " + jqXHR.status + " " + jqXHR.responseText);
        },
      });
    } else {
      toastr.info("No hay cambios para guardar");
      $("#seleccionarEstado").modal("hide");
      valor = idEstado;
      botonGuardar.replaceWith(botonGuardar.cloneNode(true));
    }
  });
}

function eliminarTurno(id) {
  confirmarEliminación("Se eliminará el turno definitivamente", (confirm) => {
    if (confirm) {
      let date = $("#calendario").data("datepicker").getDate();
      let boton = document.querySelector("#confirmarEliminacion #eliminar");
      activarSpinner(boton);
      $.ajax({
        url: "/turnos/" + id,
        type: "DELETE",
        success: function () {
          desactivarSpinner(boton);
          $("#confirmarEliminacion").modal("hide");
          defaultSuccessNotify("Turno eliminado");
          $("#calendario").datepicker("setDate", date);
        },
        error: function (jqXHR) {
          desactivarSpinner(boton);
          $("#confirmarEliminacion").modal("hide");
          defaultErrorNotify();
          console.log("error " + jqXHR.status + " " + jqXHR.responseText);
        },
      });
    }
  });
}

/******************************** */
/************FORMULARIO************/
/******************************** */
let FECHAS_INICIALES = {
  diasDeshabilitados: [],
  fechasDeshabilitadas: [],
};
let HORARIOS_INICIALES = null;
let ESTACIONES_INICIALES = null;
let SELECCION_ESPECIFICA = false;
let EDITANDO = false;
let ID_EDITANDO = 0;

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
  //Inicializar estaciones
  if (ESTACIONES_INICIALES == null) {
    let options = document.querySelector("#selectEstacionForm").options;
    ESTACIONES_INICIALES = [];
    for (let i = 1; i < options.length; i++) {
      ESTACIONES_INICIALES.push({
        id: options[i].value,
        nombre: options[i].text,
      });
    }
  }
}

inicializarFormulario();

//Activar Selectpickers
try {
  let select_box_element = document.querySelector("#selectEstudianteForm");

  dselect(select_box_element, {
    search: true,
  });
} catch (error) {}

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
  EDITANDO = false;
  SELECCION_ESPECIFICA = false;
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
  sincronizarEstaciones();

  document.querySelector("#botonLimpiar").style.display = "block";
  form.reset();
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

    //Desactivar span cargando
    fecha.disabled = false;
    loading.style.display = "none";
    calendar.style.display = "block";
    setDatepickerFechas(
      FECHAS_INICIALES.diasDeshabilitados,
      FECHAS_INICIALES.fechasDeshabilitadas
    );

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
  if (fecha == null && horario == null) {
    //Se toman los valores por default (Cargados en las variables globales)
    //Agregar los nuevos elementos
    for (let estacion of ESTACIONES_INICIALES) {
      let option = document.createElement("option");
      option.value = estacion.id;
      option.text = estacion.nombre;

      estaciones.add(option);
    }
    //Desactivar span cargando
    estaciones.disabled = true;
    loading.style.display = "none";

    return;
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
    if (SELECCION_ESPECIFICA) {
      return;
    }
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
  if (SELECCION_ESPECIFICA) {
    return;
  }
  let horario = document.querySelector("#selectHorarioForm").value;
  let fecha = document.querySelector("#inputFechaForm").value;

  if (fecha == "") {
    sincronizarFechas(horario);
  } else {
    let date = $("#inputFechaForm").data("datepicker").getDate();
    let fechaFormat =
      date.getDate() + "-" + (date.getMonth() + 1) + "-" + date.getFullYear();
    sincronizarEstaciones(fechaFormat, horario);
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
  let type = "POST";
  if (EDITANDO) {
    url = "/turnos/" + ID_EDITANDO;
    type = "PUT";
  }
  $.ajax({
    url: url,
    type: type,
    dataType: "json",
    contentType: "application/json",
    data: JSON.stringify(turno),
    success: function () {
      desactivarSpinner(document.querySelector("#botonGuardar"));
      defaultSuccessNotify("Turno programado");
      inicializarFormulario();
      resetForm();
      $("#modalFormTurnos").modal("hide");
      $("#calendario").datepicker("setDate", date);
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

function crearTurno(idEstacion, idHorario) {
  resetForm();
  SELECCION_ESPECIFICA = true;
  //Establecer la fecha escogida
  let date = $("#calendario").data("datepicker").getDate();
  $("#inputFechaForm").datepicker("setDate", date);
  let horarios = document.querySelector("#selectHorarioForm");
  let estaciones = document.querySelector("#selectEstacionForm");

  //Establecer horario escogido
  document.querySelector(
    "#selectHorarioForm option[value='" + idHorario + "']"
  ).selected = true;

  //Establecer estacion escogida
  document.querySelector(
    "#selectEstacionForm option[value='" + idEstacion + "']"
  ).selected = true;

  //Deshabilitar botones
  document.querySelector("#inputFechaForm").disabled = true;
  horarios.disabled = true;
  estaciones.disabled = true;
  document.querySelector("#botonLimpiar").style.display = "none";

  $("#modalFormTurnos").modal("show");
}

function editarTurno(turno) {
  let idHorario = turno.idHorario;
  let idEstacion = turno.idEstacion;
  resetForm();
  SELECCION_ESPECIFICA = true;
  EDITANDO = true;
  ID_EDITANDO = turno.id;

  //Estableer la fecha escogida
  let date = $("#calendario").data("datepicker").getDate();
  $("#inputFechaForm").datepicker("setDate", date);
  let horarios = document.querySelector("#selectHorarioForm");
  let estaciones = document.querySelector("#selectEstacionForm");

  //Establecer horario escogido
  document.querySelector(
    "#selectHorarioForm option[value='" + idHorario + "']"
  ).selected = true;

  //Establecer estacion escogida

  document.querySelector(
    "#selectEstacionForm option[value='" + idEstacion + "']"
  ).selected = true;
  estaciones.disabled = false;

  //Establecer estudiante
  let option = document.querySelector(
    "#selectEstudianteForm option[value='" + turno.idEstudiante + "']"
  );
  option.selected = true;
  document.querySelector(".dropdown.dselect-wrapper button").innerHTML =
    option.text;

  $("#modalFormTurnos").modal("show");
}

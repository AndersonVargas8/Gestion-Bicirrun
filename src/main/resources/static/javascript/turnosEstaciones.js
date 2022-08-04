//Activar navbar
document.querySelector("#item-turnos").classList.add("active");

//Activar datepicker
let fecha = window.location.pathname.split('/');
fecha = fecha[fecha.length-1];

fecha = fecha.split("-");
fecha = new Date(fecha[1]+"-"+fecha[0]+"-"+fecha[2]);
if(fecha == "Invalid Date"){
  fecha = new Date();
}

let date = new Date();
date.setFullYear(date.getFullYear() +1);

$("#calendario").datepicker({
    language: "es",
    format: "dd mm yyyy",
    minViewMode: "days",
    todayHighlight: true,
    weekStart: 0,
    disableTouchKeyboard: true,
    daysOfWeekDisabled: [0,6],
    startDate: new Date("1-1-2022"),
    endDate: date
  });

  //Cambiar header dia
  function cambiarHeader(date){
    let meses = ["enero", "febrero", "marzo", "abril", "mayo", "junio", "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"];
    let dia = date.getDate();
    let mes = meses[date.getMonth()];
    let anio = date.getFullYear();
    let texto = dia + " de " + mes + " del " + anio;
    document.querySelector("#header-dia").textContent = texto;

  }

  //Modificar la fecha seleccionada y el header al cargar la página
  $("#calendario").datepicker("setDate",fecha);
  cambiarHeader(fecha);

  function activarPlaceholders(){
    document.querySelector("#contenido").innerHTML = "<div class='card-text placeholder-glow p-4' id='placeholder'>"
    +"<span class='placeholder col-7 mb-3 mr-2' style='height: 100px;'></span>"
    +"<span class='placeholder col-4 mb-3' style='height: 100px;'></span>"
    +"<span class='placeholder col-5 mb-3 mr-2' style='height: 100px;'></span>"
    +"<span class='placeholder col-6 mb-3' style='height: 100px;'></span>"
    +"<span class='placeholder col-4 mb-2 mr-2' style='height: 40px;'></span>"
    +"<span class='placeholder col-5 mb-2' style='height: 40px;'></span>"
    +"<span class='placeholder col-6'></span>"
    +"<span class='placeholder col-8'></span>"
    +"<span class='placeholder col-11'></span>"
    +"</div>";
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
  })


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
  
  
  let aumento = (date.getUTCDay() == 5) ? 3 : 1;
  let decremento = (date.getUTCDay() == 1) ? 3 : 1;
  if (adelantar) date.setDate(date.getDate() + aumento);
  else date.setDate(date.getDate() - decremento);

  return date;
}

function cambiarObservacion(id,textarea){
  toastr.info("Actualizando turno");
  let turno = {
    observaciones: textarea.value
  }

  $.ajax({
    url: "/turnos/"+id,
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

function cambiarEstado(id, idEstado){
  $("#seleccionarEstado").modal('show');
  let modalBody = document.querySelector("#seleccionarEstado #modalBodyEstados");
  
  let estados = [
      {
        id: 1,
        descripcion: "Programado"
      },
      {
        id: 2,
        descripcion: "Cumplido"
      },
      {
        id: 3,
        descripcion: "Incumplido"
      }
    ]
  
  let contenido = "";
  for(let estado of estados){
    contenido += "<div class='form-check'>";

    if(estado.id == idEstado){
      contenido += "<input class='form-check-input' type='radio' name='inputEstado' id='flexRadioDefault"+estado.id+"' value='"+estado.id+"' checked>";
    }else{
      contenido += "<input class='form-check-input' type='radio' name='inputEstado' id='flexRadioDefault"+estado.id+"' value='"+estado.id+"'>";
    }

    contenido += "<label class='form-check-label col-lg-5' for='flexRadioDefault"+estado.id+"'>";

    if(estado.id == 3){
      contenido += "<div class='alert alert-danger pt-2 pb-2'>"+estado.descripcion+"</div>";
    }else if(estado.id == 2){
      contenido += "<div class='alert alert-success pt-2 pb-2'>"+estado.descripcion+"</div>";
    }else{
      contenido += "<div class='alert alert-info pt-2 pb-2'>"+estado.descripcion+"</div>";
    }

    contenido += "</label></div>"
  }

  modalBody.innerHTML = contenido;
  let botonGuardar = document.querySelector("#seleccionarEstado #guardar");
  botonGuardar.addEventListener('click',() => {
    let valor = document.querySelector("[name = 'inputEstado']:checked").value;
    if(valor != idEstado){
      activarSpinner(botonGuardar);

      let date = $("#calendario").data("datepicker").getDate();

      let turno = {
        idEstado : valor
      };
      
      $.ajax({
        url: "/turnos/"+id,
        type: "PATCH",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(turno),
        success: function () {
          botonGuardar.replaceWith(botonGuardar.cloneNode(true));
          desactivarSpinner(document.querySelector("#seleccionarEstado #guardar"));
          $("#seleccionarEstado").modal('hide');
          defaultSuccessNotify("Turno actualizado");
          $("#calendario").datepicker("setDate", date);
        },
        error: function (jqXHR) {
          desactivarSpinner(botonGuardar);
          defaultErrorNotify();
          console.log("error " + jqXHR.status + " " + jqXHR.responseText);
        }
      });
    }else{
      toastr.info("No hay cambios para guardar");
      $("#seleccionarEstado").modal('hide');
      valor = idEstado;
      botonGuardar.replaceWith(botonGuardar.cloneNode(true));
    }
  })  
}

function eliminarTurno(id){
  confirmarEliminación("Se eliminará el turno definitivamente",(confirm) => {
    if(confirm){
      let date = $("#calendario").data("datepicker").getDate();
      let boton = document.querySelector("#confirmarEliminacion #eliminar")
      activarSpinner(boton);
      $.ajax({
        url: "/turnos/"+id,
        type: "DELETE",
        success: function () {
          desactivarSpinner(boton);
          $("#confirmarEliminacion").modal('hide');
          defaultSuccessNotify("Turno eliminado");
          $("#calendario").datepicker("setDate", date);
        },
        error: function (jqXHR) {
          desactivarSpinner(boton);
          $("#confirmarEliminacion").modal('hide');
          defaultErrorNotify();
          console.log("error " + jqXHR.status + " " + jqXHR.responseText);
        }
      });
    }
  })
}



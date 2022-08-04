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
  $("#calendario")
  .datepicker()
  .on("changeDate", (e) => {
    let meses = ["enero", "febrero", "marzo", "abril", "mayo", "junio", "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"];
    let date = e.date; //Fecha seleccionada en el datepicker
    let dia = date.getDate();
    let mes = meses[date.getMonth()];
    let anio = date.getFullYear();
    let texto = dia + " de " + mes + " del " + anio;
    document.querySelector("#header-dia").textContent = texto;
  })

  
$("#calendario").datepicker("setDate",fecha);

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
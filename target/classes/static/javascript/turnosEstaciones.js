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
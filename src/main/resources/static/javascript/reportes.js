//Activar navbar
document.querySelector("#item-reportes").classList.add("active");

//Sidebar
window.addEventListener('DOMContentLoaded', event => {
    // Toggle the side navigation
    const sidebarToggle = document.body.querySelector('#sidebarToggle');
    if (sidebarToggle) {
        // Uncomment Below to persist sidebar toggle between refreshes
        // if (localStorage.getItem('sb|sidebar-toggle') === 'true') {
        //     document.body.classList.toggle('sb-sidenav-toggled');
        // }
        sidebarToggle.addEventListener('click', event => {
            event.preventDefault();
            document.body.classList.toggle('sb-sidenav-toggled');
            localStorage.setItem('sb|sidebar-toggle', document.body.classList.contains('sb-sidenav-toggled'));
        });
    }

});

//data Table
function crearTabla() {
    let table = new DataTable('#reports-table', {
        "order": [[3, "desc"]],
        language: {
            "url": "//cdn.datatables.net/plug-ins/1.10.16/i18n/Spanish.json"
        }
    });

    document.querySelector("#reports-table-card").hidden = false;
    return table
};
crearTabla();

//Activar Datepickers
$("#selInitDate").datepicker({
    language: "es",
    autoclose: true,
    format: "dd MM yyyy",
    todayHighlight: true,
    weekStart: 0,
    disableTouchKeyboard: true,
});

$("#selFinalDate").datepicker({
    language: "es",
    autoclose: true,
    format: "dd MM yyyy",
    todayHighlight: true,
    weekStart: 0,
    disableTouchKeyboard: true,
});

//Activar placeholders
function activarPlaceholders() {
    let card = document.querySelector("#reports-table-card");

    let content = "<div class='card-text placeholder-glow p-4' id='placeholder'>";
    for (let i = 0; i < 3; i++) {
        content += "<span class='placeholder col-12 mb-2' style='height: 40px;'></span>"
        content += "<span class='placeholder col-11 mb-2' style='height: 40px;'></span>"
        content += "<span class='placeholder col-10 mb-2' style='height: 40px;'></span>"

    }
    content += "</div>"

    //Ocultar tabla
    document.querySelector("#contenido").hidden = true;
    card.innerHTML += content;
}

function desactivarPlaceholder(showTable = true) {
    document.querySelector("#reports-table-card").querySelector("#placeholder").remove();

    if (showTable)
        document.querySelector("#contenido").hidden = false;
}

//Aplicar filtros
document.querySelector('#aplicar-filtros').addEventListener('click', () => {
    let initDate = $("#selInitDate").data("datepicker").getDate();
    let finalDate = $("#selFinalDate").data("datepicker").getDate();

    let initDateValue = document.querySelector("#selInitDate").value;
    let finalDateValue = document.querySelector("#selFinalDate").value;
    if (initDateValue != '' || finalDateValue != '') {
        let tituloReporte = "Reporte"
        if (initDateValue != '')
            tituloReporte += ` desde <strong>${initDateValue}</strong>`
        if (finalDateValue != '')
            tituloReporte += ` hasta <strong>${finalDateValue}</strong>`

        document.querySelector("#report-title").innerHTML = tituloReporte;
    }else{
        document.querySelector("#report-title").innerHTML = "Reporte de todo el tiempo";
    }

    initDate = (initDate == null) ? new Date('2001-1-1') : initDate;
    finalDate = (finalDate == null) ? new Date('2100-1-1') : finalDate;
    verTurnosCumplidos = document.querySelector("#turnosCumplidos").checked;
    verTurnosIncumplidos = document.querySelector("#turnosIncumplidos").checked;

    if (finalDate < initDate) {
        let errorMessage = document.querySelector('#errorMessage');
        errorMessage.innerHTML = `
        <label class='alert alert-danger w-100 text-center p-2 alert-dismissible fade show' role='alert'>
            La fecha inicial no puede ser posterior a la fecha final
        </label>`;
        setTimeout(() => { $('.alert').alert('close') }, 4000);
        return;
    }

    //Desactivar datatable
    try {
        $('#reports-table').DataTable().destroy();
    } catch (e) {
        console.error(e);
    }
    activarPlaceholders();


    let url = "/reportes/programados";

    if (verTurnosCumplidos)
        url += "/cumplidos";
    if (verTurnosIncumplidos)
        url += "/incumplidos";

    url += `/${formatearFecha(initDate)}/${formatearFecha(finalDate)}`;

    fetch(url)
        .then(async res => {
            if (!res.ok)
                return Promise.reject(error);

            const data = await res.json();
            actualizarTabla(data);
        })
        .catch(error => {
            console.log(error);
            defaultErrorNotify();
            desactivarPlaceholder(false);
        });
})

//Formatear fecha a "yyyy-mm-dd"
function formatearFecha(date) {
    let day = date.getDate();

    if (day < 10)
        day = `0${day}`;

    let month = date.getMonth() + 1;

    if (month < 10)
        month = `0${month}`;

    let year = date.getFullYear();

    return `${year}-${month}-${day}`;
}
//Actualizar tabla
function actualizarTabla(reportes) {

    let table = document.querySelector("#reports-table");
    let thead = table.querySelector("thead");
    let tbody = table.querySelector("tbody");
    thead.innerHTML = "";
    tbody.innerHTML = "";

    if(reportes.length == 1 && reportes[0].apellidos == "FESTIVO") //VERIFICA SI EL REPORTE ES DEL ESTUDIANTE "FESTIVO"
        reportes = [];

    if (reportes.length > 0) {
        let object = reportes[0];
        let titulos = ["Documento", "Nombres", "Apellidos"];

        if (object.turnosProgramados != null) {
            titulos.push("Turnos programados")
            titulos.push("Horas programadas")
        }

        if (object.turnosCumplidos != null) {
            titulos.push("Turnos cumplidos")
            titulos.push("Horas cumplidas")
        }

        if (object.turnosIncumplidos != null) {
            titulos.push("Turnos incumplidos")
            titulos.push("Horas incumplidas")
        }

        //Cargar thead
        let theadContent = ""
        for (let titulo of titulos) {
            theadContent += `<th> ${titulo} </th>`
        }
        thead.innerHTML = theadContent;

        //Cargar tbody  
        let reporteContent = "";
        for (let reporte of reportes) {
            if(reporte.apellidos == "FESTIVO")
                continue;
            reporteContent += `<tr>
                <td>${reporte.documento}</td>
                <td>${reporte.nombres}</td>
                <td>${reporte.apellidos}</td>
                <td>${reporte.turnosProgramados}</td>
                <td>${reporte.horasProgramadas}</td>`

            if (titulos.includes('Turnos cumplidos'))
                reporteContent += `<td>${reporte.turnosCumplidos}</td>`
            if (titulos.includes('Horas cumplidas'))
                reporteContent += `<td>${reporte.horasCumplidas}</td>`
            if (titulos.includes('Turnos incumplidos'))
                reporteContent += `<td>${reporte.turnosIncumplidos}</td>`
            if (titulos.includes('Horas incumplidas'))
                reporteContent += `<td>${reporte.horasIncumplidas}</td>`


            reporteContent += `</tr>`
        }
        tbody.innerHTML = reporteContent;
        crearTabla();
    } else {
        table.querySelector("tbody").innerHTML = "<tr><td style='background-color:lightgrey !important'>No se encontraron resultados</td></tr>";
    }

    desactivarPlaceholder();
}

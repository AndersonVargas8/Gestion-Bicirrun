//Activar navbar
document.querySelector("#item-settings").classList.add("active");

//Habilitar botón de guardar
function cambiarEstadoEstacion(){
	document.querySelector("#btn-guardarEstaciones").disabled = false;
}

//Guardar estado estaciones
document.querySelector("#btn-guardarEstaciones").addEventListener("click", () => {
	let boton = document.querySelector("#btn-guardarEstaciones");
	activarSpinner(boton);
	const inputs = document.getElementsByClassName("form-check-input");

	estaciones = [];

	for(let input of inputs){
		estaciones.push({
			id : input.value,
			is_habilitada : input.checked
		})
	}

	fetch('/settings/estacionesHabilitadas', {
		headers: {
			'Content-Type': 'application/json'
		},
		method: 'POST',
		body: JSON.stringify(estaciones)
	})
	.then(async res => {
		defaultSuccessNotify("Estaciones actualizadas");
		desactivarSpinner(boton);
		document.querySelector("#btn-guardarEstaciones").disabled = true;
		
	})
	.catch(error =>{
		console.log(error);
        defaultErrorNotify();
		desactivarSpinner(boton);
		document.querySelector("#btn-guardarEstaciones").disabled = true;
	})
})
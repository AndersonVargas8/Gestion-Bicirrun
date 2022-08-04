function activarSpinner(element){
    element.innerHTML = "<i class='fa-solid fa-circle-notch fa-spin mr-0'></i>";
    element.disabled = true;
}

function desactivarSpinner(element){
    element.innerHTML = element.value;
    element.disabled = false;
}

function defaultErrorNotify(mensaje = "Inténtalo nuevamente"){
    toastr.options.positionClass = 'toast-top-right';
    toastr.error(mensaje, 'Ocurrió un error');
}

function defaultSuccessNotify(mensaje = "Correcto!!!"){
    toastr.options.positionClass = 'toast-top-right';
    toastr.success(mensaje);
}
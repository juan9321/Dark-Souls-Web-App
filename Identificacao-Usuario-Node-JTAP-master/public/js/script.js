function checkError() {
    fetch('/getError')
        .then(response => response.json())
        .then(data => {
            if (data.error) {
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: data.error,
                });
            }
        })
        .catch(error => console.error('Error:', error));
}



function checkSuccess() {
    fetch('/getSuccess')
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                Swal.fire({
                    icon: 'success',
                    title: 'Sucesso!',
                    text: data.success,
                });
            }
        })
        .catch(error => console.error('Error:', error));
}



function checkMessages(){
    checkError();
    checkSuccess();
}



function logout(){
    Swal.fire({
        title: 'Deseja Realmente Sair?',
        showDenyButton: true,
        confirmButtonText: `Sim`,
        denyButtonText: `Não`,
        icon: 'info'
      }).then((result) => {
        if (result.isConfirmed) {
            fetch('/api/logout', {
                method: 'POST',
            })
            .then(response => {
                if (response.redirected) {
                    window.location.href = response.url;
                }
            })
            .catch(error => console.error('Error: ', error));
        }
      })

}



window.onload = checkMessages;
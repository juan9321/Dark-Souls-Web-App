document.addEventListener('DOMContentLoaded', function() {
    const selectImage = document.getElementById('imagem');
    const image = document.getElementById('build-imagem');

    selectImage.addEventListener('change', function() {
        const selectedValue = selectImage.value;

        image.src = '/public/img/builds/' + selectedValue;
        image.alt = selectedValue;
    });
});
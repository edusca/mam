$(document).ready(function() {
    var txtAddToMultipleSelect = $('#txt-add-values-for-search');
    
    $('#add-to-multiple-select').click(function(){
        if(txtAddToMultipleSelect.val() !== "") {
            $("#search-values").append($('<option>', {
                value: txtAddToMultipleSelect.val(),
                text: txtAddToMultipleSelect.val()
            }));
        }
    });
});



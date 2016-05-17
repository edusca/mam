$(document).ready(function() {
    var queryInput = $('#query-input');
    var queryBtn = $('#query-btn');
    
    queryBtn.click(function() {
        $.ajax({
            url : '/mam/twitter/save-tweets-by-words',
            type : 'POST',
            data : {query : queryInput.val()},
            success : function() {
                alert('Termino el proceso de guardado de los tweets');
            }
        });
    });
});


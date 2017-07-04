$(document).ready(function() {
    $('.js-current-cluster').on('click', function() {
        $.ajax({
            url: '/mam/clustering/kmeans/word_cloud',
            method: 'GET',
            data: {
                cluster_numbers: $('#current-cluster-numbers').val(),
                iterations: $('#current-cluster-iterations').val(),
                seed: $('#seed').val(),
                current_cluster: $(this).find('input[type=hidden]:first').val()
            },
            success: function (data) {
                $('#word-cloud-image').attr(
                    'src',
                    '/mam/word_cloud_images/' + data.value
                );
            },
            error: function (error) {
                console.log(error);
            }
        });
    });
});



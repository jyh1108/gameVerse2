$(document).ready(function() {
    $('tbody.post_hot').each(function() {
        $(this).find('tr:lt(2)').each(function() {
            $(this).addClass('hot-post');
        });
    });
});

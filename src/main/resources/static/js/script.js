$(function () {
  $('body')
    .on('click', '.delete-book', function (e) {
      e.preventDefault();
      let $button = $(this);
      let url = $button.data('url');
      let redirectUrl = $button.data('redirect_url');
      apiDelete(url)
        .then(function () {
          location.href = redirectUrl
        })
        .catch(function (error) {
          console.log(error)
        })
    })
    .on('change', 'input[name="search"]', function (e) {
      $(this).closest('form').trigger('submit');
    });

  function apiDelete(url) {
    return fetch(url, {
      method: 'delete'
    })
  }
});

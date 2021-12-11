import { init } from './components/pages.js';

(function () {
	$.ajaxSetup({
		dataType: "json",
		beforeSend: function (xhr, options) {
			options.url = "https://y1ix8qw498.execute-api.us-east-1.amazonaws.com/prod" + options.url;
		}
	});
	$(function () {
		window.feather.replace();
		init();
	});
})();

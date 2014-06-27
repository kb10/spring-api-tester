var angular = angular;

var apitester = angular.module('fgApitester', ['restangular', 'ui.bootstrap']).config(
	function(RestangularProvider) {
		RestangularProvider.setBaseUrl('api');
	}
);




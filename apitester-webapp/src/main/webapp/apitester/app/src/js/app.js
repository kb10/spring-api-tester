var angular = angular;

var apitester = angular.module('fgApitester', ['restangular']).config(
	function(RestangularProvider) {
		RestangularProvider.setBaseUrl('api');
	}
);



var angular = angular;

var apitester = angular.module('fgApitester', 
		['restangular', 'ui.bootstrap', 'ngRoute'])
	.config(function(RestangularProvider, $routeProvider) {
		$routeProvider
			.when('/api/browse',{
				templateUrl: 'app/src/templates/apibrowser.html'
			})
			.when('/', {
				templateUrl: 'app/src/templates/tester.html'
			})
			.otherwise({
				redirectTo: '/'
			})
		;
		
		RestangularProvider.setBaseUrl('api');
	})
;
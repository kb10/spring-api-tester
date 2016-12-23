var angular = angular;

var apitester = angular.module('fgApitester', ['restangular', 'ui.bootstrap', 'ngRoute', 'ui.select2'])
	.constant('apitesterDBConf', {
		dbname : 'apitester',
		version : 2,
		historyStore : 'history',
	})	
	.config(function(apitesterDBConf, RestangularProvider, $routeProvider) {
		$routeProvider
			.when('/api/browse',{
				templateUrl: 'app/src/templates/apibrowser.html'
			})
			.when('/api/objects',{
				templateUrl: 'app/src/templates/objects.html'
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
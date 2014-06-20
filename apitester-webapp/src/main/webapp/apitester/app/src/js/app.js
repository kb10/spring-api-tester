var angular = angular;
var apitester = angular.module('fgApitester', ['restangular']).config(
	function(RestangularProvider) {
		RestangularProvider.setBaseUrl('api');
	}
);



apitester.controller('rootController', [ '$scope' , 'Restangular', function($scope, RA) {

	RA.all('basepaths').getList().then(
		function(basepaths) {
			$scope.basepaths = basepaths;
			console.log($scope.basepaths);
		}
	);


}]);
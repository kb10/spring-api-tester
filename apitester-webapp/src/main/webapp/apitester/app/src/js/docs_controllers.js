apitester.controller('docsRootController', [ '$scope' , '$http', 'Restangular', function($scope, $http, RA) {

	$scope.config = {};
	$scope.config.basePath = "";
	$scope.config.methods = [];
	$scope.config.searchTerm = "";

	RA.all('basepaths').getList().then(
		function(basepaths) {
			$scope.basePaths = basepaths;
		}
	);

	$scope.updateInternal = _.debounce(function() {	
		console.log("executing ... ");
		if($scope.config.basePath == "") {
			$scope.config.basePath = null;
		}
		console.log($scope.config);

		RA.all('calls').getList($scope.config).then(
			function(calls) {
				$scope.apicalls = calls;
			}
		);
	},400);

	$scope.update = function() {
		console.log("fired ... ");
		$scope.updateInternal();
	};

	$scope.update();

}]);
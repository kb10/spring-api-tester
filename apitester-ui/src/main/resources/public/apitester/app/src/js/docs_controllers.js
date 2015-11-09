apitester.controller('docsRootController', [ '$scope' , '$http', 'Restangular', function($scope, $http, RA) {

	$scope.config = {};
	
	RA.all('basepaths').getList().then(
		function(basepaths) {
			$scope.basePaths = basepaths;
		}
	);

	$scope.update = _.debounce(function() {	
		$scope.selectedDocIndex = {};
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

	$scope.update();

	$scope.selectedDocIndex = {};
	$scope.selectCall = function(i) {
		$scope.selectedDocIndex = i;
	}

	$scope.isDocSelected = function(i) {
		if(i == $scope.selectedDocIndex) {
			return true;
		}
		return false;		
	}

}]);

apitester.controller('rootController', [ '$scope' , 'Restangular', function($scope, RA) {

	$scope.requestconfig = {};
	$scope.selectedCallInfo = {};
	$scope.showRequestButton = {};

	RA.all('basepaths').getList().then(
		function(basepaths) {
			$scope.basepaths = basepaths;
		}
	);

	$scope.updateFullpathOptions = function() {
		RA.all('calls').getList({basePath:$scope.requestconfig.basepath}).then(
			function(calls) {
				$scope.calls = calls;
				$scope.fullPaths = _(calls).pluck('fullPath').uniq().value();
				$scope.selectedCallInfo = {};
				$scope.showRequestButton.flag = false;
				$scope.selectedCallIndex = -1;
			}
		);
	}

	$scope.selectFullPath = function() {
		$scope.showRequestButton.flag = true;
	}

	$scope.methods = ['OPTIONS', 'GET', 'POST', 'PUT', 'DELETE'];
	$scope.buttonClasses = { OPTIONS:'btn btn-default', GET:'btn btn-default', POST:'btn btn-default', PUT:'btn btn-default', DELETE:'btn btn-default'};

	$scope.getCall = function(method, fullPath) {
		var call = _.find($scope.calls, {fullPath:fullPath, method:method});
		if (call) {
			$scope.buttonClasses[method] = 'btn btn-primary btn-lg';
		}
		else {
			$scope.buttonClasses[method] = 'btn btn-default';	
		}
		return call;
	}

	//$scope.buttonClasses = { disabled:'btn btn-default', big:'btn btn-primary btn-lg', active:'btn btn-success btn-lg active'};

	// $scope.getButtonClass = function(method, fullPath) {
	// 	if(_.find($scope.calls, {fullPath:fullPath, method:method})) {
	// 		return "btn btn-primary btn-lg";
	// 	}
	// 	return "btn btn-default";
	// }

	$scope.selectRequest = function(method, fullPath) {
		$scope.selectedCallInfo = _.find($scope.calls, {fullPath:fullPath, method:method});
		console.log("1" + $scope.buttonClasses[method] + ",," + method);
		$scope.buttonClasses[method] = 'btn btn-success btn-lg active';
		console.log("2" + $scope.buttonClasses[method]);
		console.log($scope.selectedCallInfo);
	}

	$scope.submit = function() {
		console.log("method is " + $scope.selectedCallInfo.method);
	}

	$scope.scream = function(){
		//console.log($scope.calls[i].fullPath)
	}



}]);
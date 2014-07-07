
apitester.controller('rootController', [ '$scope' , 'Restangular', function($scope, RA) {

	$scope.requestconfig = {};
	$scope.selectedCallInfo = {};
	$scope.showRequestButton = {};

	RA.all('basepaths').getList().then(
		function(basepaths) {
			$scope.basepaths = basepaths;
		}
	);

	$scope.resetAll = function() {

	}

	$scope.updateFullpathOptions = function() {
		RA.all('calls').getList({basePath:$scope.requestconfig.basepath}).then(
			function(calls) {
				$scope.calls = calls;
				$scope.fullPaths = _(calls).pluck('fullPath').uniq().value();
				$scope.selectedCallInfo = {};
				$scope.buttonClasses = {disabledBtn:{}, availableBtn:{}, deprecatedBtn:{}, activeBtn:{}};
				$scope.buttonPopOver = {};
				$scope.showRequestButton.flag = false;
				$scope.selectedCallIndex = -1;
			}
		);
	}

	$scope.selectFullPath = function() {
		$scope.showRequestButton.flag = true;
		$scope.selectedCallInfo = {};
		$scope.buttonClasses = {disabledBtn:{}, availableBtn:{}, deprecatedBtn:{}, activeBtn:{}};
		$scope.buttonPopOver = {};
		// $scope.buttonClasses.disabledBtn = {};
		// $scope.buttonClasses.availableBtn = {};
		// $scope.buttonClasses.deprecatedBtn = {};
		// $scope.buttonClasses.activeBtn = {};
	}

	$scope.methods = ['OPTIONS', 'GET', 'POST', 'PUT', 'DELETE'];
	$scope.buttonClasses = { disabledBtn:{}, availableBtn:{}, deprecatedBtn:{}, activeBtn:{}};
	// $scope.buttonClasses.disabledBtn = {};
	// $scope.buttonClasses.availableBtn = {};
	// $scope.buttonClasses.deprecatedBtn = {};
	// $scope.buttonClasses.activeBtn = {};

	$scope.buttonPopOver = {};

	$scope.getCall = function(method, fullPath) {
		var call = _.find($scope.calls, {fullPath:fullPath, method:method});
		if (call) {
			if(call.deprecated) {
				$scope.buttonClasses.deprecatedBtn[method] = true;
				$scope.buttonPopOver[method] = 'deprecated since ' + call.deprecatedSince;
			}
			$scope.buttonClasses.availableBtn[method] = true;		
		} else {
			$scope.buttonClasses.disabledBtn[method] = true;	
		}
		return call;
	}

	$scope.isDisabled = function(method) {
		return $scope.buttonClasses.disabledBtn[method];
	}

	$scope.isAvailable = function(method) {
		return $scope.buttonClasses.availableBtn[method];
	}

	$scope.isDeprecated = function(method) {
		return $scope.buttonClasses.deprecatedBtn[method];
	}

	$scope.isActive = function(method) {
		return $scope.buttonClasses.activeBtn[method];
	}

	$scope.selectRequest = function(method, fullPath) {
		$scope.selectedCallInfo = _.find($scope.calls, {fullPath:fullPath, method:method});
		$scope.buttonClasses.availableBtn[method] = false;
		$scope.buttonClasses.deprecatedBtn[method] = false;
		$scope.buttonClasses.activeBtn = {};
		$scope.buttonClasses.activeBtn[method] = true;
	}

	$scope.submit = function() {
		console.log("method is " + $scope.selectedCallInfo.method);
	}

	$scope.scream = function(){
		//console.log($scope.calls[i].fullPath)
	}



}]);
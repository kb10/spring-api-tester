
apitester.controller('rootController', [ '$scope' , '$http', 'Restangular', function($scope, $http, RA) {

	$scope.requestConfig = {};
	$scope.selectedCallInfo = {};
	$scope.showRequestButton = {};

	RA.all('basepaths').getList().then(
		function(basepaths) {
			$scope.basepaths = basepaths;
		}
	);

	$scope.resetAll = function() {
		$scope.selectedCallInfo = {};
		$scope.buttonClasses = {disabledBtn:{}, availableBtn:{}, deprecatedBtn:{}, activeBtn:{}};
		$scope.buttonPopOver = {};
		$scope.responseObject = {};
	};

	$scope.updateFullpathOptions = function() {
		RA.all('calls').getList({basePath:$scope.requestConfig.basepath}).then(
			function(calls) {
				$scope.calls = calls;
				$scope.fullPaths = _(calls).pluck('fullPath').uniq().value();
				$scope.showRequestButton.flag = false;
				$scope.selectedCallIndex = -1;
				$scope.resetAll();
			}
		);
	};

	$scope.selectFullPath = function() {
		$scope.showRequestButton.flag = true;
		$scope.resetAll();
	};

	$scope.methods = ['OPTIONS', 'GET', 'POST', 'PUT', 'DELETE'];
	$scope.buttonClasses = { disabledBtn:{}, availableBtn:{}, deprecatedBtn:{}, activeBtn:{}};
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
	};

	$scope.isDisabled = function(method) {
		return $scope.buttonClasses.disabledBtn[method];
	};

	$scope.isAvailable = function(method) {
		return $scope.buttonClasses.availableBtn[method];
	};

	$scope.isDeprecated = function(method) {
		return $scope.buttonClasses.deprecatedBtn[method];
	};

	$scope.isActive = function(method) {
		return $scope.buttonClasses.activeBtn[method];
	};

	$scope.selectRequest = function(method, fullPath) {
		$scope.selectedCallInfo = _.find($scope.calls, {fullPath:fullPath, method:method});
		$scope.buttonClasses.availableBtn[method] = false;
		$scope.buttonClasses.deprecatedBtn[method] = false;
		$scope.buttonClasses.activeBtn = {};
		$scope.buttonClasses.activeBtn[method] = true;
		$scope.showRequestButton.go = true;
		$scope.responseObject = {};
	};

	$scope.responseObject = {};

	$scope.submit = function() {
		$scope.prepareRequest();
		$http({	method : $scope.selectedCallInfo.method,
				url : $scope.requestConfig.url,
				params : $scope.requestConfig.params,
				data : $scope.requestConfig.requestBody}).
			success(function(data, status, headers, config, statusText) {
				$scope.responseObject.isSuccessful = true;
				$scope.responseObject.data = angular.toJson(data, true);
				$scope.responseObject.status = status;
				$scope.responseObject.headers = angular.toJson(headers(), true);
				$scope.responseObject.config = angular.toJson(config, true);
				$scope.responseObject.statusText = statusText;
			}).
			error(function(data, status, headers, config, statusText) {
				$scope.responseObject.isSuccessful = false;
				$scope.responseObject.data = data || 'Requset Failed';
				$scope.responseObject.status = status;
				$scope.responseObject.headers = angular.toJson(headers(), true);
				$scope.responseObject.config = angular.toJson(config, true);
				$scope.responseObject.statusText = statusText;
			});
	};

	$scope.prepareRequest = function() {
		var serverBaseUrl = 'http://127.0.0.1:8080/apitester/smp';
		var requestUrl = serverBaseUrl + $scope.selectedCallInfo.fullPath;
		return $scope.requestConfig.url;
	}

	$scope.scream = function(){
		//console.log($scope.calls[i].fullPath)
	};



}]);
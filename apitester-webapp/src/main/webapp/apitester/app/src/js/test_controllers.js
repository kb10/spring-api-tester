apitester.controller('testRootController', [ '$scope' , '$http', '$interval','Restangular',
 function($scope, $http, $interval, RA) {

	$scope.requestConfig = {};
	$scope.selectedCallInfo = {};
	$scope.showRequestButton = {};

	RA.all('basepaths').getList().then(
		function(basepaths) {
			$scope.basePaths = basepaths;
		}
	);

	$scope.resetAll = function() {
		$scope.selectedCallInfo = {};
		$scope.selectedDocIndex = {};
		$scope.buttonClasses = {disabledBtn:{}, availableBtn:{}, deprecatedBtn:{}, activeBtn:{}};
		$scope.buttonPopOver = {};
		$scope.requestObject = {};
		$scope.responseObject = {};
	};

	$scope.updateFullpathOptions = function() {
		RA.all('calls').getList({basePath:$scope.requestConfig.basePath}).then(
			function(calls) {
				$scope.calls = calls;
				$scope.fullPaths = _(calls).pluck('fullPath').uniq().value();
				$scope.showRequestButton.flag = false;
				$scope.selectedCallPath = $scope.fullPaths[0];
				$scope.resetAll();
			}
		);
	};

	$scope.selectFullPath = function() {
		$scope.showRequestButton.flag = true;
		$scope.resetAll();
	};

	$scope.selectedDocIndex;
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
		$scope.selectedDocIndex = _.findIndex($scope.calls, {fullPath:fullPath, method:method});
		$scope.selectedCallInfo = $scope.calls[$scope.selectedDocIndex];
		$scope.buttonClasses.availableBtn[method] = false;
		$scope.buttonClasses.deprecatedBtn[method] = false;
		$scope.buttonClasses.activeBtn = {};
		$scope.buttonClasses.activeBtn[method] = true;
		$scope.showRequestButton.go = true;
		$scope.requestObject = {};
		$scope.responseObject = {};
	};

	$scope.requestObject = {};
	$scope.responseObject = {};
	$scope.communicatingToServer = false;

	$scope.timer;
	$scope.counter = undefined;
	$scope.startTime = undefined;

	$scope.startCount = function() {
		if(!angular.isDefined($scope.startTime)) {
			$scope.startTime = new Date().getTime();
			$scope.timer = 0;
		}

		$scope.counter = $interval(function() {
			$scope.timer = new Date().getTime() - $scope.startTime;
		}, 5);
	};

	$scope.stopCount = function() {
		if(angular.isDefined($scope.counter)) {
			$interval.cancel($scope.counter);
			$scope.counter = undefined;
		}

		if(angular.isDefined($scope.startTime)) {
			$scope.startTime = undefined;
		}
	}

	$scope.$on('$destroy', function() {
		$scope.stopCount();
	});

	$scope.submit = function() {
		$scope.communicatingToServer = true;
		$scope.prepareRequest();
		$scope.startCount();
		$http({	method : $scope.selectedCallInfo.method,
				url : $scope.requestObject.url,
				params : $scope.requestObject.params,
				data : $scope.requestObject.requestBody}).
			success($scope.ajaxFinished).
			error($scope.ajaxFinished);
	};

	$scope.ajaxFinished = function(data, status, headers, config, statusText) {
		$scope.stopCount();
		$scope.communicatingToServer = false;
		$scope.responseObject.data = data;
		$scope.responseObject.isjson = $scope.isJsonData(data);
		$scope.responseObject.status = status;
		$scope.responseObject.headers = $scope.getHeaders(headers);
		$scope.responseObject.config = angular.toJson(config, true);
		$scope.responseObject.statusText = statusText;
	};

	$scope.isJsonData = function(data) {
		try {
			angular.fromJson(data);
			return true;
		}
		catch(e) {
			return false;
		}
	};

	$scope.getHeaders = function(headers) {
		var result = [];
		var keys = _.keys(headers());
		for(i = 0; i < keys.length; i++) {
			a = {};
			key = keys[i];
			a.key = key;
			a.value = headers(key);
			result.push(a);
		}
		return result;
	};

	$scope.prepareRequest = function() {
		var serverBaseUrl = 'http://127.0.0.1:8080';
		var requestUrl = serverBaseUrl + $scope.selectedCallInfo.fullPath;
		if($scope.selectedCallInfo.pathParameters.length > 0) {
			for(i = 0; i < $scope.selectedCallInfo.pathParameters.length; i++) {
				requestUrl = requestUrl.replace("{" + $scope.selectedCallInfo.pathParameters[i].parameterName + "}", 
					$scope.selectedCallInfo.pathParameters[i].value);
			}
		}
		$scope.requestObject.url = requestUrl;

		var requestParams = {};
		if($scope.selectedCallInfo.requestParameters.length > 0) {
			for(i = 0; i < $scope.selectedCallInfo.requestParameters.length; i++) {
				requestParams[$scope.selectedCallInfo.requestParameters[i].parameterName] = 
					$scope.selectedCallInfo.requestParameters[i].value;
			}
		}
		$scope.requestObject.params = requestParams;
	};

	$scope.hideConfigOfResponse = true;

	$scope.toggleHideConfigOfResponse = function() {
		$scope.hideConfigOfResponse = !$scope.hideConfigOfResponse;
	};

	$scope.isResponseSuccessful = function() {
		return $scope.responseObject.status>199 && $scope.responseObject.status<300;
	};

	$scope.isResponseFailed = function() {
		return $scope.responseObject.status>499 && $scope.responseObject.status<600;
	};

	$scope.selectCall = function(i) {
		var apicall = $scope.calls[i];
		$scope.selectedCallPath = $scope.fullPaths[_.indexOf($scope.fullPaths, apicall.fullPath)];
		$scope.resetAll();
		$scope.selectRequest(apicall.method, apicall.fullPath);
	};

	$scope.isDocSelected = function(i) {
		if(i == $scope.selectedDocIndex) {
			return true;
		}
		return false;
	};

}]);
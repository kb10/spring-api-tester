apitester.controller('docDetailController', ['$scope','$http','$location', function($scope, $http, $location) {
	$scope.docparam = {
		detail:'"{none}"',
		showDetail:false
	};

	$scope.expanded = false;

	$scope.toggle = function() {
		$scope.expanded = !$scope.expanded;
	};


	$scope.isNoneParam = function() {
		if($scope.docparam.detail.indexOf('{none}') >= 0) {
			return true;
		}
		return false;
	};

	$scope.getBaseUrl = function() {
		var absUrl = $location.absUrl(),
		    pos = absUrl.lastIndexOf('apitester');
		return absUrl.substring(0, pos + 9);
	};

	$scope.showParamDetails = function(){

		$scope.returnParamExpanded = !$scope.returnParamExpanded;
		
		if($scope.apicall.returnType.returnClass.className.indexOf('java.') >= 0) {
			return;
		}

		var baseUrl = $scope.getBaseUrl();
		$scope.docparam = {
			detail:'"{none}"',
			showDetail:false
		};		
		url = baseUrl + '/api/objects/'+$scope.apicall.returnType.returnClass.className+'/details';

		$http({	method : 'GET',
			url : url,
			params : '',
			data : ''}). 
		success(function(data, status, headers, config, statusText) {
			$scope.returnParamDetails = angular.toJson(data, true);
		}).error(function(data, status, headers, config, statusText) {
			$scope.returnParamDetails = 'ERROR happened!';
		});
	};

 }]);

apitester.controller('ParamController', ['$scope','$http','$location', function($scope, $http, $location) {

	$scope.expanded = false;

	$scope.toggle = function() {
		console.log('toggle');
		$scope.expanded = !$scope.expanded;
		$scope.showParamDetails();
	};

	$scope.showParamDetails = function(){
		
		if($scope.param.parameterType.className.indexOf('java.') >= 0) {
			return;
		}

		var baseUrl = $scope.getBaseUrl();
		$scope.docparam = {
			detail:'"{none}"',
			showDetail:false
		};		
		url = baseUrl + '/api/objects/'+$scope.param.parameterType.className+'/details';
		console.log(url);
		$http({	method : 'GET',
			url : url,
			params : '',
			data : ''}). 
		success(function(data, status, headers, config, statusText) {
			$scope.paramDetail = angular.toJson(data, true);
		}).error(function(data, status, headers, config, statusText) {
			$scope.paramDetail = 'ERROR happened!';
		});
	};
}]);
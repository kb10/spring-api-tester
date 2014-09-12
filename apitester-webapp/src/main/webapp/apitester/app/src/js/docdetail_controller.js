apitester.controller('docDetailController', ['$scope','$http','$location', function($scope, $http, $location) {
	$scope.docparam = {
		detail:'"{none}"',
		showDetail:false
	};

	$scope.showDocDetails = function() {
		$scope.docparam = {
			detail:'"{none}"',
			showDetail:false
		};		
	};

	$scope.showParamDetails = function(name){
		if(name.indexOf("java.") >= 0) {
			return;
		}

		var baseUrl = $scope.getBaseUrl();
		$scope.docparam = {
			detail:'"{none}"',
			showDetail:false
		};		
		$http({	method : "GET",
			url : baseUrl + "/api/objects/"+name+"/details",
			params : "",
			data : ""}). 
		success(function(data, status, headers, config, statusText) {
			$scope.docparam.detail = angular.toJson(data, true);
			$scope.docparam.showDetail = true;
		}).error(function(data, status, headers, config, statusText) {
			$scope.docparam.detail = "ERROR happened!";
		});
	};

	$scope.isNoneParam = function() {
		if($scope.docparam.detail.indexOf("{none}") >= 0) {
			return true;
		}
		return false;
	};

	$scope.getBaseUrl = function() {
		var absUrl = $location.absUrl();
		var pos = absUrl.indexOf("apitester");
		return absUrl.substring(0, pos + 9);
	};
 }]);
apitester.controller('docDetailController', ['$scope','$http',function($scope, $http) {
	$scope.docparam = {
		detail:'"{none}"',
		showDetail:false
	};

	$scope.showDocDetails = function() {
		$scope.docparam = {
			detail:'"{none}"',
			showDetail:false
		};		
	}

	$scope.showParamDetails = function(name){
		$scope.docparam = {
			detail:'"{none}"',
			showDetail:false
		};		
		$http({	method : "GET",
			url : "/apitester/apitester/api/objects/"+name+"/details",
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
		return $scope.docparam.detail == '"{none}"';
	}
}]);
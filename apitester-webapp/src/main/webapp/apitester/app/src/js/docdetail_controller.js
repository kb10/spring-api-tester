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
	}

	$scope.showParamDetails = function(name){
		console.log($location.url());
		console.log($location.absUrl());
		console.log($location.path());
		$scope.docparam = {
			detail:'"{none}"',
			showDetail:false
		};		
		$http({	method : "GET",
			url : "/smp/de/de/apitester/api/objects/"+name+"/details",
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
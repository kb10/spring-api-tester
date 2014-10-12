apitester.controller('ObjectController', [ '$scope' , '$location', '$http', 'Restangular', function($scope, $location, $http, RA) {

	$scope.searchTerm = "";

	$scope.getBaseUrl = function() {
		var absUrl = $location.absUrl(),
		    pos = absUrl.lastIndexOf('apitester');
		return absUrl.substring(0, pos + 9);
	};

	$scope.selected = {
		object : undefined
	};

	var baseUrl = $scope.getBaseUrl();
	url = baseUrl + '/api/objects/';

	$scope.update = function() {
		$scope.objects = [];
		var params = {
			params : {
				searchTerm : $scope.seachTerm
			}
		};
	
		a = { searchTerm : $scope.searchTerm }; 
	
		console.log(a);
	
		$http({	
			method : 'GET',
			url : url,
			params : a,
			data : ''
		}). 
		success(function(data, status, headers, config, statusText) {
			$scope.objects = data;
		});
	};

	$scope.select = function(a) {
		$scope.object=a;
		$scope.active =  a.className;
		url = baseUrl + '/api/objects/'+$scope.object.className+"/details";

		$http({	method : 'GET',
			url : url,
			params : '',
			data : ''}). 
		success(function(data, status, headers, config, statusText) {
			$scope.objectdetails = angular.toJson(angular.fromJson(data),true);
		});
	};

	$scope.update();
	$scope.$watch("searchTerm", _.debounce($scope.update, 500));
}]);
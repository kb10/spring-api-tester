var angular = angular;
var apitester = angular.module('fgApitester', ['restangular']);

apitester.controller('rootController', function($scope) {

	$scope.name = 'enter your name';

})
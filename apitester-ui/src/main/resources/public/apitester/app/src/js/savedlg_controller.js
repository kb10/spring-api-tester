var saveModalController = function($scope, $modalInstance, groups) {

	$scope.dlg = {};
	$scope.dlg.dbgroups = groups;
	if($scope.dlg.dbgroups.length > 0) {
		$scope.dlg.selectgroup = $scope.dlg.dbgroups[$scope.dlg.dbgroups.length - 1];
	}

	$scope.ok = function() {
		$modalInstance.close($scope.dlg);
	};

	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	};

	$scope.dlgSelectGroup =  function() {
		$scope.dlg.creategroup = undefined;
	};

	$scope.dlgCreateGroup = _.debounce(function() {
		$scope.dlg.selectgroup = undefined;
	}, 400);
};
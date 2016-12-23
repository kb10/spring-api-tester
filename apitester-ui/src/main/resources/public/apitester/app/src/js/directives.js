apitester.directive('myJsonEditor', function($parse) {
	return {
		restrict : 'E',
		replace : false,
		transclude : false,
		compile : function (element, attrs) {
			var modelAccessor = $parse(attrs.ngModel);
			var options = { mode : attrs.defaultmode,
							modes: ['code', 'form', 'text', 'tree', 'view'],
							"search": true
						  };
			var editor = new JSONEditor(element[0], options, {});

			return function(scope, element, attrs, controller) {
				var processChange = function () {
	               	var json = editor.get();
	               	scope.$apply(function (scope) {
	                	modelAccessor.assign(scope, json);
	               });
	            };

	            element.on('focusout', processChange);

	            scope.$watch(modelAccessor, function (val) {
	            	if(val != undefined) {
	            		editor.set(val);
	            	}
	            	else {
	            		editor.set({});
	            	}
	            });
			};
		}
	};
});

apitester.directive('myJsonEditor', function($parse) {
	return {
		restrict : 'A',
		require: 'ngModel',
		replace : false,
		transclude : false,
		link: function(scope, el, attrs, ngModel) {

			ngModel.$formatters.push(function(modelValue) {
				if(angular.isUndefined(modelValue)) {
					return {};
				}
				return modelValue;
			})

			ngModel.$render = function() {
			    editor.set(ngModel.$viewValue);
			};


			var options = { 
				mode : attrs.defaultmode, 
				modes: ['code', 'form', 'text', 'tree', 'view'], 
				search: true
			};

			var editor = new JSONEditor(el[0], options, {});

			var processChange = function () {
				var json = editor.get();
				//ngModel.$setViewValue(json);
              	scope.$apply(function (scope) {
                	ngModel.$setViewValue(json);
               	});

				console.log("----- new view value: ");
				console.log(ngModel.$viewValue);
				ngModel.$render();
            };

            el.on('focusout',processChange);

		}
	};
});
//kb10
apitester.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;
            
            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);
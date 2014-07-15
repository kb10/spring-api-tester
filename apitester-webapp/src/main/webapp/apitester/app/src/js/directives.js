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
			var editor = new JSONEditor(element[0], options);

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
	            });
			};
		}
	};
});
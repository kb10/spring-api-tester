var angular = angular;

var apitester = angular.module('fgApitester', 
		['restangular', 'ui.bootstrap', 'ngRoute', 'xc.indexedDB', 'ui.select2'])
	.constant('apitesterDBConf', {
		dbname : 'apitester',
		version : 2,
		historyStore : 'history',
	})	
	.config(function(apitesterDBConf, RestangularProvider, $routeProvider, $indexedDBProvider) {
		$routeProvider
			.when('/api/browse',{
				templateUrl: 'app/src/templates/apibrowser.html'
			})
			.when('/api/objects',{
				templateUrl: 'app/src/templates/objects.html'
			})
			.when('/', {
				templateUrl: 'app/src/templates/tester.html'
			})
			.otherwise({
				redirectTo: '/'
			})
		;
		
		RestangularProvider.setBaseUrl('api');

		/*
		scheme of historyStore 
			id
			name 
			group
			method
			req
			res
		*/
		$indexedDBProvider.connection(apitesterDBConf.dbname).upgradeDatabase(apitesterDBConf.version, function(event, db, tx){
			if(!db.objectStoreNames.contains(apitesterDBConf.historyStore)) {
	        	var historyStore = db.createObjectStore(apitesterDBConf.historyStore, {keyPath: 'id'});
	        	historyStore.createIndex('id_idx', 'id', {unique: true});
	        	historyStore.createIndex('group_idx', 'group', {unique: false});
	        }
	        else {
	        	console.log(apitesterDBConf.historyStore + " exists!");
	        }
      	});
	})
;
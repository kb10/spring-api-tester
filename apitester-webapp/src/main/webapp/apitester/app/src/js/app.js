var angular = angular;

var apitester = angular.module('fgApitester', 
		['restangular', 'ui.bootstrap', 'ngRoute', 'xc.indexedDB'])
	.constant('apitesterDB', {
		dbname : 'apitester',
		version : 2,
		historyStore : 'history',
		collectionStore : 'collection'
	})	
	.config(function(apitesterDB, RestangularProvider, $routeProvider, $indexedDBProvider) {
		$routeProvider
			.when('/api/browse',{
				templateUrl: 'app/src/templates/apibrowser.html'
			})
			.when('/', {
				templateUrl: 'app/src/templates/tester.html'
			})
			.otherwise({
				redirectTo: '/'
			})
		;
		
		RestangularProvider.setBaseUrl('api');

		$indexedDBProvider.connection(apitesterDB.dbname).upgradeDatabase(apitesterDB.version, function(event, db, tx){
	        var historyStore = db.createObjectStore(apitesterDB.historyStore, {keyPath: 'id'});
	        historyStore.createIndex('id_idx', 'id', {unique: true});
	        var collectionStore = db.createObjectStore(apitesterDB.collectionStore, {keyPath: 'id'});
	        collectionStore.createIndex('id_idx', 'id', {unique: true});
      	});
	})
;
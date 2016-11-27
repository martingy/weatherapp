(function() {
    'use strict';
    angular
        .module('weatherappApp')
        .factory('Alert', Alert)

    Alert.$inject = ['$resource'];

    function Alert ($resource) {
        var resourceUrl =  'api/alerts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' },
            'findCities': { url: resourceUrl + '/cities', method: 'GET', isArray: true},
            'getByCity': { url: resourceUrl + '/city/:city', method: 'GET'}
        });
    }
})();

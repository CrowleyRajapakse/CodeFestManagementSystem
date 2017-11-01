(function() {
    'use strict';
    angular
        .module('codeFestManagementSystemV1App')
        .factory('Resultevent', Resultevent);

    Resultevent.$inject = ['$resource'];

    function Resultevent ($resource) {
        var resourceUrl =  'api/resultevents/:id';

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
            'update': { method:'PUT' }
        });
    }
})();

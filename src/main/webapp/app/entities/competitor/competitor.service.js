(function() {
    'use strict';
    angular
        .module('codeFestManagementSystemV1App')
        .factory('Competitor', Competitor);

    Competitor.$inject = ['$resource'];

    function Competitor ($resource) {
        var resourceUrl =  'api/competitors/:id';

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

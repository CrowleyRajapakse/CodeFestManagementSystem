(function() {
    'use strict';
    angular
        .module('codeFestManagementSystemV1App')
        .factory('Judges', Judges);

    Judges.$inject = ['$resource'];

    function Judges ($resource) {
        var resourceUrl =  'api/judges/:id';

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

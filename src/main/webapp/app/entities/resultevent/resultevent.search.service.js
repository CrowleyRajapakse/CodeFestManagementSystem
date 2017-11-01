(function() {
    'use strict';

    angular
        .module('codeFestManagementSystemV1App')
        .factory('ResulteventSearch', ResulteventSearch);

    ResulteventSearch.$inject = ['$resource'];

    function ResulteventSearch($resource) {
        var resourceUrl =  'api/_search/resultevents/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();

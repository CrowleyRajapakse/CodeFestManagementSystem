(function() {
    'use strict';

    angular
        .module('codeFestManagementSystemV1App')
        .factory('WorkshopSearch', WorkshopSearch);

    WorkshopSearch.$inject = ['$resource'];

    function WorkshopSearch($resource) {
        var resourceUrl =  'api/_search/workshops/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();

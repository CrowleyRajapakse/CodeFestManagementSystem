(function() {
    'use strict';

    angular
        .module('codeFestManagementSystemV1App')
        .factory('CompetitorSearch', CompetitorSearch);

    CompetitorSearch.$inject = ['$resource'];

    function CompetitorSearch($resource) {
        var resourceUrl =  'api/_search/competitors/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();

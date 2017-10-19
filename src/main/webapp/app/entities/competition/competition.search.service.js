(function() {
    'use strict';

    angular
        .module('codeFestManagementSystemV1App')
        .factory('CompetitionSearch', CompetitionSearch);

    CompetitionSearch.$inject = ['$resource'];

    function CompetitionSearch($resource) {
        var resourceUrl =  'api/_search/competitions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();

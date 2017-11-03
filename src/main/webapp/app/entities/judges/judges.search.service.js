(function() {
    'use strict';

    angular
        .module('codeFestManagementSystemV1App')
        .factory('JudgesSearch', JudgesSearch);

    JudgesSearch.$inject = ['$resource'];

    function JudgesSearch($resource) {
        var resourceUrl =  'api/_search/judges/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();

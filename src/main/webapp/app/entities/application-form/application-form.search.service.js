(function() {
    'use strict';

    angular
        .module('codeFestManagementSystemV1App')
        .factory('ApplicationFormSearch', ApplicationFormSearch);

    ApplicationFormSearch.$inject = ['$resource'];

    function ApplicationFormSearch($resource) {
        var resourceUrl =  'api/_search/application-forms/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();

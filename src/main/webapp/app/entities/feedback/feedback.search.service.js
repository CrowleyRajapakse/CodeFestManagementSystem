(function() {
    'use strict';

    angular
        .module('codeFestManagementSystemV1App')
        .factory('FeedbackSearch', FeedbackSearch);

    FeedbackSearch.$inject = ['$resource'];

    function FeedbackSearch($resource) {
        var resourceUrl =  'api/_search/feedbacks/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();

(function() {
    'use strict';
    angular
        .module('codeFestManagementSystemV1App')
        .factory('Workshop', Workshop);

    Workshop.$inject = ['$resource', 'DateUtils'];

    function Workshop ($resource, DateUtils) {
        var resourceUrl =  'api/workshops/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.swTime = DateUtils.convertLocalDateFromServer(data.swTime);
                        data.swDate = DateUtils.convertLocalDateFromServer(data.swDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.swTime = DateUtils.convertLocalDateToServer(copy.swTime);
                    copy.swDate = DateUtils.convertLocalDateToServer(copy.swDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.swTime = DateUtils.convertLocalDateToServer(copy.swTime);
                    copy.swDate = DateUtils.convertLocalDateToServer(copy.swDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();

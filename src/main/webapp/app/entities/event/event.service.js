(function() {
    'use strict';
    angular
        .module('codeFestManagementSystemV1App')
        .factory('Event', Event);

    Event.$inject = ['$resource', 'DateUtils'];

    function Event ($resource, DateUtils) {
        var resourceUrl =  'api/events/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.start_Date = DateUtils.convertLocalDateFromServer(data.start_Date);
                        data.end_Date = DateUtils.convertLocalDateFromServer(data.end_Date);
                        data.start_time = DateUtils.convertDateTimeFromServer(data.start_time);
                        data.end_time = DateUtils.convertDateTimeFromServer(data.end_time);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.start_Date = DateUtils.convertLocalDateToServer(copy.start_Date);
                    copy.end_Date = DateUtils.convertLocalDateToServer(copy.end_Date);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.start_Date = DateUtils.convertLocalDateToServer(copy.start_Date);
                    copy.end_Date = DateUtils.convertLocalDateToServer(copy.end_Date);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();

(function () {
    'use strict';

    angular
        .module('codeFestManagementSystemV1App')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();

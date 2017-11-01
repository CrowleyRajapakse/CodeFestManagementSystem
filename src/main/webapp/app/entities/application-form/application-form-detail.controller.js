(function() {
    'use strict';

    angular
        .module('codeFestManagementSystemV1App')
        .controller('ApplicationFormDetailController', ApplicationFormDetailController);

    ApplicationFormDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ApplicationForm'];

    function ApplicationFormDetailController($scope, $rootScope, $stateParams, previousState, entity, ApplicationForm) {
        var vm = this;

        vm.applicationForm = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('codeFestManagementSystemV1App:applicationFormUpdate', function(event, result) {
            vm.applicationForm = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

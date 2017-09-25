(function() {
    'use strict';

    angular
        .module('codeFestManagementSystemV1App')
        .controller('WorkshopDetailController', WorkshopDetailController);

    WorkshopDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Workshop', 'Competition'];

    function WorkshopDetailController($scope, $rootScope, $stateParams, previousState, entity, Workshop, Competition) {
        var vm = this;

        vm.workshop = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('codeFestManagementSystemV1App:workshopUpdate', function(event, result) {
            vm.workshop = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

(function() {
    'use strict';

    angular
        .module('codeFestManagementSystemV1App')
        .controller('ResulteventDetailController', ResulteventDetailController);

    ResulteventDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Resultevent', 'Event'];

    function ResulteventDetailController($scope, $rootScope, $stateParams, previousState, entity, Resultevent, Event) {
        var vm = this;

        vm.resultevent = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('codeFestManagementSystemV1App:resulteventUpdate', function(event, result) {
            vm.resultevent = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

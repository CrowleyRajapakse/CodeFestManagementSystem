(function() {
    'use strict';

    angular
        .module('codeFestManagementSystemV1App')
        .controller('JudgesDetailController', JudgesDetailController);

    JudgesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Judges', 'Event'];

    function JudgesDetailController($scope, $rootScope, $stateParams, previousState, entity, Judges, Event) {
        var vm = this;

        vm.judges = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('codeFestManagementSystemV1App:judgesUpdate', function(event, result) {
            vm.judges = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

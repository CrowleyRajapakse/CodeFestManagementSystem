(function() {
    'use strict';

    angular
        .module('codeFestManagementSystemV1App')
        .controller('BudgetDetailController', BudgetDetailController);

    BudgetDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Budget', 'Event'];

    function BudgetDetailController($scope, $rootScope, $stateParams, previousState, entity, Budget, Event) {
        var vm = this;

        vm.budget = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('codeFestManagementSystemV1App:budgetUpdate', function(event, result) {
            vm.budget = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

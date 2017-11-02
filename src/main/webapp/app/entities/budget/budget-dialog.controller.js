(function() {
    'use strict';

    angular
        .module('codeFestManagementSystemV1App')
        .controller('BudgetDialogController', BudgetDialogController);

    BudgetDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Budget', 'Event'];

    function BudgetDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Budget, Event) {
        var vm = this;

        vm.budget = entity;
        vm.clear = clear;
        vm.save = save;
        vm.events = Event.query({filter: 'name-is-null'});
        $q.all([vm.budget.$promise, vm.events.$promise]).then(function() {
            if (!vm.budget.eventId) {
                return $q.reject();
            }
            return Event.get({id : vm.budget.eventId}).$promise;
        }).then(function(event) {
            vm.events.push(event);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.budget.id !== null) {
                Budget.update(vm.budget, onSaveSuccess, onSaveError);
            } else {
                Budget.save(vm.budget, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('codeFestManagementSystemV1App:budgetUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

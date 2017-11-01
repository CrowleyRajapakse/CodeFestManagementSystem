(function() {
    'use strict';

    angular
        .module('codeFestManagementSystemV1App')
        .controller('CompetitorDialogController', CompetitorDialogController);

    CompetitorDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Competitor', 'Event'];

    function CompetitorDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Competitor, Event) {
        var vm = this;

        vm.competitor = entity;
        vm.clear = clear;
        vm.save = save;
        vm.events = Event.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.competitor.id !== null) {
                Competitor.update(vm.competitor, onSaveSuccess, onSaveError);
            } else {
                Competitor.save(vm.competitor, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('codeFestManagementSystemV1App:competitorUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

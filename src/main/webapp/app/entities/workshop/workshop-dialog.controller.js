(function() {
    'use strict';

    angular
        .module('codeFestManagementSystemV1App')
        .controller('WorkshopDialogController', WorkshopDialogController);

    WorkshopDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Workshop', 'Competition'];

    function WorkshopDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Workshop, Competition) {
        var vm = this;

        vm.workshop = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.competitions = Competition.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.workshop.id !== null) {
                Workshop.update(vm.workshop, onSaveSuccess, onSaveError);
            } else {
                Workshop.save(vm.workshop, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('codeFestManagementSystemV1App:workshopUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.swTime = false;
        vm.datePickerOpenStatus.swDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();

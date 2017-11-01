(function() {
    'use strict';

    angular
        .module('codeFestManagementSystemV1App')
        .controller('ApplicationFormDialogController', ApplicationFormDialogController);

    ApplicationFormDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ApplicationForm'];

    function ApplicationFormDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ApplicationForm) {
        var vm = this;

        vm.applicationForm = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.applicationForm.id !== null) {
                ApplicationForm.update(vm.applicationForm, onSaveSuccess, onSaveError);
            } else {
                ApplicationForm.save(vm.applicationForm, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('codeFestManagementSystemV1App:applicationFormUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dob = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();

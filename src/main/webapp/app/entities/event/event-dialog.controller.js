(function() {
    'use strict';

    angular
        .module('codeFestManagementSystemV1App')
        .controller('EventDialogController', EventDialogController);

    EventDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Event', 'Competition'];

    function EventDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Event, Competition) {
        var vm = this;

        vm.event = entity;
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
            if (vm.event.id !== null) {
                Event.update(vm.event, onSaveSuccess, onSaveError);
            } else {
                Event.save(vm.event, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('codeFestManagementSystemV1App:eventUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.start_Date = false;
        vm.datePickerOpenStatus.end_Date = false;
        vm.datePickerOpenStatus.start_time = false;
        vm.datePickerOpenStatus.end_time = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();

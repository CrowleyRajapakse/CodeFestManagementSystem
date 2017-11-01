(function() {
    'use strict';

    angular
        .module('codeFestManagementSystemV1App')
        .controller('ResulteventDialogController', ResulteventDialogController);

    ResulteventDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Resultevent', 'Event'];

    function ResulteventDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Resultevent, Event) {
        var vm = this;

        vm.resultevent = entity;
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
            if (vm.resultevent.id !== null) {
                Resultevent.update(vm.resultevent, onSaveSuccess, onSaveError);
            } else {
                Resultevent.save(vm.resultevent, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('codeFestManagementSystemV1App:resulteventUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

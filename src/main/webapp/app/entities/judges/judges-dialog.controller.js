(function() {
    'use strict';

    angular
        .module('codeFestManagementSystemV1App')
        .controller('JudgesDialogController', JudgesDialogController);

    JudgesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Judges', 'Event'];

    function JudgesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Judges, Event) {
        var vm = this;

        vm.judges = entity;
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
            if (vm.judges.id !== null) {
                Judges.update(vm.judges, onSaveSuccess, onSaveError);
            } else {
                Judges.save(vm.judges, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('codeFestManagementSystemV1App:judgesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

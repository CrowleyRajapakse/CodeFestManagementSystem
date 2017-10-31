(function() {
    'use strict';

    angular
        .module('codeFestManagementSystemV1App')
        .controller('FeedbackDialogController', FeedbackDialogController);

    FeedbackDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Feedback'];

    function FeedbackDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Feedback) {
        var vm = this;

        vm.feedback = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.feedback.id !== null) {
                Feedback.update(vm.feedback, onSaveSuccess, onSaveError);
            } else {
                Feedback.save(vm.feedback, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('codeFestManagementSystemV1App:feedbackUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

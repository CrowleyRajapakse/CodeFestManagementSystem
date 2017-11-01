(function() {
    'use strict';

    angular
        .module('codeFestManagementSystemV1App')
        .controller('ApplicationFormDeleteController',ApplicationFormDeleteController);

    ApplicationFormDeleteController.$inject = ['$uibModalInstance', 'entity', 'ApplicationForm'];

    function ApplicationFormDeleteController($uibModalInstance, entity, ApplicationForm) {
        var vm = this;

        vm.applicationForm = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ApplicationForm.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

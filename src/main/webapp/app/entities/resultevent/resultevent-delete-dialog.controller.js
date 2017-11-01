(function() {
    'use strict';

    angular
        .module('codeFestManagementSystemV1App')
        .controller('ResulteventDeleteController',ResulteventDeleteController);

    ResulteventDeleteController.$inject = ['$uibModalInstance', 'entity', 'Resultevent'];

    function ResulteventDeleteController($uibModalInstance, entity, Resultevent) {
        var vm = this;

        vm.resultevent = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Resultevent.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

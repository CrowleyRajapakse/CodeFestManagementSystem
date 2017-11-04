(function() {
    'use strict';

    angular
        .module('codeFestManagementSystemV1App')
        .controller('JudgesDeleteController',JudgesDeleteController);

    JudgesDeleteController.$inject = ['$uibModalInstance', 'entity', 'Judges'];

    function JudgesDeleteController($uibModalInstance, entity, Judges) {
        var vm = this;

        vm.judges = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Judges.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

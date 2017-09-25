(function() {
    'use strict';

    angular
        .module('codeFestManagementSystemV1App')
        .controller('WorkshopDeleteController',WorkshopDeleteController);

    WorkshopDeleteController.$inject = ['$uibModalInstance', 'entity', 'Workshop'];

    function WorkshopDeleteController($uibModalInstance, entity, Workshop) {
        var vm = this;

        vm.workshop = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Workshop.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

(function() {
    'use strict';

    angular
        .module('codeFestManagementSystemV1App')
        .controller('CompetitorDeleteController',CompetitorDeleteController);

    CompetitorDeleteController.$inject = ['$uibModalInstance', 'entity', 'Competitor'];

    function CompetitorDeleteController($uibModalInstance, entity, Competitor) {
        var vm = this;

        vm.competitor = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Competitor.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

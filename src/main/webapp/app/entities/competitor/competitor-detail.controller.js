(function() {
    'use strict';

    angular
        .module('codeFestManagementSystemV1App')
        .controller('CompetitorDetailController', CompetitorDetailController);

    CompetitorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Competitor', 'Event'];

    function CompetitorDetailController($scope, $rootScope, $stateParams, previousState, entity, Competitor, Event) {
        var vm = this;

        vm.competitor = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('codeFestManagementSystemV1App:competitorUpdate', function(event, result) {
            vm.competitor = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

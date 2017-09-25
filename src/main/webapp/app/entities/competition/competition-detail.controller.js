(function() {
    'use strict';

    angular
        .module('codeFestManagementSystemV1App')
        .controller('CompetitionDetailController', CompetitionDetailController);

    CompetitionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Competition', 'Event', 'Workshop'];

    function CompetitionDetailController($scope, $rootScope, $stateParams, previousState, entity, Competition, Event, Workshop) {
        var vm = this;

        vm.competition = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('codeFestManagementSystemV1App:competitionUpdate', function(event, result) {
            vm.competition = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

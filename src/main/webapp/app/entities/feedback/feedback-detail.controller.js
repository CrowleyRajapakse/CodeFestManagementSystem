(function() {
    'use strict';

    angular
        .module('codeFestManagementSystemV1App')
        .controller('FeedbackDetailController', FeedbackDetailController);

    FeedbackDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Feedback'];

    function FeedbackDetailController($scope, $rootScope, $stateParams, previousState, entity, Feedback) {
        var vm = this;

        vm.feedback = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('codeFestManagementSystemV1App:feedbackUpdate', function(event, result) {
            vm.feedback = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

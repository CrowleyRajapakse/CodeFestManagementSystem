'use strict';

describe('Controller Tests', function() {

    describe('Competition Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockCompetition, MockEvent, MockWorkshop;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockCompetition = jasmine.createSpy('MockCompetition');
            MockEvent = jasmine.createSpy('MockEvent');
            MockWorkshop = jasmine.createSpy('MockWorkshop');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Competition': MockCompetition,
                'Event': MockEvent,
                'Workshop': MockWorkshop
            };
            createController = function() {
                $injector.get('$controller')("CompetitionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'codeFestManagementSystemV1App:competitionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});

(function () {
    'use strict';

    angular
        .module('codeFestManagementSystemV1App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('workshop', {
                parent: 'entity',
                url: '/workshop?page&sort&search',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_LECTURER'],
                    pageTitle: 'codeFestManagementSystemV1App.workshop.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/workshop/workshops.html',
                        controller: 'WorkshopController',
                        controllerAs: 'vm'
                    }
                },
                params: {
                    page: {
                        value: '1',
                        squash: true
                    },
                    sort: {
                        value: 'id,asc',
                        squash: true
                    },
                    search: null
                },
                resolve: {
                    pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                        return {
                            page: PaginationUtil.parsePage($stateParams.page),
                            sort: $stateParams.sort,
                            predicate: PaginationUtil.parsePredicate($stateParams.sort),
                            ascending: PaginationUtil.parseAscending($stateParams.sort),
                            search: $stateParams.search
                        };
                    }],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('workshop');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('workshop-detail', {
                parent: 'workshop',
                url: '/workshop/{id}',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_LECTURER'],
                    pageTitle: 'codeFestManagementSystemV1App.workshop.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/workshop/workshop-detail.html',
                        controller: 'WorkshopDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('workshop');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Workshop', function ($stateParams, Workshop) {
                        return Workshop.get({id: $stateParams.id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'workshop',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('workshop-detail.edit', {
                parent: 'workshop-detail',
                url: '/detail/edit',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_LECTURER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/workshop/workshop-dialog.html',
                        controller: 'WorkshopDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Workshop', function (Workshop) {
                                return Workshop.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('^', {}, {reload: false});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('workshop.new', {
                parent: 'workshop',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_LECTURER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/workshop/workshop-dialog.html',
                        controller: 'WorkshopDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    title: null,
                                    description: null,
                                    venue: null,
                                    wcoordinator: null,
                                    swTime: null,
                                    swDate: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function () {
                        $state.go('workshop', null, {reload: 'workshop'});
                    }, function () {
                        $state.go('workshop');
                    });
                }]
            })
            .state('workshop.edit', {
                parent: 'workshop',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_LECTURER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/workshop/workshop-dialog.html',
                        controller: 'WorkshopDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Workshop', function (Workshop) {
                                return Workshop.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('workshop', null, {reload: 'workshop'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('workshop.delete', {
                parent: 'workshop',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER', 'ROLE_LECTURER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/workshop/workshop-delete-dialog.html',
                        controller: 'WorkshopDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['Workshop', function (Workshop) {
                                return Workshop.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('workshop', null, {reload: 'workshop'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            });
    }

})();

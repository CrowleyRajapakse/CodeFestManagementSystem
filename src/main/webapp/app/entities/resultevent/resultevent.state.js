(function() {
    'use strict';

    angular
        .module('codeFestManagementSystemV1App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('resultevent', {
            parent: 'entity',
            url: '/resultevent?page&sort&search',
            data: {
                authorities: ['ROLE_USER','ROLE_LECTURER'],
                pageTitle: 'codeFestManagementSystemV1App.resultevent.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/resultevent/resultevents.html',
                    controller: 'ResulteventController',
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
                    $translatePartialLoader.addPart('resultevent');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('resultevent-detail', {
            parent: 'resultevent',
            url: '/resultevent/{id}',
            data: {
                authorities: ['ROLE_USER','ROLE_LECTURER'],
                pageTitle: 'codeFestManagementSystemV1App.resultevent.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/resultevent/resultevent-detail.html',
                    controller: 'ResulteventDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('resultevent');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Resultevent', function($stateParams, Resultevent) {
                    return Resultevent.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'resultevent',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('resultevent-detail.edit', {
            parent: 'resultevent-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER','ROLE_LECTURER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/resultevent/resultevent-dialog.html',
                    controller: 'ResulteventDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Resultevent', function(Resultevent) {
                            return Resultevent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('resultevent.new', {
            parent: 'resultevent',
            url: '/new',
            data: {
                authorities: ['ROLE_USER','ROLE_LECTURER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/resultevent/resultevent-dialog.html',
                    controller: 'ResulteventDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                winner: null,
                                runner_up1: null,
                                runner_up2: null,
                                merit1: null,
                                merit2: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('resultevent', null, { reload: 'resultevent' });
                }, function() {
                    $state.go('resultevent');
                });
            }]
        })
        .state('resultevent.edit', {
            parent: 'resultevent',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER','ROLE_LECTURER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/resultevent/resultevent-dialog.html',
                    controller: 'ResulteventDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Resultevent', function(Resultevent) {
                            return Resultevent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('resultevent', null, { reload: 'resultevent' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('resultevent.delete', {
            parent: 'resultevent',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER','ROLE_LECTURER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/resultevent/resultevent-delete-dialog.html',
                    controller: 'ResulteventDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Resultevent', function(Resultevent) {
                            return Resultevent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('resultevent', null, { reload: 'resultevent' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

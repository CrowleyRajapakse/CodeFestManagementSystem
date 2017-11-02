(function() {
    'use strict';

    angular
        .module('codeFestManagementSystemV1App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('competitor', {
            parent: 'entity',
            url: '/competitor?page&sort&search',
            data: {
                authorities: ['ROLE_USER','ROLE_LECTURER','ROLE_MEMBER'],
                pageTitle: 'codeFestManagementSystemV1App.competitor.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/competitor/competitors.html',
                    controller: 'CompetitorController',
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
                    $translatePartialLoader.addPart('competitor');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('competitor-detail', {
            parent: 'competitor',
            url: '/competitor/{id}',
            data: {
                authorities: ['ROLE_USER','ROLE_LECTURER','ROLE_MEMBER'],
                pageTitle: 'codeFestManagementSystemV1App.competitor.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/competitor/competitor-detail.html',
                    controller: 'CompetitorDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('competitor');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Competitor', function($stateParams, Competitor) {
                    return Competitor.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'competitor',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('competitor-detail.edit', {
            parent: 'competitor-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_LECTURER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/competitor/competitor-dialog.html',
                    controller: 'CompetitorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Competitor', function(Competitor) {
                            return Competitor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('competitor.new', {
            parent: 'competitor',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/competitor/competitor-dialog.html',
                    controller: 'CompetitorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                age: null,
                                university: null,
                                email: null,
                                phone: null,
                                category: null,
                                school: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('competitor', null, { reload: 'competitor' });
                }, function() {
                    $state.go('competitor');
                });
            }]
        })
        .state('competitor.edit', {
            parent: 'competitor',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER','ROLE_LECTURER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/competitor/competitor-dialog.html',
                    controller: 'CompetitorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Competitor', function(Competitor) {
                            return Competitor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('competitor', null, { reload: 'competitor' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('competitor.delete', {
            parent: 'competitor',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_LECTURER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/competitor/competitor-delete-dialog.html',
                    controller: 'CompetitorDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Competitor', function(Competitor) {
                            return Competitor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('competitor', null, { reload: 'competitor' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

(function() {
    'use strict';

    angular
        .module('codeFestManagementSystemV1App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('judges', {
            parent: 'entity',
            url: '/judges?page&sort&search',
            data: {
                authorities: ['ROLE_LECTURER'],
                pageTitle: 'codeFestManagementSystemV1App.judges.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/judges/judges.html',
                    controller: 'JudgesController',
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
                    $translatePartialLoader.addPart('judges');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('judges-detail', {
            parent: 'judges',
            url: '/judges/{id}',
            data: {
                authorities: ['ROLE_LECTURER'],
                pageTitle: 'codeFestManagementSystemV1App.judges.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/judges/judges-detail.html',
                    controller: 'JudgesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('judges');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Judges', function($stateParams, Judges) {
                    return Judges.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'judges',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('judges-detail.edit', {
            parent: 'judges-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_LECTURER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/judges/judges-dialog.html',
                    controller: 'JudgesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Judges', function(Judges) {
                            return Judges.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('judges.new', {
            parent: 'judges',
            url: '/new',
            data: {
                authorities: ['ROLE_LECTURER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/judges/judges-dialog.html',
                    controller: 'JudgesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                gender: null,
                                email: null,
                                phone: null,
                                job_title: null,
                                employer: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('judges', null, { reload: 'judges' });
                }, function() {
                    $state.go('judges');
                });
            }]
        })
        .state('judges.edit', {
            parent: 'judges',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_LECTURER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/judges/judges-dialog.html',
                    controller: 'JudgesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Judges', function(Judges) {
                            return Judges.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('judges', null, { reload: 'judges' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('judges.delete', {
            parent: 'judges',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_LECTURER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/judges/judges-delete-dialog.html',
                    controller: 'JudgesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Judges', function(Judges) {
                            return Judges.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('judges', null, { reload: 'judges' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

(function() {
    'use strict';

    angular
        .module('codeFestManagementSystemV1App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('budget', {
            parent: 'entity',
            url: '/budget?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'codeFestManagementSystemV1App.budget.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/budget/budgets.html',
                    controller: 'BudgetController',
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
                    $translatePartialLoader.addPart('budget');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('budget-detail', {
            parent: 'budget',
            url: '/budget/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'codeFestManagementSystemV1App.budget.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/budget/budget-detail.html',
                    controller: 'BudgetDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('budget');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Budget', function($stateParams, Budget) {
                    return Budget.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'budget',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('budget-detail.edit', {
            parent: 'budget-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/budget/budget-dialog.html',
                    controller: 'BudgetDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Budget', function(Budget) {
                            return Budget.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('budget.new', {
            parent: 'budget',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/budget/budget-dialog.html',
                    controller: 'BudgetDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                food: null,
                                decorations: null,
                                prizes: null,
                                photography: null,
                                transport: null,
                                stationery: null,
                                guests: null,
                                other: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('budget', null, { reload: 'budget' });
                }, function() {
                    $state.go('budget');
                });
            }]
        })
        .state('budget.edit', {
            parent: 'budget',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/budget/budget-dialog.html',
                    controller: 'BudgetDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Budget', function(Budget) {
                            return Budget.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('budget', null, { reload: 'budget' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('budget.delete', {
            parent: 'budget',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/budget/budget-delete-dialog.html',
                    controller: 'BudgetDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Budget', function(Budget) {
                            return Budget.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('budget', null, { reload: 'budget' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

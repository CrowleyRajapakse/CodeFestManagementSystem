(function() {
    'use strict';

    angular
        .module('codeFestManagementSystemV1App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('application-form', {
            parent: 'entity',
            url: '/application-form?page&sort&search',
            data: {
                authorities: ['ROLE_USER','ROLE_LECTURER'],
                pageTitle: 'codeFestManagementSystemV1App.applicationForm.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/application-form/application-forms.html',
                    controller: 'ApplicationFormController',
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
                    $translatePartialLoader.addPart('applicationForm');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('application-form-detail', {
            parent: 'application-form',
            url: '/application-form/{id}',
            data: {
                authorities: ['ROLE_USER','ROLE_LECTURER'],
                pageTitle: 'codeFestManagementSystemV1App.applicationForm.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/application-form/application-form-detail.html',
                    controller: 'ApplicationFormDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('applicationForm');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ApplicationForm', function($stateParams, ApplicationForm) {
                    return ApplicationForm.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'application-form',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('application-form-detail.edit', {
            parent: 'application-form-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_LECTURER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/application-form/application-form-dialog.html',
                    controller: 'ApplicationFormDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ApplicationForm', function(ApplicationForm) {
                            return ApplicationForm.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('application-form.new', {
            parent: 'application-form',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/application-form/application-form-dialog.html',
                    controller: 'ApplicationFormDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                dob: null,
                                year: null,
                                email: null,
                                phone: null,
                                category: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('application-form', null, { reload: 'application-form' });
                }, function() {
                    $state.go('application-form');
                });
            }]
        })
        .state('application-form.edit', {
            parent: 'application-form',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_LECTURER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/application-form/application-form-dialog.html',
                    controller: 'ApplicationFormDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ApplicationForm', function(ApplicationForm) {
                            return ApplicationForm.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('application-form', null, { reload: 'application-form' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('application-form.delete', {
            parent: 'application-form',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_LECTURER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/application-form/application-form-delete-dialog.html',
                    controller: 'ApplicationFormDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ApplicationForm', function(ApplicationForm) {
                            return ApplicationForm.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('application-form', null, { reload: 'application-form' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

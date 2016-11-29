(function() {
    'use strict';

    angular
        .module('weatherappApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('alert', {
            parent: 'app',
            url: '/alert',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Alert'
            },
            views: {
                'content@': {
                    templateUrl: 'app/alert/alert.html',
                    controller: 'AlertController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            },
            onEnter: ['Notification', function(Notification) {
                Notification.subscribe();
            }],
            onExit: ['Notification', function(Notification) {
                Notification.unsubscribe();
            }]
        })
        .state('alert-detail', {
            parent: 'app',
            url: '/alert/{id:[0-9]+}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Alert'
            },
            views: {
                'content@': {
                    templateUrl: 'app/alert/alert-detail.html',
                    controller: 'AlertDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Alert', function($stateParams, Alert) {
                    return Alert.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'alert',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('alert-detail.edit', {
            parent: 'alert-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/alert/alert-dialog.html',
                    controller: 'AlertDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Alert', function(Alert) {
                            return Alert.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('alert.new', {
            parent: 'alert',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/alert/alert-dialog.html',
                    controller: 'AlertDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                thresholdTemperature: null,
                                temperature: null,
                                weatherDescription: null,
                                icon: null,
                                email: false,
                                popup: false,
                                city: null,
                                cityId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('alert', null, { reload: 'alert' });
                }, function() {
                    $state.go('alert');
                });
            }]
        })
        .state('alert.edit', {
            parent: 'alert',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/alert/alert-dialog.html',
                    controller: 'AlertDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Alert', function(Alert) {
                            return Alert.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('alert', null, { reload: 'alert' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('alert.delete', {
            parent: 'alert',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/alert/alert-delete-dialog.html',
                    controller: 'AlertDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Alert', function(Alert) {
                            return Alert.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('alert', null, { reload: 'alert' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }
})();

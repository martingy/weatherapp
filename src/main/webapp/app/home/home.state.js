(function() {
    'use strict';

    angular
        .module('weatherappApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('home', {
            parent: 'app',
            url: '/',
            data: {
                authorities: [],
                pageTitle: 'Weatherapp'
            },
            views: {
                'content@': {
                    templateUrl: 'app/home/home.html',
                    controller: 'HomeController',
                    controllerAs: 'vm'
                }
            },
            onEnter: ['Notification', function(Notification) {
                Notification.subscribe();
            }],
            onExit: ['Notification', function(Notification) {
                Notification.unsubscribe();
            }]
        });
    }
})();

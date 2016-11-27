(function() {
    'use strict';

    angular
        .module('weatherappApp', [
            'ngStorage',
            'ngResource',
            'ngCookies',
            'ngAria',
            'ngCacheBuster',
            'ngFileUpload',
            'ui.bootstrap',
            'ui.bootstrap.datetimepicker',
            'ui.router',
            'infinite-scroll',
            // jhipster-needle-angularjs-add-module JHipster will add new module here
            'angular-loading-bar'
        ])
        .run(run);

    run.$inject = ['stateHandler', '$state', '$rootScope'];

    function run(stateHandler, $state, $rootScope) {
        stateHandler.initialize();
        $rootScope.$state = $state;
    }
})();

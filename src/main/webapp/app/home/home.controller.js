(function() {
    'use strict';

    angular
        .module('weatherappApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'AlertService', 'ParseLinks', 'Notification'];

    function HomeController ($scope, Principal, LoginService, $state, AlertService, ParseLinks, Notification) {
        var vm = this;

        Notification.receive().then(null, null, function(notification) {
            showNotification(notification);
        });

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }

        function showNotification(notification) {
            var msg = "Weather alert for " + notification.city +
                ". Current temperature (" + notification.temperature + "C°) " +
                "is higher than the preset threshold temperature (" + notification.thresholdTemperature + "C°).";
            AlertService.add({
                type: 'danger',
                msg: msg,
                params: undefined,
                timeout: 10000,
                toast: false,
                position: undefined
            });
        }

    }
})();

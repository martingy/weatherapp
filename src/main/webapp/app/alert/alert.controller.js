(function() {
    'use strict';

    angular
        .module('weatherappApp')
        .controller('AlertController', AlertController);

    AlertController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'Alert', 'AlertService', 'ParseLinks',
        'OPEN_WEATHER_MAP_ICON_URL', 'Notification'];

    function AlertController ($scope, Principal, LoginService, $state, Alert, AlertService, ParseLinks, OPEN_WEATHER_MAP_ICON_URL, Notification) {
        var vm = this;

        vm.OPEN_WEATHER_MAP_ICON_URL = OPEN_WEATHER_MAP_ICON_URL;


        vm.alerts = [];
        vm.loadPage = loadPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;

        loadAll();

        Notification.receive().then(null, null, function(notification) {
            showNotification(notification);
            loadAll();
        });

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

        function loadAll () {
            Alert.query({
                page: vm.page,
                size: 20,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.alerts.push(data[i]);
                }
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset () {
            vm.page = 0;
            vm.alerts = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }
    }
})();

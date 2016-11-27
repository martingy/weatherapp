(function() {
    'use strict';

    angular
        .module('weatherappApp')
        .controller('AlertDetailController', AlertDetailController);

    AlertDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Alert', 'User'];

    function AlertDetailController($scope, $rootScope, $stateParams, previousState, entity, Alert, User) {
        var vm = this;

        vm.alert = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('weatherappApp:alertUpdate', function(event, result) {
            vm.alert = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

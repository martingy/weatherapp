(function() {
    'use strict';

    angular
        .module('weatherappApp')
        .controller('AlertAlreadyExistsController',AlertAlreadyExistsController);

    AlertAlreadyExistsController.$inject = ['$uibModalInstance', 'entity', 'Alert'];

    function AlertAlreadyExistsController($uibModalInstance, entity, Alert) {
        var vm = this;

        vm.alert = entity;
        vm.clear = clear;
        vm.confirmUpdateAlreadyExisting = confirmUpdateAlreadyExisting;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmUpdateAlreadyExisting (id) {
            Alert.update(entity,
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

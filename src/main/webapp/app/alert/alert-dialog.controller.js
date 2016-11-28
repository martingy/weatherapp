(function() {
    'use strict';

    angular
        .module('weatherappApp')
        .controller('AlertDialogController', AlertDialogController);

    AlertDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Alert',
        'User', 'OPEN_WEATHER_MAP_ICON_URL', '$state', '$uibModal'];

    function AlertDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Alert, User, OPEN_WEATHER_MAP_ICON_URL, $state, $uibModal) {
        var vm = this;

        vm.alert = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();
        vm.OPEN_WEATHER_MAP_ICON_URL = OPEN_WEATHER_MAP_ICON_URL;
        vm.findCities = findCities;
        vm.citySelected = citySelected;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.alert.id !== null) {
                Alert.update(vm.alert, onSaveSuccess, onSaveError);
            } else {
                Alert.getByCity({city : vm.alert.city}, onGetByCitySuccess, onGetByCityError);
            }
        }

        function onGetByCitySuccess (result) {
            if (result.id) {
                vm.alert.id = result.id;

                $uibModal.open({
                    templateUrl: 'app/entity/alert-already-exists-dialog.html',
                    controller: 'AlertAlreadyExistsController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: [function() {
                            return vm.alert;
                        }]
                    }
                }).result.then(function() {
                    $state.go('alert', null, { reload: 'alert' });
                    clear();
                }, function() {
                    $state.go('alert');
                });
            } else {
                Alert.save(vm.alert, onSaveSuccess, onSaveError);
            }
        }

        function onGetByCityError (error) {
        }

        function onSaveSuccess (result) {
            $scope.$emit('weatherappApp:alertUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        function findCities (q) {
            return Alert.findCities({
                q: q
            }, onFindCitiesSuccess, onFindCitiesError).$promise;
        }

        function onFindCitiesSuccess (result) {
            return result;
        }

        function onFindCitiesError () {
        }

        function citySelected($item, $model, $label, $event) {
            vm.alert.cityId = $item.id;
            vm.alert.weatherDescription = $item.weather[0].description;
            vm.alert.icon = $item.weather[0].icon;
            vm.alert.temperature = $item.main.temp;
        }

    }
})();

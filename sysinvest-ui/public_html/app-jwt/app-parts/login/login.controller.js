(function () {
    'use strict';

    angular
            .module('app')
            .controller('LoginController', Controller);

    function Controller($location, AuthenticationService) {
        var vm = this;

        initController();

        function initController() {
            // reset login status
            AuthenticationService.Logout();
        }

        vm.login = function () {
            vm.loading = true;
            AuthenticationService.Login(vm.username, vm.password, function (result) {
                if (result === true) {
                    $location.path('/');
                } else {
                    vm.error = 'Username or password is incorrect';
                    vm.loading = false;
                }
            });
        };
    }

})();
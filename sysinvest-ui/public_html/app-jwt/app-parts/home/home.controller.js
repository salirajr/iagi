(function () {
    'use strict';

    angular
            .module('app')
            .controller('HomeController', Controller);

    function Controller($scope, $localStorage, $http, $log) {
        var vm = this;

        initController();

        function initController() {
            $scope.username = $localStorage.currentUser.username;
        }

        $http.get('/api/userroles/' + $localStorage.currentUser.username)
                .success(function (response) {
                    $log.debug($localStorage.currentUser.username + " has roles " + JSON.stringify(response));
                });
    }

})();
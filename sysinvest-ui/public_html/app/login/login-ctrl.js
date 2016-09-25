

(function () {
    angular.getApplicationModule()
            .controller('LoginCtrl', LoginCtrl);

    /*
     * LoginCtrl - controller
     */
    function LoginCtrl($rootScope, $scope, $state, $log, AuthenticationService, $stateParams) {
        $log.debug('LoginCtrl is loaded');

        $scope.doLogin = function () {
            AuthenticationService.doLogin($scope.username, $scope.password, function (isSuccess, response) {
                if (isSuccess) {
                    $rootScope.popSuccess("Login", response.message);
                    $state.go('index.main');
                } else {
                    // failed login
                    $rootScope.popError("Login", response.message);
                }
            });

        };
        $log.debug('$stateParams=' + JSON.stringify($stateParams));
        $scope.message = $stateParams.msg;
    }

})();



(function () {
    angular.getApplicationModule()
            .controller('LoginCtrl', LoginCtrl);

    /*
     * LoginCtrl - controller
     */
    function LoginCtrl($scope, $state, $log, AuthenticationService, $stateParams) {
        $log.debug('LoginCtrl is loaded');


        $scope.doLogin = function () {
            AuthenticationService.doLogin($scope.username, $scope.password, function (isSuccess, response) {
                if (isSuccess) {
                    $state.go('index.main');
                } else {
                    // failed login
                    $scope.message = response.message;
                }
            });


        };
        $log.debug('$stateParams=' + JSON.stringify($stateParams));
        $scope.message = $stateParams.msg;
    }

})();

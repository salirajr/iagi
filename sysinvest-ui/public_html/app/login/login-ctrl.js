

(function () {
    angular.getApplicationModule()
            .controller('LoginCtrl', LoginCtrl);

    /*
     * LoginCtrl - controller
     */
    function LoginCtrl($scope, $state, $log, AuthenticationService, $stateParams) {
        $log.debug('LoginCtrl is loaded');
        $scope.username = "jovi@iagi.com";
        $scope.password = "12345";

        $scope.doLogin = function () {
            AuthenticationService.doLogin($scope.username, $scope.password, function (isSuccess) {
                if (isSuccess) {
                    $state.go('index.main');
                } else {
                    // failed login
                }
            });


        };
        $log.debug('$stateParams=' + JSON.stringify($stateParams));
        $scope.message = $stateParams.msg;
    }

})();

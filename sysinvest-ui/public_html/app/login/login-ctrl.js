

(function () {
    angular.getApplicationModule()
            .controller('LoginCtrl', LoginCtrl);

    /*
     * LoginCtrl - controller
     */
    function LoginCtrl($scope, $state, $log, $location, $stateParams) {
        $log.debug('LoginCtrl is loaded');
        $scope.username = "a@a.a";
        $scope.password = "a";

        $scope.doLogin = function () {
            $state.go('index.main');
        };
        $log.debug('$stateParams=' + JSON.stringify($stateParams));
        $scope.message = $stateParams.msg;
    }

})();

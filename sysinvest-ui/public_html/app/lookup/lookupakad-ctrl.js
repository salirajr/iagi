
(function () {

    angular.getApplicationModule()
            .controller('LookupAkadCtrl', LookupAkadCtrl);

    /*
     * MainCtrl - controller
     */
    function LookupAkadCtrl($scope, $rootScope, $log, $http) {


        function initiate() {
            $scope.lookup = {};

        }
        initiate();

        
    }

})();


(function () {

    angular.getApplicationModule()
            .controller('LookupCtrl', LookupCtrl);

    /*
     * MainCtrl - controller
     */
    function LookupCtrl($scope, $rootScope, $log, $http) {



        function initiate() {
            $scope.lookups = {};
            $scope.data = {};
            $scope.data.lookup = {};

            $http.get("/api/lookup/ret/listGroupName")
                    .then(function (response) {
                        $log.debug(response);
                        $scope.lookup.groups = response.data;
                        $scope.data.lookup.groupName = $scope.lookup.groups[0];
                    });




        }
        initiate();

        $scope.getItems = function () {
            $http.get("/api/lookup/ret/findByGroupName?value=" + $scope.data.lookup.groupName)
                    .then(function (response) {
                        $log.debug(response);
                        $scope.lookups = response.data;
                    });
        };


    }

})();

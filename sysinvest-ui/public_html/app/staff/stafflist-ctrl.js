
(function () {

    angular.getApplicationModule()
            .controller('StaffListCtrl', StaffListCtrl);


    /*
     * InvestorCtrl - controller
     */
    function StaffListCtrl($rootScope, $scope, $log, $http, $location) {

        $scope.list = {};
        $scope.list.staff = [];

        $scope.getStaffList = function () {
            $http.get("/api/staff/ret?key=")
                    .then(function (response) {
                        $scope.list.staff = response.data;
                    });
        };
        $scope.getStaffList();



    }



})();

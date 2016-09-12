
(function () {

    angular.getApplicationModule()
            .controller('StaffListCtrl', StaffListCtrl);


    /*
     * InvestorCtrl - controller
     */
    function StaffListCtrl($rootScope, $scope, $log, $http, $location) {
        $log.debug(' StaffListCtrl is loaded');
        $scope.userName = 'Admin';
        $scope.helloText = 'Hello You Welcome in SeedProject';
        $scope.descriptionText = 'Main Investor Controller.';

        $scope.list = {};
        $scope.list.investor = [];

        $scope.getInvestorList = function () {
            $http.get("/api/staff/ret")
                    .then(function (response) {
                        $scope.list.investor = response.data;
                        $log.debug($scope.list.investor);
                    });
        };
        $scope.getInvestorList();



    }



})();

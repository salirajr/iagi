
(function () {

    angular.getApplicationModule()
            .controller('InvestorListCtrl', InvestorListCtrl);


    /*
     * InvestorCtrl - controller
     */
    function InvestorListCtrl($rootScope, $scope, $log, $http, $location) {
        $log.debug(' InvestorListCtrl is loaded');
        $scope.userName = 'Admin';
        $scope.helloText = 'Hello You Welcome in SeedProject';
        $scope.descriptionText = 'Main Investor Controller.';

        $scope.list = {};
        $scope.list.investor = [];

        $scope.getInvestorList = function () {
            $http.get("/api/investor/ret")
                    .then(function (response) {
                        $scope.list.investor = response.data;
                        $log.debug($scope.list.investor);
                    });
        };
        $scope.getInvestorList();



    }



})();

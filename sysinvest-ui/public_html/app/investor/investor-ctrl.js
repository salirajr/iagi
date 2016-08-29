
(function () {

    angular.getApplicationModule()
            .controller('InvestorCtrl', InvestorCtrl);


    /*
     * InvestorCtrl - controller
     */
    function InvestorCtrl($scope, $log, $http) {
        $log.debug('Main Investor is loaded');
        $scope.userName = 'Admin';
        $scope.helloText = 'Hello You Welcome in SeedProject';
        $scope.descriptionText = 'Main Investor Controller.';

        $scope.data = {};
        
        $scope.getInvestor = function () {
            $http.get("sdata/investor.json")
                    .then(function (response) {
                        $scope.data.investor = response.data;
                        $log.debug("getInvestor responsed sucess");
                    });

        };
        $scope.getInvestor();
        
         $scope.getInvestment = function () {
            $http.get("sdata/investment.json")
                    .then(function (response) {
                        $scope.data.investment = response.data;
                        $log.debug("getInvestment responsed sucess");
                    });

        };
        $scope.getInvestment();
    }



})();

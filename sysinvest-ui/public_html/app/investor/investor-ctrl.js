
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
            $log.debug("getInvestor clicked!");
            $http.get("sdata/investor.json")
                    .then(function (response) {
                        $scope.data.investors = response.data;
                        $log.debug("getInvestor responsed sucess");
                    });

        };
    }



})();

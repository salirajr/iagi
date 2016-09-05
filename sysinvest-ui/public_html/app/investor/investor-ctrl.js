
(function () {

    angular.getApplicationModule()
            .controller('InvestorCtrl', InvestorCtrl);


    /*
     * InvestorCtrl - controller
     */
    function InvestorCtrl($rootScope, $scope, $log, $http, $location) {
        $log.debug('Main Investor is loaded');
        $scope.userName = 'Admin';
        $scope.helloText = 'Hello You Welcome in SeedProject';
        $scope.descriptionText = 'Main Investor Controller.';

        $scope.data = {};

        $rootScope.dctrl = {};
        $rootScope.dctrl.investor = {};


        //$scope.getInvestor = function () {
        //    $http.get("sdata/investor.json")
        //            .then(function (response) {
        //                $scope.data.investor = response.data;
        //                $log.debug("getInvestor responsed sucess");
        //            });
        //
        //};
        //$scope.getInvestor();


        $scope.saveInvestor = function () {
            var payload = $scope.data.investor;
            payload.birthDate = new Date($scope.data.investor.birthDate);

            $http.post('/investor/addnew', payload)
                    .success(function (response) {
                        $scope.data.investor = response;
                        $rootScope.dctrl.investor.id = response.id;
                    }).error(function (err) {
                $log.debug(err);
            });

        };

        $scope.nextToCovenant = function () {
            $rootScope.dctrl.investor = $scope.data.investor;
            $location.path('/index/covenant');
        };

    }



})();


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
        $scope.data.investor = {};

        $rootScope.data = {};
        $rootScope.data.investor = {};

        $scope.data.accountType = 'INVESTASI';

        $scope.saveInvestor = function () {
            if ($scope.data.accountType === 'INVESTASI') {
                var payload = $scope.data.investor;
                payload.birthDate = new Date($scope.data.investor.birthDate);
                $http.post('/api/investor/addnew', payload)
                        .success(function (response) {
                            $scope.data.investor = response;
                            $rootScope.data.investor.id = response.id;
                        }).error(function (err) {
                    $log.debug(err);
                });
            }else{
                
            }
        };

        $scope.nextToCovenant = function () {
            $rootScope.data.investor = $scope.data.investor;
            $location.path('/index/covenant');
        };
        
        $scope.getAccount = function () {
            $http.get("/api/investor/ret/byaccountid?value=" + $scope.data.investor.accountId)
                    .then(function (response) {
                        $log.debug(response);
                        $scope.data.investor = response.data;
                        $log.debug("getAccount responsed sucess with " + $scope.data.investor.accountId);
                    });

        };

    }



})();

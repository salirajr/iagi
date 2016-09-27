
(function () {

    angular.getApplicationModule()
            .controller('InvestorListCtrl', InvestorListCtrl);

    /*
     * InvestorCtrl - controller
     */
    function InvestorListCtrl($rootScope, $state, $scope, $log, $http, ErrorHandler) {

        /* predefinede function */
        $scope.getInvestorList = function () {
            $http.get("/api/investor/ret")
                    .then(function (response) {
                        $scope.list.investor = response.data;
                    }, ErrorHandler.handlingHttp);
        };
        
        function initiate() {
            $scope.temp = {};
            $scope.list = {};
            $scope.list.investor = [];
            $rootScope.data = {};

            $scope.getInvestorList();

        }
        initiate();



        $scope.doAction = function (investorId) {
            for (var i = 0; i < $scope.list.investor.length; i++) {
                if ($scope.list.investor[i].id === investorId) {
                    $rootScope.data.investor = $scope.list.investor[i];
                    break;
                }
            }
            $state.go('index.investor');
        };
    }



})();


(function () {

    angular.getApplicationModule()
            .controller('InvestorListCtrl', InvestorListCtrl);


    /*
     * InvestorCtrl - controller
     */
    function InvestorListCtrl($rootScope, $state, $scope, $log, $http, $location) {
        $log.debug(' InvestorListCtrl is loaded');
        $scope.userName = 'Admin';
        $scope.helloText = 'Hello You Welcome in SeedProject';
        $scope.descriptionText = 'Main Investor Controller.';

        $scope.temp = {};

        $scope.list = {};
        $scope.list.investor = [];

        $rootScope.data = {};

        $scope.getInvestorList = function () {
            $http.get("/api/investor/ret")
                    .then(function (response) {
                        $scope.list.investor = response.data;
                        $log.debug($scope.list.investor);
                    });
        };
        $scope.getInvestorList();

        $scope.doAction = function () {

            if ($scope.temp.investorId === undefined) {
                alert("Tidak ada investor yang anda pilih, silahkan pilih salah satu melalui tombol radio.");
                return;
            }


            for (var i = 0; i < $scope.list.investor.length; i++) {
                if ($scope.list.investor[i].id === $scope.temp.investorId) {
                    $rootScope.data.investor = $scope.list.investor[i];
                    break;
                }
            }
            $state.go('index.investor');
        };



    }



})();


(function () {

    angular.getApplicationModule()
            .controller('CovenantCtrl', CovenantCtrl);

    /*
     * MainCtrl - controller
     */
    function CovenantCtrl($rootScope, $scope, $log, $http) {
        $log.debug('CovenantCtrl is loaded');
        $scope.userName = 'Example user';
        $scope.helloText = 'Hello You Welcome in SeedProject';
        $scope.descriptionText = 'It is an application skeleton for a typical AngularJS web app. You can use it to quickly bootstrap your angular webapp projects and dev environment for these projects.';

        $scope.data = {};
        $scope.data.cInvestor = {};
        $scope.data.selectedCStaff = {};
        $scope.dInput = {};
        $scope.data.acquisition = {};


        $scope.data.cInvestor = {};
        if ($rootScope.dctrl !== undefined && $rootScope.dctrl.investor !== undefined) {
            $scope.data.cInvestor = $rootScope.dctrl.investor;
        }


        $scope.selectCStaff = function () {
            $log.debug("selectCStaff called with " + $scope.dInput.selectedCStaffId);

            for (var i = 0; i < $scope.data.covenantStaff.length; i++) {
                if ($scope.data.covenantStaff[i].id === $scope.dInput.selectedCStaffId) {
                    $scope.data.selectedCStaff = $scope.data.covenantStaff[i];
                    break;
                }
            }
        };

        $scope.getCovenantStaff = function () {
            $http.get("/staff/ret?key=covenant")
                    .then(function (response) {
                        $log.debug(response);
                        $scope.data.covenantStaff = response.data;
                        $scope.dInput.selectedCStaffId = response.data[0].rank.id;
                        $scope.data.selectedCStaff = response.data[0];
                        $log.debug("getInvestor responsed sucess with " + $scope.dInput.selectedCStaffId);
                    });

        };
        $scope.getCovenantStaff();

        $scope.getAccount = function () {
            $http.get("/investor/ret/byaccountid?value=" + $scope.data.cInvestor.accountId)
                    .then(function (response) {
                        $log.debug(response);
                        $scope.data.cInvestor = response.data;
                        $log.debug("getAccount responsed sucess with " + $scope.data.cInvestor.id);
                    });

        };

        $scope.getSite = function () {
            $http.get("/site/ret?key=")
                    .then(function (response) {
                        $log.debug(response);
                        $scope.data.site = response.data;
                        $scope.dInput.selectedSiteId = response.data[0].id;
                        $scope.data.selectedSite = response.data[0];
                        $log.debug("getSite responsed sucess with " + $scope.dInput.selectedSiteId);
                        $scope.selectSite();
                    });

        };
        $scope.getSite();
        $scope.selectSite = function () {
            $log.debug("selectSite called with " + $scope.dInput.selectedSiteId);
            $http.get("/tower/ret/bysiteid?value=" + $scope.dInput.selectedSiteId)
                    .then(function (response) {
                        $log.debug(response);
                        $scope.data.tower = response.data;
                        $scope.dInput.selectedTowerId = response.data[0].id;
                        $scope.data.selectedTower = response.data[0];
                        $log.debug("getSite responsed sucess with " + $scope.dInput.selectedTowerId);
                        $scope.selectTower();
                    });
        };

        $scope.selectTower = function () {
            $log.debug("selectSite called with " + $scope.dInput.selectedTowerId);
            $http.get("/investment/ret/onsale/bytowerid?value=" + $scope.dInput.selectedTowerId)
                    .then(function (response) {

                        $scope.data.investment = response.data;
                        $scope.dInput.selectedInvestmentId = response.data[0].id;
                        $scope.data.selectedInvestment = response.data[0];
                        $log.debug("selectTower responsed sucess with selectedInvestmentId: " + $scope.dInput.selectedInvestmentId);
                        $scope.selectInvestment();
                    });
        };

        $scope.selectInvestment = function () {
            $log.debug("selectInvestment called with selectedInvestmentId:" + $scope.dInput.selectedInvestmentId);

            for (var i = 0; i < $scope.data.investment.length; i++) {
                if ($scope.data.investment[i].id === $scope.dInput.selectedInvestmentId) {
                    $scope.data.selectedInvestment = $scope.data.investment[i];
                    break;
                }
            }
            $log.debug($scope.data.selectedInvestment);
        };

        $scope.data.acquisition.investments = [];
        $scope.data.acquisition.totalFee = 0;
        $scope.addCInvestment = function () {
            $scope.data.selectedInvestment.soldRate = parseInt($scope.data.selectedInvestment.soldRate);
            $scope.data.acquisition.investments.push($scope.data.selectedInvestment);
            $scope.data.acquisition.totalFee += $scope.data.selectedInvestment.soldRate;
            alert("add " + $scope.data.selectedInvestment);

        };

        $scope.saveAcquisition = function () {
            alert("saveAcquisition called!");
            $log.debug($scope.data.acquisition);
            var payload = $scope.data.acquisition;
            $http.post('/acquisition/addnew', payload)
                    .success(function (response) {
                        $log.debug(response);
                    }).error(function (err) {
                $log.debug(err);
            });
        };

    }


})();

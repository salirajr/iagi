
(function () {

    angular.getApplicationModule()
            .controller('CovenantCtrl', CovenantCtrl);

    /*
     * MainCtrl - controller
     */
    function CovenantCtrl($rootScope, $scope, $log, $http, $window) {
        $log.debug('CovenantCtrl is loaded');
        $scope.userName = 'Example user';
        $scope.helloText = 'Hello You Welcome in SeedProject';
        $scope.descriptionText = 'It is an application skeleton for a typical AngularJS web app. You can use it to quickly bootstrap your angular webapp projects and dev environment for these projects.';

        $scope.data = {};
        $scope.dInput = {};
        $scope.data.acquisition = {};
        $scope.data.acquisition.investor = {};
        $scope.data.acquisition.staff = {};


        if ($rootScope.data !== undefined && $rootScope.data.investor !== undefined) {
            $scope.data.acquisition.investor = $rootScope.data.investor;
            $rootScope.data.investor = undefined;
        }


        $scope.selectCStaff = function () {
            $log.debug("selectCStaff called with " + $scope.dInput.selectedCStaffId);

            for (var i = 0; i < $scope.data.covenantStaff.length; i++) {
                if ($scope.data.covenantStaff[i].id === $scope.dInput.selectedCStaffId) {
                    $scope.data.acquisition.staff = $scope.data.covenantStaff[i];
                    break;
                }
            }
        };

        $scope.getCovenantStaff = function () {
            $http.get("/api/staff/ret?key=covenant")
                    .then(function (response) {
                        $log.debug(response);
                        $scope.data.covenantStaff = response.data;
                        $scope.dInput.selectedCStaffId = response.data[0].rank.id;
                        $scope.data.acquisition.staff = response.data[0];
                        $log.debug("getInvestor responsed sucess with " + $scope.dInput.selectedCStaffId);
                    });

        };
        $scope.getCovenantStaff();

        $scope.getAccount = function () {
            $http.get("/api/investor/ret/byaccountid?value=" + $scope.data.acquisition.investor.accountId)
                    .then(function (response) {
                        $log.debug(response);
                        $scope.data.acquisition.investor = response.data;
                        $log.debug("getAccount responsed sucess with " + $scope.data.acquisition.investor.accountId);
                    });

        };

        $scope.getSite = function () {
            $http.get("/api/site/ret?key=")
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
            $http.get("/api/tower/ret/bysiteid?value=" + $scope.dInput.selectedSiteId)
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
            $http.get("/api/investment/ret/onsale/bytowerid?value=" + $scope.dInput.selectedTowerId)
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
        $scope.data.acquisition.totalMarketFee = 0;
        $scope.addCInvestment = function () {
            $scope.data.selectedInvestment.soldRate = parseInt($scope.data.selectedInvestment.soldRate);
            var investment = $scope.data.selectedInvestment;
            $scope.data.acquisition.investments.push(investment);
            $scope.data.acquisition.totalFee += $scope.data.selectedInvestment.soldRate;
            $scope.data.acquisition.totalMarketFee += $scope.data.selectedInvestment.marketRate;
            alert("add " + $scope.data.selectedInvestment);

        };

        $scope.saveAcquisition = function (fnCallback) {
            var isConfirm = confirm("Apakah anda ingin menyimpan data rekord berikut?");
            if (!isConfirm)
                return;
            $log.debug($scope.data.acquisition);
            var payload = $scope.data.acquisition;
            var date = new Date();
            payload.startDate = new Date(new Date(date.getFullYear(), date.getMonth(), 2).setMonth(date.getMonth() + 1));
            if ($scope.data.acquisition.type === 'SOFT_INSTALLMENT') {
                payload.endDate = new Date(new Date(date.getFullYear(), date.getMonth(), 2).setMonth(date.getMonth() + $scope.data.acquisition.nPeriod));
            } else if ($scope.data.acquisition.type === 'INSTALLMENT') {
                payload.endDate = new Date(new Date(date.getFullYear(), date.getMonth(), 2).setMonth(date.getMonth() + 27));
            } else {
                payload.startDate = new Date();
                payload.endDate = new Date();
            }
            $http.post('/api/acquisition/addnew', payload)
                    .success(function (response) {
                        $log.debug("addnew success! " + response);
                        $scope.data.acquisition.id = response;
                        if (fnCallback) {
                            fnCallback();
                        }
                    }).error(function (err) {
                $log.debug(err);
            });
        };


        $scope.generateAkad = function () {
            var isConfirm = confirm("Apakah anda yakin untuk men-generate akad berdasarkan data rekord berikut?");
            if (!isConfirm)
                return;
            $http.post('/api/acquisition/generateakad?id=' + $scope.data.acquisition.id, {}, {responseType: 'arraybuffer'})
                    .success(function (response) {
                        $log.debug("generateakad success!");
                        var file = new Blob([response], {type: 'application/pdf'});
                        var fileURL = URL.createObjectURL(file);
                        $window.open(fileURL);
                    }).error(function (err) {
                $log.debug(err);
            });

        };

    }


})();

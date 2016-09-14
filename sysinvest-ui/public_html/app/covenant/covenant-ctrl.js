
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
        $scope.temp = {};
        $scope.dInput = {};
        $scope.data.acquisition = {};
        $scope.data.acquisition.investor = {};
        $scope.data.acquisition.staff = {};

        $scope.temp.dpDate = new Date().toISOString().slice(0, 10);
        $scope.temp.bookDate = new Date().toISOString().slice(0, 10);
        $scope.temp.todayDate = new Date().toISOString().slice(0, 10);
        $scope.temp.repaymentDate = new Date().toISOString().slice(0, 10);
        $scope.temp.startDate = new Date().toISOString().slice(0, 10);


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
                        $scope.dInput.selectedCStaffId = response.data[0].id;
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
            $scope.data.investment = [];
            $http.get("/api/investment/ret/onsale/bytowerid?value=" + $scope.dInput.selectedTowerId)
                    .then(function (response) {
                        $log.debug(response);
                        if (response.data.length <= 0) {
                            return;
                        }
                        $scope.data.investment = response.data;
                        $scope.dInput.selectedInvestmentId = response.data[0].id;
                        $scope.data.selectedInvestment = response.data[0];
                        $scope.selectInvestment();
                    });
        };

        $scope.selectInvestment = function () {
            $scope.data.selectedInvestment = {};

            for (var i = 0; i < $scope.data.investment.length; i++) {
                if ($scope.data.investment[i].id === $scope.dInput.selectedInvestmentId) {
                    angular.copy($scope.data.investment[i], $scope.data.selectedInvestment);
                    break;
                }
            }
        };


        $scope.data.acquisition.investments = [];
        $scope.data.acquisition.rate = 0;
        $scope.data.acquisition.marketRate = 0;

        $scope.addCInvestment = function () {
            $log.debug();
            var investment = {};
            angular.copy($scope.data.selectedInvestment, investment);
            var isExist = false;
            var i = 0;
            for (i = 0; i < $scope.data.acquisition.investments.length; i++) {
                if ($scope.data.acquisition.investments[i].id === $scope.dInput.selectedInvestmentId) {
                    isExist = true;
                    break;
                }
            }
            $log.debug("i=" + i + " isExist=" + isExist);
            if (isExist) {
                var isConfirm = confirm("Aparkost ini telah masuk dalam daftar investasi, apakah anda ingin menggantinya?");
                if (isConfirm) {
                    $scope.data.acquisition.marketRate += parseInt(investment.marketRate) - parseInt($scope.data.acquisition.investments[i].marketRate);
                    $scope.data.acquisition.rate += parseInt(investment.soldRate) - parseInt($scope.data.acquisition.investments[i].soldRate);
                    $scope.data.acquisition.investments[i] = investment;
                }
            } else {
                $scope.data.acquisition.investments.push(investment);
                $scope.data.acquisition.marketRate += parseInt(investment.marketRate);
                $scope.data.acquisition.rate += parseInt(investment.soldRate);
            }
        };

        $scope.temp.payments = [];
        $scope.paymentActivation = function () {
            $scope.temp.payments = [];
            var payments = [];
            $scope.temp.payments = [];
            if ($scope.data.acquisition.type === 'INSTALLMENT') {
                var installment = $scope.data.acquisition.payableFee / 27;
                var lastInstallment = $scope.data.acquisition.payableFee;
                var d = new Date();
                var tD;
                var i = 1;
                for (; i < 27; i++) {
                    tD = new Date(new Date(d.getFullYear(), d.getMonth(), 2).setMonth(d.getMonth() + i));
                    payments.push(createPayment(i + 1, installment, "Cicilan " + i, tD.toISOString().slice(0, 10)));
                    lastInstallment -= installment;
                }
                tD = new Date(new Date(d.getFullYear(), d.getMonth(), 2).setMonth(d.getMonth() + i));
                payments.push(createPayment(i + 1, lastInstallment, "Cicilan Terakhir", tD.toISOString().slice(0, 10)));
            } else if ($scope.data.acquisition.type === 'CASH') {
                payments.push(createPayment(2, $scope.data.acquisition.payableFee, "LUNAS", new Date($scope.temp.repaymentDate)));
                $log.debug("CASH CALLED!");
            }
            angular.copy(payments, $scope.temp.payments);
        };

        function createPayment(i, installment, type, paydate) {
            var temp = {};
            temp.index = i;
            temp.nominal = installment;
            temp.type = type;
            temp.paydate = paydate;
            return temp;
        }

        $scope.saveAcquisition = function (fnCallback) {
            var isConfirm = confirm("Apakah anda ingin menyimpan data rekord berikut?");
            if (!isConfirm)
                return;
            $scope.data.acquisition.payments = [];
            $scope.data.acquisition.payments.push(createPayment(0, $scope.temp.bookFee, "Uang Tanda Jadi", new Date($scope.temp.bookDate)));
            $scope.data.acquisition.payments.push(createPayment(1, $scope.temp.dpFee, "Uang Muka", new Date($scope.temp.dpDate)));
            for (var i = 0; i < $scope.temp.payments.length; i++) {
                $scope.data.acquisition.payments.push($scope.temp.payments[i]);
            }

            $log.debug($scope.data.acquisition);

            var payload = $scope.data.acquisition;

            $http.post('/api/acquisition/addnew', payload)
                    .success(function (response) {
                        $scope.data.acquisition = response;
                        alert("Data Investasi telah disimpan!");
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
                        var file = new Blob([response], {type: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'});
                        var fileURL = URL.createObjectURL(file);
                        $window.open(fileURL);
                    }).error(function (err) {
                $log.debug(err);
            });

        };

    }


})();
